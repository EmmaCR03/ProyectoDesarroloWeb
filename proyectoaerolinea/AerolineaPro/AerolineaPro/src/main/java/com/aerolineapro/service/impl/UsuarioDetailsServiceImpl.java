
package com.aerolineapro.service.impl;

import com.tienda_k.service.UsuarioDetailsService;
import com.tienda_k.dao.UsuarioDao;
import com.tienda_k.domain.Usuario;
import com.tienda_k.domain.Rol;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
public class UsuarioDetailsServiceImpl implements UsuarioDetailsService, UserDetailsService {
    @Autowired
    private UsuarioDao usuarioDao;
    
    @Autowired
    private HttpSession session;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Busca el usuario por el username en la tabla
        Usuario usuario = usuarioDao.findByUsername(username);
        //Si no existe el usuario lanza una excepción
        if (usuario == null) {
            throw new UsernameNotFoundException(username);
        }
        
        
        session.removeAttribute("rutaImagen");
        session.setAttribute("rutaImagen", usuario.getRutaImagen());
        
        
        //Si está acá es porque existe el usuario... sacamos los roles que tiene
        var roles = new ArrayList<GrantedAuthority>();
        for (Rol r : usuario.getRoles()) {   //Se sacan los roles
            roles.add(new SimpleGrantedAuthority(r.getNombre()));
        }
        
        //Se devuelve User (clase de userDetails)
        return new User(usuario.getUsername(), usuario.getPassword(), roles);
    }
}