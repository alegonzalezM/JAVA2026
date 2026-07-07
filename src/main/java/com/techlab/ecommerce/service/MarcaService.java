package com.techlab.ecommerce.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.techlab.ecommerce.exception.MarcaNoEncontradaException;
import com.techlab.ecommerce.model.Marca;
import com.techlab.ecommerce.repository.MarcaRepository;

@Service
public class MarcaService {

    private final MarcaRepository marcaRepository;

    public MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    public Marca obtenerPorId(Integer id) {
        return marcaRepository.findById(id)
        .orElseThrow(() -> new MarcaNoEncontradaException("La marca con ID " + id + " no existe."));
    } 
    
    public Marca guardar(Marca marca) {
        if (marca.getId() != null) {
            throw new IllegalArgumentException("No puedes enviar un ID al crear una nueva marca.");
        }
        if (marcaRepository.existsByNombre(marca.getNombre())) {
            throw new IllegalArgumentException("La marca '" + marca.getNombre() + "' ya está registrada.");
        }
        return marcaRepository.save(marca);
    }

    public List<Marca> listarTodas() {
        return marcaRepository.findAll();
    }

    public void eliminar(Integer id) {
        Marca m = obtenerPorId(id); 
        marcaRepository.delete(m);
    }
    public Marca actualizar(Integer id, Marca datos){
        Marca marcaExistente = obtenerPorId(id);
        if (!marcaExistente.getNombre().equalsIgnoreCase(datos.getNombre())) {
            if (marcaRepository.existsByNombre(datos.getNombre())) {
                throw new IllegalArgumentException("Ya existe una marca con el nombre '" + datos.getNombre() + "'.");
            }
        }
        marcaExistente.setNombre(datos.getNombre());
        return marcaRepository.save(marcaExistente);
    }
} 
