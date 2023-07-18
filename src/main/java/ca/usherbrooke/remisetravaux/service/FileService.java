package ca.usherbrooke.remisetravaux.service;

import ca.usherbrooke.remisetravaux.files.FileDataAccess;
import ca.usherbrooke.remisetravaux.files.LocalFileWriter;
import ca.usherbrooke.remisetravaux.persistence.FileMapper;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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


    //LES TYPES DE RETOUR DES MÉTHODES SERONT À CHANGER
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getHandedAssignmentFile(int id) {
        //Vérifier que la personne est prof dans ce groupe ou que la personne fait partie de l'équipe ayant remis le travail


        InputStream file = null;
        try {
            file = new FileInputStream("babla");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + "<<file name>>" + "\"") //optional
                .build();
    }


    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
//  @RolesAllowed({"etudiant", "enseignant"})
    @Path("/download/assignmentfile/fileId={fileId}")
    public Response getJoinedAssignmentFile(@PathParam("fileId") int file_id){

        String cip = "lavm2134"; //this.securityContext.getUserPrincipal().getName();

        // Verifier que l'etudiant fait partie du groupe dans lequel l'assignment est


        //Lire les info du fileID


        //Lire le fichier sur le

        FileDataAccess fileDataAccess = new LocalFileWriter();

        InputStream file = null;
        try {
            file = new FileInputStream("babla");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + "<<file name>>" + "\"") //optional
                .build();
    }
}
