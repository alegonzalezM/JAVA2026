package com.techlab.ecommerce.security.jwt;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.techlab.ecommerce.security.user.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    // Constructor manual único (se removió @RequiredArgsConstructor para evitar conflictos)
    public JwtFilter(JwtService jwtService, UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // 1. Validar excepciones públicas (Pasan de largo sin validar JWT)
        if (requestURI.equals("/") || 
            requestURI.endsWith(".html") || 
            requestURI.startsWith("/app.js") || 
            requestURI.startsWith("/favicon.ico") || 
            requestURI.startsWith("/imagenes/") ||
            requestURI.contains("/productos") ||   
            requestURI.contains("/carritos") ||
            requestURI.contains("/clientes") ||
            requestURI.contains("/marcas")) { 
            
            filterChain.doFilter(request, response);
            return; // Termina la ejecución de este método para rutas públicas
        }

        // 2. Si es una ruta protegida, extrae y valida el Token JWT
        final String token = extraerTokenDelRequest(request);
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        final String username = jwtService.extraerUsername(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails usuario = usuarioRepository.findByUsername(username)
                    .orElseThrow();
                    
            if (jwtService.esTokenValido(token, usuario)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                usuario, null, usuario.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extraerTokenDelRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
