package com.desafio.magalu.controller;

import com.desafio.magalu.controller.request.UserRequest;
import com.desafio.magalu.controller.request.ProductRequest;
import com.desafio.magalu.controller.response.UserResponse;
import com.desafio.magalu.domain.ProductDomain;
import com.desafio.magalu.domain.UserDomain;
import com.desafio.magalu.mapper.UserConverter;
import com.desafio.magalu.mapper.ProductConverter;
import com.desafio.magalu.service.UserService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestControllerAdvice
@RequestMapping("/api/client/")
public class UserController {

    @Autowired
    public UserController(UserService userService, RedisTemplate redisTemplate) {
        this.userService = userService;
        this.redisTemplate = redisTemplate;
    }

    private UserService userService;

    UserConverter userConverter = Mappers.getMapper(UserConverter.class);

    ProductConverter productConverter = Mappers.getMapper(ProductConverter.class);

    RedisTemplate redisTemplate;

    @PostMapping()
    public ResponseEntity create(@Valid @RequestBody UserRequest request) {

        UserDomain userDomain = userConverter.requestToDomain(request);

        UserDomain clientSaved = userService.create(userDomain);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(clientSaved.getId()).toUri();

        return ResponseEntity.created(location).build();
    }


    @GetMapping("{id}")
    @Secured({"ROLE_USER"})
    public ResponseEntity read(@PathVariable("id") Long id) {

        UserDomain clientDomain = userService.read(id);

        UserResponse userResponse = userConverter.domainToResponse(clientDomain);

        return ResponseEntity.ok(userResponse);

    }

    @PutMapping("{id}")
    @Secured({"ROLE_USER"})
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody UserRequest request) {

        UserDomain userDomain = userConverter.requestToDomain(request);

        UserDomain result = userService.update(id, userDomain);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    @Secured({"ROLE_USER"})
    public ResponseEntity delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}/favorite")
    @Secured({"ROLE_USER"})
    public ResponseEntity allProducts(@PathVariable("id") Long idUser, Pageable pageable) {

        return ResponseEntity.ok(userService.getAllFavorites(idUser, pageable));

    }

    @PostMapping("{id}/favorite")
    @Secured({"ROLE_USER"})
    public ResponseEntity addProduct(@PathVariable("id") Long idUser, @RequestBody ProductRequest request) {
        ProductDomain productDomain = productConverter.requestToDomain(request);

        userService.addProductToFavoriteList(idUser, productDomain);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}/favorite")
    @Secured({"ROLE_USER"})
    public ResponseEntity deleteProduct(@PathVariable("id") Long idUser, @RequestBody ProductRequest request) {

        ProductDomain productDomain = productConverter.requestToDomain(request);

        userService.removeProductOfFavoriteList(idUser, productDomain);

        return ResponseEntity.noContent().build();
    }


}
