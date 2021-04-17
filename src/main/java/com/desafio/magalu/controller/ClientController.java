package com.desafio.magalu.controller;

import com.desafio.magalu.controller.request.ClientRequest;
import com.desafio.magalu.controller.request.ProductRequest;
import com.desafio.magalu.controller.response.ClientResponse;
import com.desafio.magalu.domain.ClientDomain;
import com.desafio.magalu.domain.ProductDomain;
import com.desafio.magalu.mapper.ClientMapper;
import com.desafio.magalu.mapper.ProductMapper;
import com.desafio.magalu.service.ClientService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestControllerAdvice
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    ClientMapper clientMapper = Mappers.getMapper(ClientMapper.class);

    ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);


    @PostMapping()
    @Secured({"ROLE_USER"})
    public ResponseEntity create(@Valid @RequestBody ClientRequest request){

        ClientDomain clientDomain = clientMapper.requestToDomain(request);

        ClientDomain clientSaved = clientService.create(clientDomain);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(clientSaved.getId()).toUri();

        return ResponseEntity.created(location).build();
    }


    @GetMapping("{id}")
    @Secured({"ROLE_USER"})
    public ResponseEntity read(@PathVariable("id") Long id){

        ClientDomain clientDomain = clientService.read(id);

        ClientResponse clientResponse = clientMapper.domainToResponse(clientDomain);

        return ResponseEntity.ok(clientResponse);

    }

    @PutMapping("{id}")
    @Secured({"ROLE_USER"})
    public ResponseEntity update(@PathVariable("id") Long id,  @RequestBody ClientRequest request) {

        ClientDomain clientDomain = clientMapper.requestToDomain(request);

        clientDomain = clientService.update(id, clientDomain);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("").buildAndExpand().toUri();

        return ResponseEntity.ok().location(location).build();
    }

    @DeleteMapping("{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity delete(@PathVariable("id") Long id){
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("{id}/favorite")
    @Secured({"ROLE_USER"})
    public ResponseEntity addProduct(@PathVariable("id") Long idUser, @RequestBody ProductRequest request){

        ProductDomain productDomain = productMapper.requestToDomain(request);

        clientService.addProductToFavoriteList(idUser, productDomain);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}/favorite")
    @Secured({"ROLE_USER"})
    public ResponseEntity deleteProduct(@PathVariable("id") Long idUser, @RequestBody ProductRequest request){

        ProductDomain productDomain = productMapper.requestToDomain(request);

        clientService.removeProductOfFavoriteList(idUser, productDomain);

        return ResponseEntity.noContent().build();
    }
}
