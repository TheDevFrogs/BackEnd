package ca.usherbrooke.remisetravaux.service;

import ca.usherbrooke.remisetravaux.business.DatabaseFile;
import ca.usherbrooke.remisetravaux.files.FileDataAccess;
import ca.usherbrooke.remisetravaux.files.LocalFileWriter;
import ca.usherbrooke.remisetravaux.persistence.FileMapper;
import ca.usherbrooke.remisetravaux.persistence.HandedAssignmentMapper;
import ca.usherbrooke.remisetravaux.persistence.AssignmentMapper;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;
import java.security.PublicKey;

@Path("/file")
public class FileService {

    @Context
    SecurityContext securityContext;

    @Inject
    FileMapper fileMapper;

    @Inject
    HandedAssignmentMapper handedAssignmentMapper;

    @Inject
    AssignmentMapper assignmentMapper;

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @RolesAllowed({"etudiant", "enseignant"})
    @Path("/download/handedassignmentfile/fileId={fileId}")
    public Response getHandedAssignmentFile(@PathParam("fileId") int file_id) {
        String cip = this.securityContext.getUserPrincipal().getName();

        // Verifier que l'etudiant fait partie du groupe dans lequel l'assignment est
        if(!handedAssignmentMapper.canDownloadHandedAssignmentFile(cip))
            throw new WebApplicationException("You may not download this file", 401);

        DatabaseFile databaseFile = fileMapper.getFile(file_id);

        FileDataAccess fileDataAccess = new LocalFileWriter();
        try {
            return Response.ok(fileDataAccess.ReadFile(databaseFile), MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + databaseFile.displayed_name + databaseFile.extension + "\"") //optional
                    .build();
        }catch (Exception e){
            throw new WebApplicationException("Error while reading file", 422);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @RolesAllowed({"etudiant", "enseignant"})
    @Path("/download/assignmentfile/fileId={fileId}")
    public Response getJoinedAssignmentFile(@PathParam("fileId") int file_id){

        String cip = this.securityContext.getUserPrincipal().getName();

        // TODO CHANGER LA VERIFICATION, LE RESTE DEVRAIT ÊTRE LE MÊME
        if(!handedAssignmentMapper.canDownloadHandedAssignmentFile(cip))
            throw new WebApplicationException("You may not download this file", 401);

        DatabaseFile databaseFile = fileMapper.getFile(file_id);

        FileDataAccess fileDataAccess = new LocalFileWriter();
        try {
            return Response.ok(fileDataAccess.ReadFile(databaseFile), MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + databaseFile.displayed_name + databaseFile.extension + "\"") //optional
                    .build();
        }catch (Exception e){
            throw new WebApplicationException("Error while reading file", 422);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @RolesAllowed({"etudiant", "enseignant"})
    @Path("/download/grouphandedassignment/assignmentId={assignmentId}")
    public Response getGroupHandedAssignments(@PathParam("assignmentId") int assignmentId) {
        String cip = this.securityContext.getUserPrincipal().getName();

        // Verifier que l'etudiant fait partie du groupe dans lequel l'assignment est
        if(!assignmentMapper.isTeacherOfAssignment(assignmentId,cip))
            throw new WebApplicationException("You may not download this file", 401);

        String folder = handedAssignmentMapper.getHandedAssignmentFolder(assignmentId);

        FileDataAccess fileDataAccess = new LocalFileWriter();


        try {
            return Response.ok(fileDataAccess.getFolderAsZip(folder), MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + "Assignment_" + assignmentId + "_HandedAssignments.zip" + "\"")
                    .build();
        }catch (Exception e){
            throw new WebApplicationException("Error while reading file", 422);
        }
    }
}