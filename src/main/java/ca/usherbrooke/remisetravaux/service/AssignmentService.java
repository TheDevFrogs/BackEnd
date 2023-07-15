package ca.usherbrooke.remisetravaux.service;

import br.com.fluentvalidator.context.ValidationResult;
import ca.usherbrooke.remisetravaux.business.Assignment;
import ca.usherbrooke.remisetravaux.business.AssignmentFile;
import ca.usherbrooke.remisetravaux.business.DatabaseFile;
import ca.usherbrooke.remisetravaux.files.FileDataAccess;
import ca.usherbrooke.remisetravaux.files.LocalFileWriter;
import ca.usherbrooke.remisetravaux.persistence.FileMapper;
import ca.usherbrooke.remisetravaux.persistence.GroupMapper;
import ca.usherbrooke.remisetravaux.validator.AssignmentValidator;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import ca.usherbrooke.remisetravaux.persistence.AssignmentMapper;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Path("/assignment")
public class AssignmentService {



    @Context
    SecurityContext securityContext;
    @Inject
    SqlSessionFactory sqlSessionFactory;

    @POST
    @Path("/create")
    //@RolesAllowed({"etudiant","enseignant"})
    @PermitAll
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Assignment createAssignment(MultipartFormDataInput input) {

        String cip = "lavm2134";// this.securityContext.getUserPrincipal().getName();
        Assignment assignment = new Assignment();
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        byte[] fileData;
        try {
            assignment.id_group = Integer.parseInt(input.getFormDataPart("group_id", String.class, null));
            assignment.description = input.getFormDataPart("description", String.class, null);
            assignment.name = input.getFormDataPart("name", String.class, null);
            assignment.due_date = fromStringToDate(input.getFormDataPart("due_date", String.class, null));
            assignment.close_date = fromStringToDate(input.getFormDataPart("close_date", String.class, null));
            assignment.available_date = fromStringToDate(input.getFormDataPart("available_date", String.class, null));
            assignment.setdefaultValues();
            fileData = getFileData(uploadForm.get("file").get(0));

            //TODO When the software will be ready, teachers will be able to change team sizes.
            assignment.team_size = 1;
            //Verify if the input is valid
            final AssignmentValidator validator = new AssignmentValidator();
            ValidationResult validationResult = validator.validate(assignment);

            if (!validationResult.isValid())
                throw new IOException("Invalid data format for Assignment /assignment/create");

            //Get the file
            List<InputPart> inputParts = uploadForm.get("file");

        } catch (IOException ioException) {
            throw new WebApplicationException("Some of the fields are invalid", 400);
        } catch (ParseException e) {
            throw new WebApplicationException("Dates are sent in the wrong format", 400);
        }
        SqlSession sqlSession = sqlSessionFactory.openSession(false);
        Configuration config = sqlSessionFactory.getConfiguration();
        AssignmentMapper assignmentMapper = sqlSession.getMapper(AssignmentMapper.class);
        FileMapper fileMapper = sqlSession.getMapper(FileMapper.class);
        GroupMapper groupMapper = sqlSession.getMapper(GroupMapper.class);

        if (!groupMapper.isGroupTeacher(cip, assignment.id_group))
            throw new WebApplicationException("You are not the teacher of this group", 401);

        //Pour eviter des problèmes l'ors de l'insetion, il
        //est primordial d'utiliser une session sql

        FileDataAccess dataAccess = new LocalFileWriter();
        try {
            assignmentMapper.addAssignment(assignment);

            if (fileData.length != 0){
                //Obtenir le nom du fichier

                DatabaseFile databaseFile = new DatabaseFile();
                databaseFile.name = "handedFile.zip";
                databaseFile.path = assignmentMapper.getAssignmentFilePath(assignment.id_assignment);

                fileMapper.insertFile(databaseFile);

                AssignmentFile assignmentFile = new AssignmentFile();
                assignmentFile.id_assignment = assignment.id_assignment;
                assignmentFile.id_file = databaseFile.id_file;
                fileMapper.insertAssignmentFile(assignmentFile);

                dataAccess.WriteFile(databaseFile.path,databaseFile.name,fileData);
            }
            sqlSession.commit();
        } catch (Throwable e) {
            sqlSession.rollback(true);
            throw new WebApplicationException("Error while adding to database", 422);
        }
        finally {
            sqlSession.close();
        }

        return assignment;
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Date fromStringToDate(String date) throws ParseException {

        if (date != null && !date.isEmpty())
            return dateFormat.parse(date);
        else
            return null;
    }

    private byte[] getFileData(InputPart inputPart) throws IOException {

        MultivaluedMap<String, String> header = inputPart.getHeaders();

        // convert the uploaded file to inputstream
        InputStream inputStream = inputPart.getBody(InputStream.class, null);
        return IOUtils.toByteArray(inputStream);
    }

    @GET
    @Path("/assignment/{id}")
    @PermitAll
    public String getAssignmentInfo(MultipartFormDataInput input) {
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("file");
        List<InputPart> teamId = uploadForm.get("team_id");

        return "yo";
    }
}
