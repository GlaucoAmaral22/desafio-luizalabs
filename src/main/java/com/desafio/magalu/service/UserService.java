package com.desafio.magalu.service;

import com.desafio.magalu.domain.ClientDomain;
import com.desafio.magalu.domain.ProductDomain;
import com.desafio.magalu.domain.UserDomain;
import com.desafio.magalu.exception.ObjectNotFoundException;
import com.desafio.magalu.exception.UserDoesNotMatchAuthorizationException;
import com.desafio.magalu.exception.UserEmailAlreadyExistsException;
import com.desafio.magalu.mapper.UserConverter;
import com.desafio.magalu.mapper.ProductConverter;
import com.desafio.magalu.repository.product.ProductEntity;
import com.desafio.magalu.repository.user.RoleEntity;
import com.desafio.magalu.repository.user.RoleRepository;
import com.desafio.magalu.repository.user.UserEntity;
import com.desafio.magalu.repository.user.UserRepository;
import com.desafio.magalu.security.jwt.JwtUtil;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {


    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, ProductService productService, HttpServletRequest request, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.productService = productService;
        this.request = request;
        this.jwtUtil = jwtUtil;
    }

    UserRepository userRepository;

    //TODO: retirar role daqui e colocar na RoleService e fazer a logica para criar a role de usuário
    RoleRepository roleRepository;

    ProductService productService;

    HttpServletRequest request;

    JwtUtil jwtUtil;

    UserConverter userConverter = Mappers.getMapper(UserConverter.class);

    ProductConverter productConverter = Mappers.getMapper(ProductConverter.class);


    public UserDomain create(UserDomain userDomain) {

        Optional<UserEntity> optClient = userRepository.findByEmail(userDomain.getEmail());

        if (optClient.isPresent()) {
            throw new UserEmailAlreadyExistsException("Email in use.");
        }

        RoleEntity roleUser = roleRepository.findByName("ROLE_USER");
        UserEntity userEntity = userConverter.domainToEntity(userDomain);
        userEntity.setRoles(new ArrayList<>(Arrays.asList(roleUser)));

        UserEntity userSaved = userRepository.save(userEntity);

        return userConverter.entityToDomain(userSaved);
    }

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

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public void addProductToFavoriteList(Long idUser, ProductDomain productDomain) {
        String token = request.getHeader("Authorization");
        validUsers(idUser, token);
        ProductEntity productEntity = null;
        Optional<UserEntity> optClient = userRepository.findById(idUser);
        if (optClient.isPresent()) {
            productDomain = productService.consultProduct(productDomain);
            productEntity = productConverter.domainToEntity(productDomain);
            UserEntity userEntity = optClient.get();
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
        Optional<UserEntity> optClient = userRepository.findById(idUser);

        if (optClient.isPresent()) {
            userEntity = optClient.get();
            productDomain = productService.read(productDomain.getIdProduct());
            productEntity = productConverter.domainToEntity(productDomain);
            if(userEntity.getFavoriteProducts().contains(productEntity)){
                userEntity.getFavoriteProducts().remove(productEntity);
                userRepository.save(userEntity);
                return;
            }
            return;
        }
        throw new ObjectNotFoundException("Cliente not found.");
    }


    public void validUsers(Long id, String token) {

        String email = JwtUtil.getAuthEmail();

        Optional<UserEntity> optUserLogged = userRepository.findByEmail(email);
        Optional<UserEntity> optUserToDoAction = userRepository.findById(id);


        if (optUserLogged.isPresent() && optUserToDoAction.isPresent()) {
            UserDomain userLogged = userConverter.entityToDomain(optUserLogged.get());
            UserDomain userToDoAction = userConverter.entityToDomain(optUserToDoAction.get());

            if (!userLogged.getEmail().equals(userToDoAction.getEmail())) {
                throw new UserDoesNotMatchAuthorizationException("Users does not match in action.");
            }
        }
    }


}
