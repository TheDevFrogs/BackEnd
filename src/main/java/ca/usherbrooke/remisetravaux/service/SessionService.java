package ca.usherbrooke.remisetravaux.service;

import ca.usherbrooke.remisetravaux.persistence.SessionMapper;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.security.PublicKey;
import java.util.List;

@Path("/session")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SessionService {
    @Inject
    SessionMapper sessionMapper;

    //Permet de faire des requete SQL voir les requetes dans le fichiers SessionMapper.xml dans ressources
    @Context
    SecurityContext securityContext;

    @GET
    @Path("{sessionDescription}")
    //@RolesAllowed({"etudiant","enseignant"})
    public List<Class> getClasses(
            @PathParam("sessionDescription") String sessionDescription
    ){
        String cip = "dadw";
        List<Class> classes = sessionMapper.getUserSessionClasses(cip,sessionDescription);
        //String cip = this.securityContext.getUserPrincipal().getName();
        return classes;
    }
}
