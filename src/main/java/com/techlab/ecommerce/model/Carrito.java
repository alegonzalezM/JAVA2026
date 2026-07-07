package com.techlab.ecommerce.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carrito")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // OneToMany: un carrito tiene muchos CarritoProducto.
    // mappedBy indica que la relación está definida en CarritoProducto (campo "carrito").
    // CascadeType.ALL: si se elimina el carrito, se eliminan sus CarritoProducto.
    // orphanRemoval: si se quita un CarritoProducto de la lista, se elimina de la base.
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoProducto> productos = new ArrayList<>();
}