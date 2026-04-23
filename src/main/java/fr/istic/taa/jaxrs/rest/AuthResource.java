package fr.istic.taa.jaxrs.rest;

import fr.istic.taa.jaxrs.dao.UserDao;
import fr.istic.taa.jaxrs.domain.Admin;
import fr.istic.taa.jaxrs.domain.Customer;
import fr.istic.taa.jaxrs.domain.Organizer;
import fr.istic.taa.jaxrs.domain.User;
import fr.istic.taa.jaxrs.dto.response.AuthTokenResponseDto;
import fr.istic.taa.jaxrs.dto.user.CreateUserDto;
import fr.istic.taa.jaxrs.utils.JwtUtil;
import fr.istic.taa.jaxrs.utils.PasswordUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;
import java.util.Set;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Auth", description = "Authentication and registration endpoints")
public class AuthResource {

    private final UserDao userDao = new UserDao();

    public static class LoginRequest {
        public String mail;
        public String password;
    }

    @POST
    @Path("/login")
    @Operation(summary = "Login", description = "Authenticates a user and returns a JWT token", responses = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "400", description = "Missing credentials"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public Response login(LoginRequest req) {

        if (req == null || req.mail == null || req.password == null
                || req.mail.isBlank() || req.password.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "mail/password required"))
                    .build();
        }

        User user = userDao.findByEmail(req.mail);

        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("error", "invalid credentials"))
                    .build();
        }

        if (user.getPassword() == null || !PasswordUtil.verify(req.password, user.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("error", "invalid credentials"))
                    .build();
        }

        Set<String> roles = rolesFromUser(user);
        String token = JwtUtil.generateToken(user.getMail(), roles);

        return Response.ok(new AuthTokenResponseDto(token, roles, "authenticated")).build();
    }

    @POST
    @Path("/register")
    @Operation(summary = "Register", description = "Registers a new customer account and returns a JWT token", responses = {
            @ApiResponse(responseCode = "201", description = "Registration successful"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Email already used")
    })
    public Response register(CreateUserDto dto) {
        if (dto == null || dto.getEmail() == null || dto.getPassword() == null
                || dto.getFirstname() == null || dto.getLastname() == null || dto.getBirthdate() == null
                || dto.getEmail().isBlank() || dto.getPassword().isBlank()
                || dto.getFirstname().isBlank() || dto.getLastname().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "missing fields"))
                    .build();
        }

        User existing = userDao.findByEmail(dto.getEmail());
        if (existing != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", "email already used"))
                    .build();
        }

        String hashed = PasswordUtil.hash(dto.getPassword());

        Customer newUser = new Customer(
                dto.getLastname(),
                dto.getFirstname(),
                dto.getEmail(),
                hashed
        );

        userDao.save(newUser);

        Set<String> roles = rolesFromUser(newUser);
        String token = JwtUtil.generateToken(newUser.getMail(), roles);

        return Response.status(Response.Status.CREATED)
                .entity(new AuthTokenResponseDto(token, roles, "registered"))
                .build();
    }

    private Set<String> rolesFromUser(User u) {
        if (u instanceof Admin) return Set.of("ADMIN");
        if (u instanceof Organizer) return Set.of("ORGANIZER");
        if (u instanceof Customer) return Set.of("CUSTOMER");
        return Set.of("USER");
    }
}
