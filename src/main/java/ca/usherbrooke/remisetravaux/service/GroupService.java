package ca.usherbrooke.remisetravaux.service;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Path("/group")
public class GroupService {

    @POST
    @Path("/addAssignment")
    @RolesAllowed({"etudiant","enseignant"})
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String sendMultipartData(MultipartFormDataInput input){
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("files");
        return "yo";
    }
}
