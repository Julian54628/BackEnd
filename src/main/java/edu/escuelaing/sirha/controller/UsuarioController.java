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

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        try {
            // MongoDB generará el ID automáticamente
            Usuario usuarioCreado = usuarioService.crearUsuario(usuario);
            return ResponseEntity.ok(usuarioCreado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestParam String username, @RequestParam String password) {
        try {
            Optional<Usuario> usuarioOpt = usuarioService.autenticar(username, password);
            if (usuarioOpt.isPresent()) {
                return ResponseEntity.ok(usuarioOpt.get());
            } else {
                return ResponseEntity.status(401).build(); // No autorizado
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> cambiarPassword(@PathVariable String id, @RequestParam String nuevoPassword) {
        try {
            usuarioService.cambiarPassword(id, nuevoPassword);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Usuario> buscarPorUsername(@PathVariable String username) {
        try {
            Optional<Usuario> usuario = usuarioService.buscarPorUsername(username);
            return usuario.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable String id) {
        try {
            Optional<Usuario> usuario = usuarioService.buscarPorId(id);
            return usuario.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}/permiso")
    public ResponseEntity<Boolean> tienePermiso(@PathVariable String id, @RequestParam String accion) {
        try {
            boolean tienePermiso = usuarioService.tienePermiso(id, accion);
            return ResponseEntity.ok(tienePermiso);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}