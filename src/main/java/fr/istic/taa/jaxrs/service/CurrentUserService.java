package fr.istic.taa.jaxrs.service;

import fr.istic.taa.jaxrs.dao.UserDao;
import fr.istic.taa.jaxrs.domain.User;
import fr.istic.taa.jaxrs.utils.JwtUtil;
import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;
import java.util.Set;

public class CurrentUserService {

    private final UserDao userDao = new UserDao();

    public User getCurrentUser(SecurityContext securityContext) {
        JwtUtil.TokenPayload payload = getTokenPayload(securityContext);
        if (payload == null) {
            return null;
        }
        return userDao.findByEmail(payload.username());
    }

    public String getCurrentUserRole(SecurityContext securityContext) {
        JwtUtil.TokenPayload payload = getTokenPayload(securityContext);
        if (payload == null) {
            return null;
        }
        return payload.roles().stream().findFirst().orElse("USER");
    }

    public Set<String> getCurrentUserRoles(SecurityContext securityContext) {
        JwtUtil.TokenPayload payload = getTokenPayload(securityContext);
        return payload == null ? Set.of() : payload.roles();
    }

    private JwtUtil.TokenPayload getTokenPayload(SecurityContext securityContext) {
        if (securityContext == null) {
            return null;
        }
        Principal principal = securityContext.getUserPrincipal();
        if (!(principal instanceof JwtUtil.TokenPayload)) {
            return null;
        }
        return (JwtUtil.TokenPayload) principal;
    }
}

