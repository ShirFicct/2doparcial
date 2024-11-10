package com.hospital.salud.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.optim.linear.UnboundedSolutionException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    private boolean activo;
    private boolean cuentaNoExpirada;
    private boolean cuentaNoBloqueada;
    private boolean credencialesNoExpiradas;

    // Relación con Persona
    @OneToOne
    @JoinColumn(name = "persona_ci", referencedColumnName = "ci")
    private Persona persona;

    // Relación con Rol
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();

    public Usuario( String correo, String password,Set<Rol> rol) {

        this.email = correo;
        this.password = password;
        this.roles = rol;
        this.activo = true;
        this.cuentaNoExpirada = true;
        this.cuentaNoBloqueada = true;
        this.credencialesNoExpiradas = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getNombre()))
                .collect(Collectors.toSet());

        roles.forEach(role -> {
            System.out.println("Rol: " + role.getNombre());

            if (role.getPermiso() != null) { // Verificación de null para evitar NPE
                role.getPermiso().forEach(permiso -> {
                    System.out.println("Permiso: " + permiso.getNombre());
                    authorities.add(new SimpleGrantedAuthority(permiso.getNombre()));
                });
            }
        });

        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return cuentaNoExpirada;
    }

    @Override
    public boolean isAccountNonLocked() {
        return cuentaNoBloqueada;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credencialesNoExpiradas;
    }

    @Override
    public boolean isEnabled() {
        return activo;
    }

    @Override
    public String getPassword() {
        return password;
    }

}
