package ca.usherbrooke.remisetravaux.service;

import ca.usherbrooke.remisetravaux.business.userinfo.SessionAndRole;
import ca.usherbrooke.remisetravaux.business.session.SessionClass;
import ca.usherbrooke.remisetravaux.dto.Sessions;
import ca.usherbrooke.remisetravaux.persistence.SessionMapper;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
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
    @Path("/sessions")
    @RolesAllowed({"etudiant","enseignant"})
    public Sessions getSessions(){
        String cip = this.securityContext.getUserPrincipal().getName();
        List<SessionAndRole> SessionAndRoles = sessionMapper.getAllUserSessions(cip);

        //Put back in a readable object
        Sessions sessionUser = new Sessions();
        for (var sessionAndRole:
             SessionAndRoles) {
            if (sessionAndRole.rolename.equals("Etudiant")){
                sessionUser.Etudiant.add(sessionAndRole.sessionnom);
            }else if(sessionAndRole.rolename.equals("Enseignant")){
                sessionUser.Enseignant.add(sessionAndRole.sessionnom);
            }
        }
        return sessionUser;
    }

    @GET
    @Path("/classes/{sessionId}/{roleId}")
    @RolesAllowed({"etudiant","enseignant"})
    public List<SessionClass> getAllStudentClasses(
            @PathParam("sessionId") int sessionId,
            @PathParam("roleId") int roleId
    ){
        String cip = this.securityContext.getUserPrincipal().getName();
        List<SessionClass> classes =  sessionMapper.getAllUserClass(cip,sessionId,roleId);
        return classes;
    }
}