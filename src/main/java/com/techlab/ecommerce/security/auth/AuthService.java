package com.techlab.ecommerce.security.auth;

import com.techlab.ecommerce.security.jwt.JwtService;
import com.techlab.ecommerce.security.user.Usuario;
import com.techlab.ecommerce.security.user.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow();

        String token = jwtService.generarToken(usuario);
        return AuthResponse.builder().token(token).build();
    }

      public AuthResponse register(RegisterRequest request) {
        Usuario usuario = Usuario.builder()
                .email(request.getUsername()) // <-- CORREGIDO: Cambiado de .username() a .email()
                .password(passwordEncoder.encode(request.getPassword()))
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .build();

        usuarioRepository.save(usuario);

        String token = jwtService.generarToken(usuario);
        return AuthResponse.builder().token(token).build();
    }
}

