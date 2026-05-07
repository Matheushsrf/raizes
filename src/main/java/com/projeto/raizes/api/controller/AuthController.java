package com.projeto.raizes.api.controller;

import com.projeto.raizes.domain.model.Usuario;
import com.projeto.raizes.infrastructure.persistence.repository.UsuarioRepository;
import com.projeto.raizes.infrastructure.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository,
                          JwtService jwtService,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String senha = body.get("senha");

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "CREDENCIAIS_INVALIDAS",
                             "message", "Email ou senha inválidos."));
        }

        Usuario usuario = usuarioOpt.get();

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "CREDENCIAIS_INVALIDAS",
                             "message", "Email ou senha inválidos."));
        }

        String token = jwtService.gerarToken(usuario.getEmail(), usuario.getRole());

        return ResponseEntity.ok(Map.of(
            "accessToken", token,
            "tokenType", "Bearer",
            "nome", usuario.getNome(),
            "role", usuario.getRole()
        ));
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody Map<String, String> body) {
        String email = body.get("email");

        if (usuarioRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(409)
                .body(Map.of("error", "EMAIL_JA_CADASTRADO",
                             "message", "Este email já está em uso."));
        }

        Usuario usuario = new Usuario();
        usuario.setNome(body.get("nome"));
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(body.get("senha")));
        usuario.setRole(body.getOrDefault("role", "CLIENTE"));

        usuarioRepository.save(usuario);

        return ResponseEntity.status(201)
            .body(Map.of("message", "Usuário cadastrado com sucesso."));
    }
}