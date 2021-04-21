package com.desafio.magalu.service;

import com.desafio.magalu.controller.response.ProductResponse;
import com.desafio.magalu.domain.ProductDomain;
import com.desafio.magalu.domain.RoleDomain;
import com.desafio.magalu.domain.UserDomain;
import com.desafio.magalu.exception.ObjectNotFoundException;
import com.desafio.magalu.exception.UserDoesNotMatchAuthorizationException;
import com.desafio.magalu.exception.UserEmailAlreadyExistsException;
import com.desafio.magalu.mapper.UserConverter;
import com.desafio.magalu.mapper.ProductConverter;
import com.desafio.magalu.repository.product.ProductEntity;
import com.desafio.magalu.repository.user.UserEntity;
import com.desafio.magalu.repository.user.UserRepository;
import com.desafio.magalu.security.jwt.JwtUtil;
import com.desafio.magalu.utils.PageCustom;
import com.desafio.magalu.utils.PageModel;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {


    @Autowired
    public UserService(UserRepository userRepository, ProductService productService, RoleService roleService, HttpServletRequest request, JwtUtil jwtUtil, RedisTemplate redisTemplate) {
        this.userRepository = userRepository;
        this.productService = productService;
        this.roleService = roleService;
        this.redisTemplate = redisTemplate;
        this.request = request;
        this.jwtUtil = jwtUtil;
    }

    private UserRepository userRepository;

    private ProductService productService;

    private RoleService roleService;

    private HttpServletRequest request;

    private JwtUtil jwtUtil;

    private RedisTemplate redisTemplate;

    private UserConverter userConverter = Mappers.getMapper(UserConverter.class);

    private ProductConverter productConverter = Mappers.getMapper(ProductConverter.class);


    public UserDomain create(UserDomain userDomain) {

        Optional<UserEntity> optClient = userRepository.findByEmail(userDomain.getEmail());
        if (optClient.isPresent()) {
            throw new UserEmailAlreadyExistsException("Email in use.");
        }
        RoleDomain roleUser = roleService.read("ROLE_USER");

        userDomain.setRoles(new ArrayList<>(Arrays.asList(roleUser)));

        UserEntity userEntity = userConverter.domainToEntity(userDomain);

        UserEntity userSaved = userRepository.save(userEntity);

        return userConverter.entityToDomain(userSaved);
    }

    @Cacheable(cacheNames = "UserById", key = "#id")
    public UserDomain read(Long id) {
        String token = request.getHeader("Authorization");
        Optional<UserEntity> optClient = userRepository.findById(id);
        validUsers(id, token);
        if (optClient.isPresent()) {
            UserDomain userDomain = userConverter.entityToDomain(optClient.get());
            return userDomain;
        }
        throw new ObjectNotFoundException("User not found.");
    }

    @CachePut(cacheNames = {"UserById"}, key = "#id")
    public UserDomain update(Long id, UserDomain userDomain) {
        String token = request.getHeader("Authorization");
        validUsers(id, token);

        Optional<UserEntity> optClient = userRepository.findById(id);
        Optional<UserEntity> optClientByTryNewEmail = userRepository.findByEmail(userDomain.getEmail());
        if (optClientByTryNewEmail.isPresent() && optClientByTryNewEmail.get().getId() != id) {
            throw new UserEmailAlreadyExistsException("Email already in use.");
        }
        if (optClient.isPresent()) {
            UserEntity clientEntity = optClient.get();
            clientEntity.setName(userDomain.getName());
            clientEntity.setEmail(userDomain.getEmail());
            UserEntity clientUpdated = userRepository.save(clientEntity);
            userDomain = userConverter.entityToDomain(clientUpdated);
            return userDomain;
        }
        throw new ObjectNotFoundException("User not found.");
    }

    @CacheEvict(cacheNames = "UserById", key = "#id")
    public void delete(Long id) {
        userRepository.deleteById(id);
    }


    public PageCustom getAllFavorites(Long idUser, Pageable pageable) {
        String token = request.getHeader("Authorization");
        validUsers(idUser, token);

        Optional<UserEntity> optUser = userRepository.findById(idUser);

        if (optUser.isPresent()) {

            Set<ProductResponse> productResponses = productService.converterToResponsePageModel(optUser.get().getFavoriteProducts());

            PageModel pageModel = new PageModel(new ArrayList<ProductResponse>(productResponses), pageable.getPageSize(), pageable.getPageNumber());

            return pageModel.getPagination();
        }
        throw new ObjectNotFoundException("User not found.");

    }

    public void addProductToFavoriteList(Long idUser, ProductDomain productDomain) {
        String token = request.getHeader("Authorization");
        validUsers(idUser, token);
        ProductEntity productEntity = null;
        Optional<UserEntity> optUser = userRepository.findById(idUser);
        if (optUser.isPresent()) {
            productDomain = productService.read(productDomain.getIdProduct());
            productEntity = productConverter.domainToEntity(productDomain);
            UserEntity userEntity = optUser.get();
            if (productDomain.getId() != null) {
                Set<ProductEntity> favoriteProducts = userEntity.getFavoriteProducts();
                if (favoriteProducts.contains(productEntity)) {
                    return;
                }
            }
            userEntity.getFavoriteProducts().add(productEntity);
            userRepository.save(userEntity);
            return;
        }
        throw new ObjectNotFoundException("User Not Found.");
    }

    public void removeProductOfFavoriteList(Long idUser, ProductDomain productDomain) {
        String token = request.getHeader("Authorization");
        validUsers(idUser, token);
        UserEntity userEntity = null;
        ProductEntity productEntity = null;
        Optional<UserEntity> optUser = userRepository.findById(idUser);

        if (optUser.isPresent()) {
            userEntity = optUser.get();
            productDomain = productService.read(productDomain.getIdProduct());
            productEntity = productConverter.domainToEntity(productDomain);
            if (userEntity.getFavoriteProducts().contains(productEntity)) {
                userEntity.getFavoriteProducts().remove(productEntity);
                userRepository.save(userEntity);
                return;
            }
            return;
        }
        throw new ObjectNotFoundException("Cliente not found.");
    }

    public void validUsers(Long id, String token) {
        Long idUserLogged = Long.parseLong(JwtUtil.getId(token));

        if (id != idUserLogged) {
            throw new UserDoesNotMatchAuthorizationException("Users does not match in action.");
        }

    }


}
