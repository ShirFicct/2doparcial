package com.hospital.salud.service;

import com.hospital.salud.config.JwtService;
import com.hospital.salud.dto.UsuarioDTO;
import com.hospital.salud.entity.Doctor;
import com.hospital.salud.entity.Persona;
import com.hospital.salud.entity.Rol;
import com.hospital.salud.entity.Usuario;
import com.hospital.salud.repository.DoctorRepository;
import com.hospital.salud.repository.PersonaRepository;
import com.hospital.salud.repository.RolRepository;
import com.hospital.salud.repository.UsuarioRepository;
import com.hospital.salud.response.AuthResponse;
import com.hospital.salud.response.LoginRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private RolRepository rolRepository;
	
	@Autowired
	private PersonaRepository personaRepository;
	
	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private EspecialidadService especialidadService;
	
	@Autowired
	private UsuarioDetailsService usuarioDetailsService;
	
	public List<Usuario> listUsuario() {
		return usuarioRepository.findAll();
	}
	
	public List<Persona> listUsuarioPersona() {
		return personaRepository.findAll();
	}

	public Long getUsuariorById(String name) {
		Usuario usuario = usuarioRepository.findByEmail(name)
	            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
	        return usuario.getId();
	}
	
	public Usuario obtenerUserPorId(Long id) {
		Optional<Usuario> user = usuarioRepository.findById(id);
		if (user.isPresent()) {
			return user.get();
		}else {
			throw new UsernameNotFoundException("El usuario no se encuentra");
		}
	}

	public AuthResponse createUserAdmin(UsuarioDTO userDTO) {
		Rol adminRole = rolRepository.findByNombre("ADMIN")
				.orElseGet(() -> rolRepository.save(new Rol("ADMIN")));
		
		Persona persona = new Persona();
		persona.setCi(userDTO.getCi());
		persona.setNombre(userDTO.getNombre());
		persona.setApellido(userDTO.getApellido());
		persona.setFechaNacimiento(userDTO.getFechaNacimiento());
		persona.setSexo(userDTO.getSexo());
		personaRepository.save(persona);

		Usuario usuario = new Usuario();
		usuario.setEmail(userDTO.getEmail());
		usuario.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		usuario.setPersona(persona);
		usuario.setRoles(Set.of(adminRole));
		usuario.setCredencialesNoExpiradas(true);
		usuario.setCuentaNoBloqueada(true);
		usuario.setCuentaNoExpirada(true);
		usuario.setActivo(true);
		usuarioRepository.save(usuario);

		String token = jwtService.getToken(usuario);
		return AuthResponse.builder().token(token).build();
		
	}
	
	public AuthResponse createUserDoctor(UsuarioDTO userDTO) {
		Rol adminRole = rolRepository.findByNombre("DOCTOR")
				.orElseGet(() -> rolRepository.save(new Rol("DOCTOR")));
		
		Persona persona = new Persona();
		persona.setCi(userDTO.getCi());
		persona.setNombre(userDTO.getNombre());
		persona.setApellido(userDTO.getApellido());
		persona.setFechaNacimiento(userDTO.getFechaNacimiento());
		persona.setSexo(userDTO.getSexo());
		personaRepository.save(persona);
		
		Doctor doctor = new Doctor();
		doctor.setEspecialidad(especialidadService.obtenerEspecialidad(userDTO.getEspecialidad_id()));
		doctor.setPersona(persona);
		doctorRepository.save(doctor);

		Usuario usuario = new Usuario();
		usuario.setEmail(userDTO.getEmail());
		usuario.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		usuario.setPersona(persona);
		usuario.setRoles(Set.of(adminRole));
		usuario.setCredencialesNoExpiradas(true);
		usuario.setCuentaNoBloqueada(true);
		usuario.setCuentaNoExpirada(true);
		usuario.setActivo(true);
		usuarioRepository.save(usuario);

		String token = jwtService.getToken(usuario);
		return AuthResponse.builder().token(token).build();
	}


	public Usuario getUser(String email) {
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		return usuario;
	}


	public AuthResponse login(LoginRequest loginRequest) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getUsername(),
						loginRequest.getPassword()
				)
		);
		
		Usuario usuario = usuarioRepository.findByEmail(loginRequest.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("Usuario o contraseña inválidos"));

		String token = jwtService.getToken(usuario);
		
		return AuthResponse.builder()
				.token(token)
				.email(usuario.getEmail())
				.role(usuario.getRoles().iterator().next())
				.nombre(usuario.getPersona().getNombre())
				.apellido(usuario.getPersona().getApellido())
				.id(usuario.getId())
				.build();
	}

	public AuthResponse loader(Authentication authentication) {
		
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Usuario user = usuarioDetailsService.getUser(userDetails.getUsername());
		Persona persona = user.getPersona();
		
		String token = jwtService.getToken(userDetails);
		
		return AuthResponse.builder()
				.token(token)
				.email(user.getEmail())
				.role(user.getRoles().iterator().next()) 
				.nombre(persona.getNombre())
				.apellido(persona.getApellido())
				.id(user.getId())
				.build();
	}


	public Usuario updateUser(Long id, UsuarioDTO userDto) {
		
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

		Persona persona = usuario.getPersona();
		persona.setNombre(userDto.getNombre());
		persona.setApellido(userDto.getApellido());
		persona.setFechaNacimiento(userDto.getFechaNacimiento());
		persona.setSexo(userDto.getSexo());
		personaRepository.save(persona);

		usuario.setEmail(userDto.getEmail());
		usuario.setPassword(passwordEncoder.encode(userDto.getPassword()));
		return usuarioRepository.save(usuario);
	}

	public Usuario getUserByEmail(String email) {
		return usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
	}

	@Transactional
	public void deleteUser(Long id) {
		Usuario user = obtenerUserPorId(id);
		user.setActivo(false);
		user.setCredencialesNoExpiradas(false);
		user.setCuentaNoBloqueada(false);
		user.setCuentaNoBloqueada(false);
		usuarioRepository.save(user);
	}
	
	@Transactional
	public void activeUser(Long id) {
		Usuario user = obtenerUserPorId(id);
		user.setActivo(true);
		user.setCredencialesNoExpiradas(true);
		user.setCuentaNoBloqueada(true);
		user.setCuentaNoBloqueada(true);
		usuarioRepository.save(user);
	}
	
	public List<Usuario> buscarUsuarios(String searchTerm) {
        return usuarioRepository.findByNombreOrApellidoOrEmail(searchTerm);
    }
	
}
