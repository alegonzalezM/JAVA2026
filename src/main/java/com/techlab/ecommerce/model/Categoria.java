package com.techlab.ecommerce.model;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum Categoria {
    BICICLETAS,
    REPUESTOS,
    ACCESORIOS,
    HERRAMIENTAS,
    INDUMENTARIA,
    VARIOS;

     @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Categoria convierteTexto(String valor) {   
        
        for (Categoria c : Categoria.values()) {
            if (c.name().equalsIgnoreCase(valor)) {
                return c;
            }
        }
        return null; // <-- Si está mal escrita, devuelve null 
    }
     public String toValue() {
        return name().toLowerCase(); // Esto hace que Spring acepte minúsculas en la URL de forma nativa
    }
}

    



