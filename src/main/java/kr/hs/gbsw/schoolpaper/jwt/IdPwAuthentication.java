package kr.hs.gbsw.schoolpaper.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class IdPwAuthentication extends UsernamePasswordAuthenticationToken {

    public IdPwAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public IdPwAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
