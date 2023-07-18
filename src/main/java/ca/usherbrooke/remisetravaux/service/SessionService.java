package ca.usherbrooke.remisetravaux.service;

import ca.usherbrooke.remisetravaux.business.session.TeacherSessionClass;
import ca.usherbrooke.remisetravaux.business.userinfo.SessionAndRole;
import ca.usherbrooke.remisetravaux.business.session.StudentSessionClass;
import ca.usherbrooke.remisetravaux.dto.Session;
import ca.usherbrooke.remisetravaux.dto.Sessions;
import ca.usherbrooke.remisetravaux.persistence.SessionMapper;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;

@Path("/session")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SessionService {
    @Inject
    SessionMapper sessionMapper;

    @Context
    SecurityContext securityContext;

    @GET
    @Path("/sessions")
    @RolesAllowed({"etudiant", "enseignant"})
    public Sessions getSessions() {
        String cip = this.securityContext.getUserPrincipal().getName();

        List<SessionAndRole> SessionAndRoles = sessionMapper.getAllUserSessions(cip);

        //Put back in a readable object
        Sessions sessionUser = new Sessions();
        for (var sessionAndRole :
                SessionAndRoles) {
            if (sessionAndRole.rolename.equals("Etudiant")) {

                sessionUser.Etudiant.add(new Session(sessionAndRole.sessionnom, sessionAndRole.id_session));
            } else if (sessionAndRole.rolename.equals("Enseignant")) {
                sessionUser.Enseignant.add(new Session(sessionAndRole.sessionnom, sessionAndRole.id_session));
            }
        }
        return sessionUser;
    }

    @GET
    @Path("/classes/student/sessionId={sessionId}")
    @RolesAllowed({"etudiant", "enseignant"})
    public List<StudentSessionClass> getAllStudentClasses(
            @PathParam("sessionId") int sessionId
    ) {
        String cip = this.securityContext.getUserPrincipal().getName();
        List<StudentSessionClass> classes = sessionMapper.getAllStudentClasses(cip, sessionId);
        return classes;
    }

    @GET
    @Path("/classes/teacher/sessionId={sessionId}")
    @RolesAllowed({"etudiant", "enseignant"})
    public List<TeacherSessionClass> getAllTeacherClasses(
            @PathParam("sessionId") int sessionId
    ) {
        String cip = this.securityContext.getUserPrincipal().getName();
        List<TeacherSessionClass> classes = sessionMapper.getAllTeacherClasses(cip, sessionId);
        return classes;
    }
}
