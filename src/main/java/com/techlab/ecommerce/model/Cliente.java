package com.techlab.ecommerce.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "clientes")
public class Cliente {

@Id
@Min(value = 1000000, message = "El DNI debe tener como mínimo 7 dígitos")
@Max(value = 99999999, message = "El DNI no puede tener más de 8 dígitos")
@Column(name = "dni")
private int dni;
    
@NotBlank(message="El nombre del cliente no puede estar vacío")
@Column(name="nombre", nullable=false, length=50)
    private String nombre;

@NotBlank(message="El apellido del cliente no puede estar vacío")
@Column(name="apellido", nullable=false, length=50)
    private String apellido;

@NotBlank(message = "El email no puede estar vacío")
@Email(message = "El formato del correo electrónico es incorrecto") 
@Column(name="email", nullable = false, unique = true, length = 100)
private String email;

@NotBlank(message = "El teléfono es obligatorio")
@Size(min = 8, max = 15)
@Pattern(regexp = "^[0-9]+$", message = "El teléfono debe contener solo números")
@Column(name="telefono", nullable = false, length = 30)
    private String telefono;

  }


