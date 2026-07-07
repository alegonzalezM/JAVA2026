package com.techlab.ecommerce.service;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.techlab.ecommerce.model.Categoria;
import com.techlab.ecommerce.model.Producto;

@Service
public class CategoriaService {
 
    public List<Categoria> listarTodos() {
        // Arrays.asList convierte el array del enum en una Lista de Java
        return Arrays.asList(Categoria.values());
    }

    public List<Producto> findByCategoria(Categoria categoriaEnum) {
        throw new UnsupportedOperationException("Unimplemented method 'findByCategoria'");
    }
 }


