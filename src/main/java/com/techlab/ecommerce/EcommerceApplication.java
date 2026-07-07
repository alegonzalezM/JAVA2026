package com.techlab.ecommerce;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.techlab.ecommerce.model.Carrito;
import com.techlab.ecommerce.model.Categoria;
import com.techlab.ecommerce.model.Cliente;
import com.techlab.ecommerce.model.Marca;
import com.techlab.ecommerce.model.Producto;
import com.techlab.ecommerce.repository.CarritoRepository;
import com.techlab.ecommerce.repository.ClienteRepository;
import com.techlab.ecommerce.repository.MarcaRepository;
import com.techlab.ecommerce.security.jwt.JwtService;
import com.techlab.ecommerce.security.user.Usuario;
import com.techlab.ecommerce.security.user.UsuarioRepository;
import com.techlab.ecommerce.service.ProductoService;

@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

	@Bean
	CommandLineRunner cargarDatos(ProductoService productoService, MarcaRepository marcaRepository, CarritoRepository carritoRepository, JwtService jwtService, UsuarioRepository usuarioRepository, ClienteRepository clienteRepository){
		return args -> {
			if (productoService.listarTodos().isEmpty()) { 
				Marca raleigh = buscarOCrearMarca("Raleigh", marcaRepository);
				Marca shimano = buscarOCrearMarca("Shimano", marcaRepository);
				Marca firebird = buscarOCrearMarca("Firebird", marcaRepository);
				Marca giro = buscarOCrearMarca("Giro", marcaRepository);
				Marca parkTool = buscarOCrearMarca("Park Tool", marcaRepository);
				Marca ktm = buscarOCrearMarca("KTM", marcaRepository);
				Marca beto = buscarOCrearMarca("Beto", marcaRepository);
				Marca continental = buscarOCrearMarca("Continental", marcaRepository);

				productoService.guardar(new Producto("BICI001", "Bicicleta Mountain Bike R29 Pro", 450000, 12, Categoria.BICICLETAS, "/imagenes/bicicleta-raleigh-8.0.jpg", raleigh));
				productoService.guardar(new Producto("REP002", "Pastillas de Freno Hidráulico", 15500, 45, Categoria.REPUESTOS, "/imagenes/pastillas.jpg", shimano));
				productoService.guardar(new Producto("ACCE003", "Casco de Ciclismo con Luz LED", 38000, 20, Categoria.ACCESORIOS, "/imagenes/casco.jpg", giro));
				productoService.guardar(new Producto("BICI005", "Bicicleta infantil rod.20Firebird", 280000, 12, Categoria.BICICLETAS, "/imagenes/bicicleta-firebird-rod20-nena.jpg", firebird));
				productoService.guardar(new Producto("BICI006", "Bicicleta infantil rod.16 Firebird", 190000, 11, Categoria.BICICLETAS, "/imagenes/bicicleta-firebird-rod16-nena.jpg", firebird));
				productoService.guardar(new Producto("VARIOS004", "Multiherramienta de Bolsillo 16 en 1", 18900, 30, Categoria.HERRAMIENTAS, "/imagenes/multiherramienta.jpg", parkTool));
				productoService.guardar(new Producto("BICI002", "Bicicleta MTB R29 Firebird", 420000, 9, Categoria.BICICLETAS, "/imagenes/bicicleta-firebird.jpg", firebird));
				productoService.guardar(new Producto("IND001", "Jersey KTM", 39500, 18, Categoria.INDUMENTARIA, "/imagenes/jersey.jpg", ktm));
				productoService.guardar(new Producto("REP003", "Cubierta Continental 29x2.0", 58900, 60, Categoria.REPUESTOS, "/imagenes/cubierta.jpg", continental));
				productoService.guardar(new Producto("ACCE004", "Inflador Beto", 25800, 22, Categoria.ACCESORIOS, "/imagenes/inflador-beto.jpg", beto));
				
				System.out.println("¡Marcas y productos iniciales cargados con éxito!");
			} 
		if (carritoRepository.count() == 0) {
  			  Carrito carritoInicial = new Carrito();
    
   		 carritoRepository.save(carritoInicial); 
    
    System.out.println("🛒 Carrito inicial creado : " + carritoInicial.getId());
}

if (clienteRepository.count() == 0) {
    clienteRepository.saveAll(java.util.List.of(
        new Cliente(38451293, "Juan", "Pérez", "juan.perez@email.com", "1145896321"),
        new Cliente(40125894, "María", "Gómez", "maria.gomez@email.com", "1156897412"),
        new Cliente(35684125, "Carlos", "Rodríguez", "carlos.rod@email.com", "1132659874"),
        new Cliente(42158963, "Ana", "Martínez", "ana.mtz@email.com", "1169853214"),
        new Cliente(37894561, "Luis", "López", "luis.lopez@email.com", "1125413698"),
        new Cliente(39561248, "Laura", "Díaz", "laura.diaz@email.com", "1178541236"),
        new Cliente(36412589, "Diego", "Sánchez", "diego.sanchez@email.com", "1141253698"),
        new Cliente(41258964, "Sofía", "Fernández", "sofia.fer@email.com", "1152639874"),
        new Cliente(34895612, "Javier", "Álvarez", "javier.alv@email.com", "1136985214"),
        new Cliente(43125489, "Elena", "Romero", "elena.romero@email.com", "1165847123")
    ));
    System.out.println("👥 Lista de 10 clientes cargada con éxito de forma masiva!");
}

			Usuario usuarioPrueba;
			var usuarios = usuarioRepository.findAll();

			if (usuarios.isEmpty()) {
				usuarioPrueba = new com.techlab.ecommerce.security.user.Usuario();
				usuarioPrueba.setEmail("usuario.prueba@techlab.com"); 
				usuarioPrueba.setPassword("password123"); 
				usuarioPrueba = usuarioRepository.save(usuarioPrueba);
				System.out.println("¡Usuario de prueba creado en la base de datos!");
			} else {
				usuarioPrueba = usuarios.get(0);
			}

		}; 
	}

	private Marca buscarOCrearMarca(String nombreMarca, MarcaRepository marcaRepository) {
		if (marcaRepository.existsByNombre(nombreMarca)) {
			return marcaRepository.findAll().stream()
					.filter(m -> nombreMarca.equalsIgnoreCase(m.getNombre()))
					.findFirst()
					.orElseThrow();
		} else {
			Marca nueva = new Marca();
			nueva.setNombre(nombreMarca);
			return marcaRepository.save(nueva);
		}
	}
}

// taskkill //F //IM java.exe
// ./mvnw clean compile
// ./mvnw spring-boot:run
