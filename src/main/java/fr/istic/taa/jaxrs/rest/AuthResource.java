package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.UserDao;
import fr.istic.taa.jaxrs.domain.Admin;
import fr.istic.taa.jaxrs.domain.Customer;
import fr.istic.taa.jaxrs.domain.Organizer;
import fr.istic.taa.jaxrs.domain.User;
import fr.istic.taa.jaxrs.dto.user.CreateUserDto;
import fr.istic.taa.jaxrs.utils.JwtUtil;
import fr.istic.taa.jaxrs.utils.PasswordUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;
import java.util.Set;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final UserDao userDao = new UserDao();

    /**
     * DTO minimal pour le login 
     * Le client envoie : { "mail": "...", "password": "..." }
     */
    public static class LoginRequest {
        public String mail;
        public String password;
    }

    /**
     * LOGIN : vérifie les identifiants et renvoie un JWT
     */
    @POST
    @Path("/login")
    public Response login(LoginRequest req) {

        // Vérification basique des champs
        if (req == null || req.mail == null || req.password == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "mail/password required"))
                    .build();
        }

        // Recherche de l'utilisateur (selon ton DAO)
        User user = userDao.findByEmail(req.mail);

        // Si utilisateur introuvable => 401
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("error", "invalid credentials"))
                    .build();
        }
        
        if (!PasswordUtil.verify(req.password, user.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        // Déduit le rôle du type réel (Admin/Organizer/Customer)
        Set<String> roles = rolesFromUser(user);

        // Génère le token JWT
        String token = JwtUtil.generateToken(user.getMail(), roles);

        return Response.ok(Map.of("token", token, "roles", roles)).build();
    }

    /**
     * REGISTER : crée un nouvel utilisateur en base.
     * Ici on choisit de créer un CUSTOMER par défaut (inscription publique).
     */
    @POST
    @Path("/register")
    public Response register(CreateUserDto dto) {
         String hashed = PasswordUtil.hash(dto.getPassword());
        // Vérification basique des champs
        if (dto == null || dto.getEmail() == null || dto.getPassword() == null
                || dto.getFirstname() == null || dto.getLastname() == null || dto.getBirthdate() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "missing fields"))
                    .build();
        }

        // Vérifie si l'email existe déjà (à implémenter si pas encore)
        User existing = userDao.findByEmail(dto.getEmail());
        if (existing != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", "email already used"))
                    .build();
        }

        // On crée un utilisateur de type CUSTOMER (public)
        Customer newUser = new Customer(
                dto.getLastname(),
                dto.getFirstname(),
                dto.getEmail(),
                hashed
        );

        // Sauvegarde en base
        userDao.save(newUser);

        // Option : connecter directement après inscription
        Set<String> roles = rolesFromUser(newUser);
        String token = JwtUtil.generateToken(newUser.getMail(), roles);

        return Response.status(Response.Status.CREATED)
                .entity(Map.of("message", "registered", "token", token, "roles", roles))
                .build();
    }

    /**
     * Déduit les rôles à partir du type réel de l'entité User
     */
    private Set<String> rolesFromUser(User u) {
        if (u instanceof Admin) return Set.of("ADMIN");
        if (u instanceof Organizer) return Set.of("ORGANIZER");
        if (u instanceof Customer) return Set.of("CUSTOMER");
        return Set.of("USER");
    }
}