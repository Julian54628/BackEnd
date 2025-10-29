package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.Usuario;
import edu.escuelaing.sirha.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestParam String username, @RequestParam String password) {
        return usuarioService.autenticar(username, password)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build());
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> cambiarPassword(@PathVariable String id, @RequestParam String nuevoPassword) {
        try {
            usuarioService.cambiarPassword(id, nuevoPassword);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Usuario> buscarPorUsername(@PathVariable String username) {
        return usuarioService.buscarPorUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable String id) {
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/permiso")
    public ResponseEntity<Boolean> tienePermiso(@PathVariable String id, @RequestParam String accion) {
        boolean tienePermiso = usuarioService.tienePermiso(id, accion);
        return ResponseEntity.ok(tienePermiso);
    }

}