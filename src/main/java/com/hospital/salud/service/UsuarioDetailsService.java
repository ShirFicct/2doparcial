package com.hospital.salud.service;

import com.hospital.salud.entity.Rol;
import com.hospital.salud.entity.Usuario;
import com.hospital.salud.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UsuarioDetailsService implements UserDetailsService{

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmail(username)
	            .orElseThrow(() -> new UsernameNotFoundException("Usuario o password inválidos"));
	        return new User(usuario.getEmail(), usuario.getPassword(), mapearAutoridadesRoles(usuario.getRoles()));
	}
	
	private Collection<? extends GrantedAuthority> mapearAutoridadesRoles(Collection<Rol> roles) {
	    return roles.stream()
	        .flatMap(role -> {
	            Set<GrantedAuthority> roleAuthorities = new HashSet<>();
	            roleAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getNombre()));
	            
	            Set<GrantedAuthority> permissionAuthorities = role.getPermiso().stream()
	                .map(permiso -> new SimpleGrantedAuthority(permiso.getNombre()))
	                .collect(Collectors.toSet());
	            
	            return Stream.concat(roleAuthorities.stream(), permissionAuthorities.stream());
	        })
	        .collect(Collectors.toSet());
	}

    public Usuario getUser(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
	
}
