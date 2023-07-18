package ca.usherbrooke.remisetravaux.service;

import br.com.fluentvalidator.context.ValidationResult;
import ca.usherbrooke.remisetravaux.business.*;
import ca.usherbrooke.remisetravaux.dto.assignment.TeacherAssignmentPage;
import ca.usherbrooke.remisetravaux.files.FileDataAccess;
import ca.usherbrooke.remisetravaux.files.LocalFileWriter;
import ca.usherbrooke.remisetravaux.persistence.FileMapper;
import ca.usherbrooke.remisetravaux.persistence.GroupMapper;
import ca.usherbrooke.remisetravaux.persistence.HandedAssignmentMapper;
import ca.usherbrooke.remisetravaux.persistence.TeamMapper;
import ca.usherbrooke.remisetravaux.validator.AssignmentValidator;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import ca.usherbrooke.remisetravaux.dto.assignment.StudentAssignmentPage;
import ca.usherbrooke.remisetravaux.persistence.AssignmentMapper;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
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

    @Inject
    AssignmentMapper assignmentMapper;

    @POST
    @Path("/create")
    @RolesAllowed({"etudiant", "enseignant"})
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Assignment createAssignment(MultipartFormDataInput input) {

        String cip = this.securityContext.getUserPrincipal().getName();
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

        AssignmentMapper assignmentmapper = sqlSession.getMapper(AssignmentMapper.class);
        FileMapper fileMapper = sqlSession.getMapper(FileMapper.class);
        GroupMapper groupMapper = sqlSession.getMapper(GroupMapper.class);

        if (!groupMapper.isGroupTeacher(cip, assignment.id_group))
            throw new WebApplicationException("You are not the teacher of this group", 401);

        //Pour eviter des problèmes l'ors de l'insetion, il
        //est primordial d'utiliser une session sql

        FileDataAccess dataAccess = new LocalFileWriter();
        try {
            assignmentmapper.addAssignment(assignment);

            if (fileData.length != 0) {
                //Obtenir le nom du fichier

                DatabaseFile databaseFile = new DatabaseFile();
                databaseFile.name = "handedFile.zip";
                databaseFile.path = assignmentmapper.getAssignmentFilePath(assignment.id_assignment);
                fileMapper.insertFile(databaseFile);

                assignment.id_file = databaseFile.id_file;
                dataAccess.WriteFile(databaseFile.path, databaseFile.name, fileData);

                //We need to update the assignment and add the file to it
                assignmentmapper.updateAssignmentFile(assignment.id_assignment, databaseFile.id_file);
            }
            sqlSession.commit();
        } catch (Throwable e) {
            sqlSession.rollback(true);
            throw new WebApplicationException("Error while adding to database", 422);
        } finally {
            sqlSession.close();
        }

        return assignment;
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

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
    @RolesAllowed({"etudiant", "enseignant"})
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/studentpreview/assignmentId={assignmentId}")
    public StudentAssignmentPage getStudentAssignmentDisplay(@PathParam("assignmentId") int assignmentId) {

        String cip = this.securityContext.getUserPrincipal().getName();

        if (!assignmentMapper.isStudentOfAssingment(assignmentId, cip))
            throw new WebApplicationException("You are not a student of this assignment", 401);

        StudentAssignmentPage studentAssignmentPage = assignmentMapper.geStudentAssignmentPage(assignmentId, cip);

        return studentAssignmentPage;
    }

    @GET
    @RolesAllowed({"etudiant", "enseignant"})
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/teacherpreview/assignmentId={assignmentId}")
    public TeacherAssignmentPage getTeacherAssignmentDisplay(@PathParam("assignmentId") int assignmentId) {

        String cip = this.securityContext.getUserPrincipal().getName();

        if (!assignmentMapper.isTeacherOfAssignment(assignmentId, cip))
            throw new WebApplicationException("You are not a teacher of this group", 401);

        TeacherAssignmentPage teacherAssignmentPage = assignmentMapper.getTeacherAssignmentPage(assignmentId);

        return teacherAssignmentPage;
    }

    @POST
    @Transactional
    @Path("/hand")
    @RolesAllowed({"etudiant", "enseignant"})
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public HandedAssignment createHandedAssignment(MultipartFormDataInput input) {
        Date currentTime = new Date();
        String cip = this.securityContext.getUserPrincipal().getName();

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        SqlSession sqlSession = sqlSessionFactory.openSession(false);
        TeamMapper teamMapper = sqlSession.getMapper(TeamMapper.class);
        HandedAssignmentMapper handedAssignmentMapper = sqlSession.getMapper(HandedAssignmentMapper.class);
        FileMapper fileMapper = sqlSession.getMapper(FileMapper.class);

        byte[] fileData;
        HandedAssignment handedAssignment = new HandedAssignment();
        try {
            int assignmentId = Integer.parseInt(input.getFormDataPart("assignmentId", String.class, null));

            if (!assignmentMapper.isStudentOfAssingment(assignmentId, cip))
                throw new WebApplicationException("You are not a student of this assignment", 401);
            fileData = getFileData(uploadForm.get("file").get(0));
            if (fileData.length == 0)
                throw new WebApplicationException("File received was empty", 400);
            //Vérifier si l'étudiant est déja dans un équipe
            Team team = teamMapper.getStudentTeam(assignmentId, cip);
            //Si ce n'est pas le cas, créer équipe avec étudiant
            // et l'ajouter dans teamMember
            if (team == null) {
                team = new Team();
                team.id_assignment = assignmentId;
                teamMapper.insertTeam(team);

                TeamMember teamMember = new TeamMember();
                teamMember.cip = cip;
                teamMember.id_team = team.id_team;

                teamMapper.insertTeamMember(teamMember);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdHHmmssSS");
            String fileName = dateFormat.format(currentTime) + "Remise";

            //Aller chercher le path de fichier
            String filePath = handedAssignmentMapper.getHandedAssignmentFilePath(team.id_team);
            //Ajouter le fichier dans la bd
            DatabaseFile databaseFile = new DatabaseFile();
            databaseFile.name = fileName;
            databaseFile.path = filePath;
            databaseFile.extension = ".zip";
            fileMapper.insertFile(databaseFile);


            handedAssignment.handed_date = currentTime;
            handedAssignment.id_file = databaseFile.id_file;
            handedAssignment.id_team = team.id_team;

            handedAssignmentMapper.insertHandedAssignment(handedAssignment);

            FileDataAccess dataAccess = new LocalFileWriter();
            dataAccess.WriteFile(databaseFile.path, databaseFile.name + databaseFile.extension, fileData);


            sqlSession.commit();
        } catch (IOException e) {
            sqlSession.rollback();
            throw new WebApplicationException("Error while adding to database", 422);
        } finally {
            sqlSession.close();
        }

        return handedAssignment;
    }
    @DELETE
    @Path("/delete/assignmentId={assignmentId}")
    @RolesAllowed({"etudiant", "enseignant"})
    public boolean deleteAssignment(@PathParam("assignmentId") int assignmentId) {

        String cip = this.securityContext.getUserPrincipal().getName();
        //Verify is the person is a teacher in this assignment

        if (!assignmentMapper.isTeacherOfAssignment(assignmentId, cip))
            throw new WebApplicationException("You are not a teacher of this assignment", 401);

        assignmentMapper.deleteAssignment(assignmentId);

        return true;
    }
}