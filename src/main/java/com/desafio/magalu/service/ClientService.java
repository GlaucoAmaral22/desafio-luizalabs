package com.desafio.magalu.service;

import com.desafio.magalu.domain.ClientDomain;
import com.desafio.magalu.domain.ProductDomain;
import com.desafio.magalu.exception.ClientAlreadyExistsException;
import com.desafio.magalu.exception.ObjectNotFoundException;
import com.desafio.magalu.integration.apiproducts.ProductService;
import com.desafio.magalu.mapper.ClientMapper;
import com.desafio.magalu.mapper.ProductMapper;
import com.desafio.magalu.repository.client.ClientEntity;
import com.desafio.magalu.repository.client.ClientRepository;
import com.desafio.magalu.repository.product.ProductEntity;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.Set;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ProductService productService;

    ClientMapper clientMapper = Mappers.getMapper(ClientMapper.class);

    ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);


    public ClientDomain create(ClientDomain cltDomain) {

        Optional<ClientEntity> optClient = clientRepository.findByEmail(cltDomain.getEmail());

        if (optClient.isPresent()) {
            throw new ClientAlreadyExistsException("Email in use.");
        }

        ClientEntity clientEntity = clientMapper.domainToEntity(cltDomain);

        ClientEntity clientSaved = clientRepository.save(clientEntity);

        return clientMapper.entityToDomain(clientSaved);
    }

    public ClientDomain read(Long id) {

        Optional<ClientEntity> optClient = clientRepository.findById(id);

        if (optClient.isPresent()) {
            ClientDomain cltDomain = clientMapper.entityToDomain(optClient.get());
            return cltDomain;
        }
        throw new ObjectNotFoundException("Client not found.");
    }

    public ClientDomain update(Long id, ClientDomain cltDomain) {

        Optional<ClientEntity> optClient = clientRepository.findById(id);

        Optional<ClientEntity> optClientByEmail = clientRepository.findByEmail(cltDomain.getEmail());

        if (optClientByEmail.isPresent() && optClientByEmail.get().getId() == id) {
            if (optClient.isPresent()) {
                ClientEntity clientEntity = optClient.get();
                clientEntity.setName(cltDomain.getName());
                clientEntity.setEmail(cltDomain.getEmail());
                ClientEntity clientUpdated = clientRepository.save(clientEntity);
                cltDomain = clientMapper.entityToDomain(clientUpdated);
                return cltDomain;
            }
            throw new ObjectNotFoundException("Cliente não encontrado.");
        }
        throw new ClientAlreadyExistsException("Email já em uso.");
    }

    public void delete(Long id) {
        clientRepository.deleteById(id);
    }

    public void addProductToFavoriteList(Long idUser, ProductDomain productDomain) {
        ProductEntity productEntity = null;
        Optional<ClientEntity> optClient = clientRepository.findById(idUser);
        if (optClient.isPresent()) {
            productDomain = productService.consultProduct(productDomain);
            productEntity = productMapper.domainToEntity(productDomain);
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
        throw new ObjectNotFoundException("Cliente não encontrado.");
    }

    public void removeProductOfFavoriteList(Long idUser, ProductDomain productDomain){
        ClientEntity cltEntity = null;
        ProductEntity productEntity = null;
        Optional<ClientEntity> optClient = clientRepository.findById(idUser);

        if (optClient.isPresent()) {
            cltEntity = optClient.get();
            productDomain = productService.read(productDomain.getIdProduct());
            productEntity = productMapper.domainToEntity(productDomain);
            if(cltEntity.getFavoriteProducts().contains(productEntity)){
                cltEntity.getFavoriteProducts().remove(productEntity);
                clientRepository.save(cltEntity);
                return;
            }
            return;
        }
        throw new ObjectNotFoundException("Cliente not found.");
    }





}
