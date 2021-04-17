package com.desafio.magalu.service;

import com.desafio.magalu.domain.ClientDomain;
import com.desafio.magalu.domain.ProductDomain;
import com.desafio.magalu.domain.UserDomain;
import com.desafio.magalu.exception.ClientAlreadyExistsException;
import com.desafio.magalu.exception.ObjectNotFoundException;
import com.desafio.magalu.exception.UserDoesNotMatchAuthorizationException;
import com.desafio.magalu.mapper.UserConverter;
import com.desafio.magalu.mapper.ProductConverter;
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

    JwtUtil  jwtUtil;

    UserConverter userConverter = Mappers.getMapper(UserConverter.class);

    ProductConverter productConverter = Mappers.getMapper(ProductConverter.class);


    public UserDomain create(UserDomain userDomain) {

        Optional<UserEntity> optClient = userRepository.findByEmail(userDomain.getEmail());

        if (optClient.isPresent()) {
            throw new ClientAlreadyExistsException("Email in use.");
        }

        RoleEntity roleUser = roleRepository.findByName("ROLE_USER");
        UserEntity userEntity = userConverter.domainToEntity(userDomain);
        userEntity.setRoles(new ArrayList<>(Arrays.asList(roleUser)));

        UserEntity userSaved = userRepository.save(userEntity);

        return userConverter.entityToDomain(userSaved);
    }

    public UserDomain read(Long id) {

        Optional<UserEntity> optClient = userRepository.findById(id);

        if (validUsers(id, request.getHeader("Authorization") )) {
            UserDomain userDomain = userConverter.entityToDomain(optClient.get());
            return userDomain;
        }
        throw new ObjectNotFoundException("User not found.");
    }

    public ClientDomain update(Long id, ClientDomain cltDomain) {
        /*
        Optional<ClientEntity> optClient = clientRepository.findById(id);

        Optional<ClientEntity> optClientByEmail = clientRepository.findByEmail(cltDomain.getEmail());

        if (optClientByEmail.isPresent() && optClientByEmail.get().getId() == id) {
            if (optClient.isPresent()) {
                ClientEntity clientEntity = optClient.get();
                clientEntity.setName(cltDomain.getName());
                clientEntity.setEmail(cltDomain.getEmail());
                ClientEntity clientUpdated = clientRepository.save(clientEntity);
                cltDomain = clientConverter.entityToDomain(clientUpdated);
                return cltDomain;
            }
            throw new ObjectNotFoundException("Cliente não encontrado.");
        }
        throw new ClientAlreadyExistsException("Email já em uso.");*/
        return null;
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public void addProductToFavoriteList(Long idUser, ProductDomain productDomain) {
        /*ProductEntity productEntity = null;
        Optional<ClientEntity> optClient = clientRepository.findById(idUser);
        if (optClient.isPresent()) {
            productDomain = productService.consultProduct(productDomain);
            productEntity = productConverter.domainToEntity(productDomain);
            ClientEntity clientEntity = optClient.get();
            if (productDomain.getId() != null) {
                Set<ProductEntity> favoriteProducts = clientEntity.getFavoriteProducts();
                if (favoriteProducts.contains(productEntity)) {
                    return;
                }
            }
            clientEntity.getFavoriteProducts().add(productEntity);
            clientRepository.save(clientEntity);
            return;
        }
        throw new ObjectNotFoundException("Cliente não encontrado.");*/
    }

    public void removeProductOfFavoriteList(Long idUser, ProductDomain productDomain){
        /*ClientEntity cltEntity = null;
        ProductEntity productEntity = null;
        Optional<ClientEntity> optClient = clientRepository.findById(idUser);

        if (optClient.isPresent()) {
            cltEntity = optClient.get();
            productDomain = productService.read(productDomain.getIdProduct());
            productEntity = productConverter.domainToEntity(productDomain);
            if(cltEntity.getFavoriteProducts().contains(productEntity)){
                cltEntity.getFavoriteProducts().remove(productEntity);
                clientRepository.save(cltEntity);
                return;
            }
            return;
        }
        throw new ObjectNotFoundException("Cliente not found.");*/
    }


    public boolean validUsers(Long id,  String token){

        String email = JwtUtil.getAuthEmail();

        Optional<UserEntity> optUserLogged = userRepository.findByEmail(email);
        Optional<UserEntity> optUserToDoAction = userRepository.findById(id);


        if(optUserLogged.isPresent() && optUserToDoAction.isPresent()){
            UserDomain userLogged = userConverter.entityToDomain(optUserLogged.get());
            UserDomain userToDoAction = userConverter.entityToDomain(optUserToDoAction.get());

            if(userLogged.getEmail().equals(userToDoAction.getEmail())){
                return true;
            }
        }
        throw new UserDoesNotMatchAuthorizationException("Users does not match in action.");
    }





}
