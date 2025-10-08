package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.Usuario;
import edu.escuelaing.sirha.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    public UsuarioService usuarioService;

    @PostMapping("/Autentica un usuario en el sistema login")
    public Optional<Usuario> login(@RequestParam String username, @RequestParam String password) {
        return usuarioService.autenticar(username, password);
    }

    @PutMapping("/Cambia la contraseña de un usuario{id}/password")
    public void cambiarPassword(@PathVariable String id, @RequestParam String nuevoPassword) {
        usuarioService.cambiarPassword(id, nuevoPassword);
    }

    @GetMapping("/Busca un usuario por su nombre/{username}")
    public Optional<Usuario> buscarPorUsername(@PathVariable String username) {
        return usuarioService.buscarPorUsername(username);
    }

    @GetMapping("/ Verifica si un usuario tiene permisos{id}/permiso")
    public boolean tienePermiso(@PathVariable String id, @RequestParam String accion) {
        return usuarioService.tienePermiso(id, accion);
    }
}