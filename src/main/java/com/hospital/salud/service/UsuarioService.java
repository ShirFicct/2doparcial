package com.hospital.salud.service;

import com.hospital.salud.config.JwtService;
import com.hospital.salud.dto.UsuarioDTO;
import com.hospital.salud.entity.Persona;
import com.hospital.salud.entity.Rol;
import com.hospital.salud.entity.Usuario;
import com.hospital.salud.repository.PersonaRepository;
import com.hospital.salud.repository.RolRepository;
import com.hospital.salud.repository.UsuarioRepository;
import com.hospital.salud.response.AuthResponse;
import com.hospital.salud.response.LoginRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private RolRepository rolRepository;
	@Autowired
	private PersonaRepository personaRepository;


	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;
	
	@Autowired 
	private ModelMapper modelMapper;
	
	@Autowired
	private RolService rolService;
	
	@Autowired
	private UsuarioDetailsService usuarioDetailsService;

	public List<UsuarioDTO> listUser() {
		List<Usuario> users = usuarioRepository.findAll();
		return users.stream()
				.map(user -> {
					UsuarioDTO dto = new UsuarioDTO();
					dto.setId(user.getId());
					dto.setNombre(user.getPersona().getNombre());
					dto.setApellido(user.getPersona().getApellido());
					dto.setEmail(user.getEmail());
					// Otros campos que sean necesarios en el DTO
					return dto;
				})
				.collect(Collectors.toList());
	}
	
	public List<Usuario> listUsuario() {
		return usuarioRepository.findAll();
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

	public AuthResponse createUser(UsuarioDTO userDto) {
	Rol rol = rolRepository.findById(userDto.getRolId())
			.orElseThrow(() -> new RuntimeException("Rol no encontrado"));

		// Crear una nueva Persona
		Persona persona = new Persona();
		persona.setCi(userDto.getId().toString()); // Usar un ID único para el CI
		persona.setNombre(userDto.getNombre());
		persona.setApellido(userDto.getApellido());
		persona.setEmail(userDto.getEmail());
		persona.setTelefono("");
		persona.setFechaNacimiento(userDto.getFechaNacimiento());
		persona.setSexo(userDto.getSexo());
		personaRepository.save(persona);

		// Crear el Usuario y asignar la Persona y Rol
		Usuario usuario = new Usuario();
		usuario.setEmail(userDto.getEmail());
		usuario.setPassword(passwordEncoder.encode(userDto.getPassword()));
		usuario.setPersona(persona);
		usuario.setRoles(Set.of(rol));
		usuarioRepository.save(usuario);

		// Generar un token JWT para el usuario registrado
		String token = jwtService.getToken(usuario);
		return AuthResponse.builder().token(token).build();
	}

	public Usuario registrarUser(UsuarioDTO userDto) {
		// Obtener el rol usando el servicio de roles
		Rol rol = rolService.obtenerRol(userDto.getRolId());
		Set<Rol> rolesSet = new HashSet<>();
		rolesSet.add(rol);

		// Crear la entidad Persona y establecer los datos personales
		Persona persona = new Persona();
		persona.setCi(userDto.getId().toString());  // O generar un CI único si es necesario
		persona.setNombre(userDto.getNombre());
		persona.setApellido(userDto.getApellido());
		persona.setEmail(userDto.getEmail());
		persona.setFechaNacimiento(userDto.getFechaNacimiento());
		persona.setSexo(userDto.getSexo());

		// Guardar la entidad Persona en la base de datos
		personaRepository.save(persona);

		// Crear el Usuario y asignarle la Persona y el conjunto de roles
		Usuario usuario = new Usuario();
		usuario.setEmail(userDto.getEmail());
		usuario.setPassword(passwordEncoder.encode(userDto.getPassword()));
		usuario.setPersona(persona); // Asociar la persona recién creada al usuario
		usuario.setRoles(rolesSet);

		usuario.setCredencialesNoExpiradas(true);
		usuario.setCuentaNoBloqueada(true);
		usuario.setCuentaNoExpirada(true);
		usuario.setActivo(true);
		// Guardar el usuario en la base de datos
		return usuarioRepository.save(usuario);
	}


	public AuthResponse createUserAdmin(UsuarioDTO userDto) {
		// Buscar o crear el rol "ADMIN"
		Rol adminRole = rolRepository.findByNombre("ADMIN")
				.orElseGet(() -> rolRepository.save(new Rol("ADMIN")));

		// Crear la entidad Persona
		Persona persona = new Persona();
		persona.setCi(userDto.getId().toString());
		persona.setNombre(userDto.getNombre());
		persona.setApellido(userDto.getApellido());
		persona.setEmail(userDto.getEmail());
		persona.setFechaNacimiento(userDto.getFechaNacimiento());
		persona.setSexo(userDto.getSexo());
		personaRepository.save(persona);

		// Crear el Usuario y asignarle el rol ADMIN
		Usuario usuario = new Usuario();
		usuario.setEmail(userDto.getEmail());
		usuario.setPassword(passwordEncoder.encode(userDto.getPassword()));
		usuario.setPersona(persona);
		usuario.setRoles(Set.of(adminRole));
		usuarioRepository.save(usuario);
		usuario.setCredencialesNoExpiradas(true);
		usuario.setCuentaNoBloqueada(true);
		usuario.setCuentaNoExpirada(true);
		usuario.setActivo(true);

		// Generar el token JWT para el nuevo administrador
		String token = jwtService.getToken(usuario);
		return AuthResponse.builder().token(token).build();
	}


	public Usuario getUser(String email) {
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		return usuario;
	}


	public AuthResponse login(LoginRequest loginRequest) {
		// Autenticar el usuario
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getUsername(),
						loginRequest.getPassword()
				)
		);
		// Buscar el usuario en la base de datos
		Usuario usuario = usuarioRepository.findByEmail(loginRequest.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("Usuario o contraseña inválidos"));

		// Generar y retornar el token JWT
		String token = jwtService.getToken(usuario);
		return AuthResponse.builder()
				.token(token)
				.email(usuario.getEmail())
				.nombre(usuario.getPersona().getNombre())
				.apellido(usuario.getPersona().getApellido())
				.id(usuario.getId())
				.build();
	}

	public AuthResponse loader(Authentication authentication) {
		// Obtener los detalles del usuario autenticado
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		// Buscar el usuario completo en la base de datos usando su email (nombre de usuario)
		Usuario user = usuarioDetailsService.getUser(userDetails.getUsername());

		// Obtener la Persona asociada para acceder a los datos personales
		Persona persona = user.getPersona();

		// Construir y retornar la respuesta de autenticación con los detalles del usuario
		return AuthResponse.builder()
				.email(user.getEmail())
				.role(user.getRoles().iterator().next()) // Obtener el primer rol del conjunto (ajusta si hay múltiples roles)
				.nombre(persona.getNombre())
				.apellido(persona.getApellido())
				.id(user.getId())
				.build();
	}


	public Usuario updateUser(Long id, UsuarioDTO userDto) {
		// Obtener el usuario existente
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

		// Actualizar los datos de la Persona relacionada
		Persona persona = usuario.getPersona();
		persona.setNombre(userDto.getNombre());
		persona.setApellido(userDto.getApellido());
		persona.setFechaNacimiento(userDto.getFechaNacimiento());
		persona.setSexo(userDto.getSexo());
		personaRepository.save(persona);

		// Actualizar el Usuario
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
