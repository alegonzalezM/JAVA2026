package com.techlab.ecommerce.security.demo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DemoController {
    @GetMapping("/saludo")
    public ResponseEntity<String> saludo(){
        return ResponseEntity.ok("Acceso autorizado");
    }
    
}
