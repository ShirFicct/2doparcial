package com.hospital.salud.controller;

import com.hospital.salud.dto.UsuarioDTO;
import com.hospital.salud.entity.Persona;
import com.hospital.salud.entity.Usuario;
import com.hospital.salud.response.ApiResponse;
import com.hospital.salud.service.UsuarioService;
import com.hospital.salud.util.HttpStatusMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UsuarioController {
	
	@Autowired
    private UsuarioService service;

	@GetMapping
//	@PreAuthorize("hasAuthority('ADMINISTRAR_PERSONAL')")
	public ResponseEntity<ApiResponse<List<Usuario>>> listarUsuarios(@RequestParam(value = "search", required = false) String searchTerm) {
		List<Usuario> user;
		if (searchTerm != null && !searchTerm.isEmpty()) {
			user = service.buscarUsuarios(searchTerm);
        } else {
        	user = service.listUsuario();
        }
		return new ResponseEntity<>(
				ApiResponse.<List<Usuario>>builder()
						.statusCode(HttpStatus.OK.value())
						.message(HttpStatusMessage.getMessage(HttpStatus.OK))
						.data(user)
						.build(),
				HttpStatus.OK
		);
	}
	
	@GetMapping("/persona")
	public ResponseEntity<ApiResponse<List<Persona>>> listarUsuariosPersonas(@RequestParam(value = "search", required = false) String searchTerm) {
		List<Persona> user;
		if (searchTerm != null && !searchTerm.isEmpty()) {
//			user = service.buscarUsuarios(searchTerm);
			user = service.listUsuarioPersona();
        } else {
        	user = service.listUsuarioPersona();
        }
		return new ResponseEntity<>(
				ApiResponse.<List<Persona>>builder()
						.statusCode(HttpStatus.OK.value())
						.message(HttpStatusMessage.getMessage(HttpStatus.OK))
						.data(user)
						.build(),
				HttpStatus.OK
		);
	}
	
    @GetMapping("/protected")
    public ResponseEntity<String> checkAuthorities() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getAuthorities()); 
        
        return ResponseEntity.ok("Acceso concedido");
    }

    
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')") 
	public ResponseEntity<ApiResponse<Usuario>> obtenerUsuario(@PathVariable Long id) {
		try {
			Usuario usuariosOpt = service.obtenerUserPorId(id);
			return new ResponseEntity<>(
					ApiResponse.<Usuario>builder()
							.statusCode(HttpStatus.OK.value())
							.message(HttpStatusMessage.getMessage(HttpStatus.OK))
							.data(usuariosOpt)
							.build(),
					HttpStatus.OK
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Usuario>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}
    

	@PatchMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ApiResponse<Usuario>>actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO userDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage)
					.collect(Collectors.toList());
			return new ResponseEntity<>(
					ApiResponse.<Usuario>builder()
							.errors(errors)
							.build(),
					HttpStatus.BAD_REQUEST
			);
		}
		try {
			Usuario usuarioActualizado = service.updateUser(id, userDTO);
			return new ResponseEntity<>(
					ApiResponse.<Usuario>builder()
							.statusCode(HttpStatus.OK.value())
							.message(HttpStatusMessage.getMessage(HttpStatus.OK))
							.data(usuarioActualizado)
							.build(),
					HttpStatus.OK
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Usuario>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMINISTRAR_PERSONAL')")
	public ResponseEntity<ApiResponse<Void>> eliminarUsuario(@PathVariable Long id) {
		try {
			service.deleteUser(id);
			return new ResponseEntity<>(
					ApiResponse.<Void>builder()
							.statusCode(HttpStatus.NO_CONTENT.value())
							.message(HttpStatusMessage.getMessage(HttpStatus.NO_CONTENT))
							.build(),
					HttpStatus.NO_CONTENT
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Void>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}
	
	@PostMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMINISTRAR_PERSONAL')")
	public ResponseEntity<ApiResponse<Void>> activarUsuario(@PathVariable Long id) {
		try {
			service.activeUser(id);
			return new ResponseEntity<>(
					ApiResponse.<Void>builder()
							.statusCode(HttpStatus.OK.value())
							.message(HttpStatusMessage.getMessage(HttpStatus.OK))
							.build(),
					HttpStatus.OK
			);
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(
					ApiResponse.<Void>builder()
							.statusCode(e.getStatusCode().value())
							.message(e.getReason())
							.build(),
					e.getStatusCode()
			);
		}
	}
	
	
}
