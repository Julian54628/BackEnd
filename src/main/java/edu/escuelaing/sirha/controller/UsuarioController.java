package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.Usuario;
import edu.escuelaing.sirha.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public Optional<Usuario> login(@RequestParam String username, @RequestParam String password) {
        return usuarioService.autenticar(username, password);
    }

    @PutMapping("/{id}/password")
    public void changePassword(@PathVariable String id, @RequestParam String nuevoPassword) {
        usuarioService.cambiarPassword(id, nuevoPassword);
    }
}