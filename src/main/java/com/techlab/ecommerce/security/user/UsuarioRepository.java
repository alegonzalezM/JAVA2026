// package com.techlab.ecommerce.security.user;

// import java.util.Optional;
// import org.springframework.data.jpa.repository.JpaRepository;

// public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
//     Optional<Usuario> findByUsername(String username);
//}
package com.techlab.ecommerce.security.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    Optional<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE u.email = :username")
    Optional<Usuario> findByUsername(@Param("username") String username);
}