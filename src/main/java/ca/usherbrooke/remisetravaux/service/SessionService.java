package ca.usherbrooke.remisetravaux.service;

import ca.usherbrooke.remisetravaux.business.userinfo.SessionAndRole;
import ca.usherbrooke.remisetravaux.business.session.SessionClass;
import ca.usherbrooke.remisetravaux.dto.Session;
import ca.usherbrooke.remisetravaux.dto.Sessions;
import ca.usherbrooke.remisetravaux.dto.Assignment;
import ca.usherbrooke.remisetravaux.persistence.SessionMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
    @PermitAll
    public Sessions getSessions(){
        String cip = this.securityContext.getUserPrincipal().getName();
        List<SessionAndRole> SessionAndRoles = sessionMapper.getAllUserSessions(cip);

        //Put back in a readable object
        Sessions sessionUser = new Sessions();
        for (var sessionAndRole:
             SessionAndRoles) {
            if (sessionAndRole.rolename.equals("Etudiant")){

                sessionUser.Etudiant.add(new Session(sessionAndRole.sessionnom, sessionAndRole.id_session));
            }else if(sessionAndRole.rolename.equals("Enseignant")){
                sessionUser.Enseignant.add(new Session(sessionAndRole.sessionnom, sessionAndRole.id_session));
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
    
    @GET
    @Path("/sessions/{SessionID}/{AssignmentID}")
    //@RolesAllowed({"etudiant","enseignant"})
    public Assignment getAssignment(@PathParam("SessionID") String SessionID, @PathParam("AssignmentID") String AssignmentID){
    //    String cip = this.securityContext.getUserPrincipal().getName();
        String cip;
        cip = "bils2704";
        Assignment assignment = sessionMapper.getAssignment(cip, AssignmentID, SessionID);
        return assignment;
    }
}
