package com.techlab.ecommerce.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="productos")

    public class Producto {
        @Id
        @GeneratedValue(strategy= GenerationType.IDENTITY)
            private Integer id;

        @Pattern( regexp = "^[a-zA-Z0-9]{5,10}$", message= "El código debe tener entre 5 y 10 caracteres alfanumericos")
        @Column (name="codigo", nullable =false, length=20)
            private String codigo;
        
        @NotBlank(message="El nombre del producto no puede estar vacío")
        @Column (name="nombre", nullable =false, length=100)
            private String nombre;

        @NotNull(message = "El precio es obligatorio")
        @PositiveOrZero(message="El precio debe ser mayor a 0" )
        @Column (name="precio", nullable =false)
            private  double precio;

        @NotNull(message = "El stock es obligatorio")
        @PositiveOrZero(message="El stock no puede ser negativo" )
        @Column (name="stock", nullable =false)
            private  Integer stock;

        @Enumerated(EnumType.STRING)
        @NotNull(message = "La categoría es obligatoria ")
            // y debe ser una de las siguientes opciones:
            //  - BICICLETAS
            //  - REPUESTOS
            //  - ACCESORIOS
            //  - HERRAMIENTAS
            //  - INDUMENTARIA
            //  - VARIOS""")
        @Column(name="categoria", nullable =false) 
            private  Categoria categoria;  //Categoria es enum

        @Column(name="imagen")
            private String imagen;
            
        @ManyToOne //relacion muchos a uno
        @NotNull(message= "La marca del producto es obligatoria") //@NotBlank lanza error xq es Marca(no String)
        @JoinColumn(name="marca_id", nullable=false ) //cambio @Column por @JoinColumn 
            private Marca marca;
   
        public Producto( String codigo, String nombre, double precio, Integer stock, Categoria categoria,  String imagen, Marca marca){
            this.codigo= codigo;
            this.nombre= nombre;
            this.precio = precio;
            this.stock= stock;
            this.categoria= categoria;
            this.imagen= imagen;
            this.marca= marca;

         }

        
}