package com.desafio.magalu.repository.client;


import com.desafio.magalu.repository.product.ProductEntity;


import javax.persistence.*;
import java.util.Set;

@Entity(name = "client")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;


    @ManyToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinTable(
            name = "client_products",
            joinColumns = {@JoinColumn(name = "client_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")})
    private Set<ProductEntity> favoriteProducts;

    public ClientEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<ProductEntity> getFavoriteProducts() {
        return favoriteProducts;
    }

    public void setFavoriteProducts(Set<ProductEntity> favoriteProducts) {
        this.favoriteProducts = favoriteProducts;
    }
}
