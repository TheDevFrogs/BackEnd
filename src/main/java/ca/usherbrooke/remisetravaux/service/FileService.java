package ca.usherbrooke.remisetravaux.service;

import ca.usherbrooke.remisetravaux.business.DatabaseFile;
import ca.usherbrooke.remisetravaux.files.FileDataAccess;
import ca.usherbrooke.remisetravaux.files.LocalFileWriter;
import ca.usherbrooke.remisetravaux.persistence.FileMapper;
import ca.usherbrooke.remisetravaux.persistence.HandedAssignmentMapper;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.PublicKey;

@Path("/file")
public class FileService {

    @Context
    SecurityContext securityContext;
    // VOIR https://stackoverflow.com/questions/12239868/whats-the-correct-way-to-send-a-file-from-rest-web-service-to-client

    @Inject
    FileMapper fileMapper;

    @Inject
    HandedAssignmentMapper handedAssignmentMapper;

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    //@RolesAllowed({"etudiant", "enseignant"})
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
}
