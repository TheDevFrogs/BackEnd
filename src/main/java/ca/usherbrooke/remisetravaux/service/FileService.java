package ca.usherbrooke.remisetravaux.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Path("/file")
public class FileService {


    @Context
    SecurityContext securityContext;
    // VOIR https://stackoverflow.com/questions/12239868/whats-the-correct-way-to-send-a-file-from-rest-web-service-to-client

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
    public void getAssignmentFile(int id_file) {
        // Vérifier que la personne soit un étudiant ou un enseignant de ce groupe

        //Aller chercher le path du fichier via la table file

        //Envoyer le fichier
    }

    @GET
    public void getGroupHandedAssingmentFile(int assignment_id) {
        //Verifier que la personne soit enseignant dans le groupe

        //Aller chercher tous les fichiers de tous le assignments
        //Zip tous les fichiers enssemble

        //Envoyer tous les fichiers
    }

}
