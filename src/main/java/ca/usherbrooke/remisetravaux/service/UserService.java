package ca.usherbrooke.remisetravaux.service;

import ca.usherbrooke.remisetravaux.dto.User;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserService {

    @Context
    SecurityContext securityContext;
    @Inject
    JsonWebToken jwt;

    @GET
    @Path("information")
    @RolesAllowed({"etudiant","enseignant"})
    @Context
    public User getUserName() {
        User user = new User();
        user.cip = securityContext.getUserPrincipal().getName();
        user.lastName = this.jwt.getClaim("family_name");
        user.firstName = this.jwt.getClaim("given_name");
        return user;
    }
}
