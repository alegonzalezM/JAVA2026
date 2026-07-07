package com.techlab.ecommerce.service;
import java.util.List;
import org.springframework.stereotype.Service;
import com.techlab.ecommerce.exception.ClienteNoEncontradoException;
import com.techlab.ecommerce.exception.DniInvalidoException;
import com.techlab.ecommerce.model.Cliente;
import com.techlab.ecommerce.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository){
        this.repository= repository;
    }
    public Cliente guardar (Cliente c){
    if (c.getDni() < 1000000 || c.getDni() > 99999999) {
        throw new DniInvalidoException("El DNI está vacío o es incorrecto, se recibió " + c.getDni());
        }
    if (repository.existsById(c.getDni())) {
        throw new DniInvalidoException("El DNI " + c.getDni() + " ya se encuentra registrado.");
    }
        return repository.save(c);
    }

    public List<Cliente> listarTodos() {
    System.out.println("Cantidad de clientes en BD: " + repository.count()); 
    return repository.findAll();
    }
     
    public Cliente obtenerPorDni(int dni) {
                return repository.findById(dni)
        .orElseThrow(() ->  new ClienteNoEncontradoException("No se encontro un cliente con DNI " + dni));
    }

    public Cliente actualizar(int dni, Cliente datos) {
           Cliente c = obtenerPorDni(dni);

      if (datos.getDni() < 10000000 || datos.getDni() > 99999999) {
        throw new DniInvalidoException("El DNI es incorrecto");
        }

        c.setNombre(datos.getNombre());
        c.setApellido(datos.getApellido());
        c.setEmail(datos.getEmail());
        c.setTelefono(datos.getTelefono());

        return repository.save(c);
    }

    public void eliminar(int dni) {
         if (!repository.existsById(dni)) {
        throw new ClienteNoEncontradoException("No se encontró un cliente con DNI " + dni);
    }
    repository.deleteById(dni);

    }
}

