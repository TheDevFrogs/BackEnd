package ca.usherbrooke.remisetravaux.service;

import ca.usherbrooke.remisetravaux.business.AssignmentCorrection;
import ca.usherbrooke.remisetravaux.business.DatabaseFile;
import ca.usherbrooke.remisetravaux.business.HandedAssignment;
import ca.usherbrooke.remisetravaux.business.Team;
import ca.usherbrooke.remisetravaux.files.FileDataAccess;
import ca.usherbrooke.remisetravaux.files.LocalFileWriter;
import ca.usherbrooke.remisetravaux.persistence.FileMapper;
import ca.usherbrooke.remisetravaux.persistence.HandedAssignmentMapper;
import ca.usherbrooke.remisetravaux.persistence.AssignmentMapper;
import ca.usherbrooke.remisetravaux.persistence.TeamMapper;
import ca.usherbrooke.remisetravaux.service.logic.fileservice.FileServiceLogic;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.postgresql.core.Tuple;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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

    @Inject
    SqlSessionFactory sqlSessionFactory;

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @RolesAllowed({"etudiant", "enseignant"})
    @Path("/download/handedassignmentfile/fileId={fileId}")
    public Response getHandedAssignmentFile(@PathParam("fileId") int file_id) {
        String cip = this.securityContext.getUserPrincipal().getName();

        // Verifier que l'etudiant fait partie du groupe dans lequel l'assignment est
        if(!handedAssignmentMapper.canDownloadHandedAssignmentFile(cip, file_id))
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
    @Path("/download/assignmentcorrection/fileId={fileId}")
    public Response getAssignmentCorrection(@PathParam("fileId") int file_id){
        String cip = this.securityContext.getUserPrincipal().getName();

        // Verifier que l'etudiant fait partie du groupe dans lequel l'assignment est
        if(!fileMapper.canDownloadCorrectionFile(cip, file_id))
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

        if(!fileMapper.canDownloadAssignmentFile(cip,file_id))
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

    @POST
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({"etudiant", "enseignant"})
    @Path("/upload/groupassignmentcorrection")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String uploadGroupAssignmentCorrection(MultipartFormDataInput input) {
        Date currentTime = new Date();
        String cip =  this.securityContext.getUserPrincipal().getName();

        SqlSession sqlSession = sqlSessionFactory.openSession(false);
        FileMapper filemapper = sqlSession.getMapper(FileMapper.class);
        TeamMapper teammaper = sqlSession.getMapper(TeamMapper.class);
        HandedAssignmentMapper handedAssignmentmapper =  sqlSession.getMapper(HandedAssignmentMapper.class);
        try {
            int assignmentId = Integer.parseInt(input.getFormDataPart("assignmentId", String.class, null));
            if(!assignmentMapper.isTeacherOfAssignment(assignmentId,cip)) {
                throw new WebApplicationException("You may not upload this file", 401);
            }

            byte[] fileData = LocalFileWriter.getFileData(input.getFormDataMap().get("file").get(0));
            String filePath = handedAssignmentMapper.getCorrectionFolder(assignmentId);

            ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(fileData));

            List<Team> teams = teammaper.getAllAssignmentTeams(assignmentId);

            int currentTeamNumber = -1;
            List<FileServiceLogic> fileServiceLogics = new ArrayList<>();
            ZipOutputStream zipOutputStream = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdHHmmssSS");

            FileServiceLogic fileServiceLogic = null;
            ZipEntry entry = null;
            while ((entry = zipStream.getNextEntry()) != null)
            {
                String entryName = entry.toString();
                int newNumber = Integer.parseInt(entryName.substring(entryName.indexOf("_") + 1,entryName.indexOf("/")));
                String fileName = entryName.substring(entryName.indexOf("/") + 1);

                if (currentTeamNumber != newNumber)
                {
                    if (currentTeamNumber != -1){
                        zipOutputStream.finish();
                        zipOutputStream.flush();
                        zipOutputStream.close();
                        fileServiceLogic.byteArrayOutputStream.flush();
                        fileServiceLogic.byteArrayOutputStream.close();
                    }
                    fileServiceLogic = new FileServiceLogic();
                    fileServiceLogics.add(fileServiceLogic);
                    fileServiceLogic.byteArrayOutputStream = new ByteArrayOutputStream();
                    zipOutputStream = new ZipOutputStream(fileServiceLogic.byteArrayOutputStream);

                    fileServiceLogic.team = teams.stream().filter(team -> team.no_equipe == newNumber).findAny().orElse(null);
                    DatabaseFile databaseFile = new DatabaseFile();
                    databaseFile.name = dateFormat.format(currentTime) + "correction";
                    databaseFile.displayed_name = dateFormat.format(currentTime) + "correction";
                    databaseFile.path = filePath + "equipe_" + newNumber + "/";
                    databaseFile.extension = ".zip";
                    fileServiceLogic.databaseFile = databaseFile;
                    currentTeamNumber = newNumber;
                }

                ZipEntry ze = new ZipEntry(fileName);
                zipOutputStream.putNextEntry(ze);
                //read the file and write to ZipOutputStream
                zipOutputStream.write(zipStream.readAllBytes());
                zipOutputStream.closeEntry();
            }

            zipOutputStream.finish();
            zipOutputStream.flush();
            zipOutputStream.close();
            fileServiceLogic.byteArrayOutputStream.flush();
            fileServiceLogic.byteArrayOutputStream.close();

            //Once all of the files have recompressed we can create the files and the assignmentCorrections
            FileDataAccess fileDataAccess = new LocalFileWriter();
            for (FileServiceLogic file : fileServiceLogics){
                filemapper.insertFile(file.databaseFile);
                AssignmentCorrection assignmentCorrection = new AssignmentCorrection();
                assignmentCorrection.id_assignment = assignmentId;
                assignmentCorrection.id_file = file.databaseFile.id_file;
                assignmentCorrection.corrected_date = currentTime;
                assignmentCorrection.id_team = file.team.id_team;
                handedAssignmentmapper.insertAssignmentCorrection(assignmentCorrection);

                fileDataAccess.WriteFile(file.databaseFile,file.byteArrayOutputStream.toByteArray());
            }
            sqlSession.commit();
        } catch (IOException e) {
            sqlSession.rollback();
            throw new WebApplicationException("Error while uploading files", 422);
        }finally {
            sqlSession.close();
        }

        return "success";
    }


    //TODO tester
    @POST
    @Transactional
    @RolesAllowed({"etudiant", "enseignant"})
    @Path("/upload/assignmentcorrection")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public AssignmentCorrection createCorrectedAssignment(MultipartFormDataInput input)  {
        Date currentTime = new Date();
        String cip =  this.securityContext.getUserPrincipal().getName();

        SqlSession sqlSession = sqlSessionFactory.openSession(false);
        FileMapper filemapper = sqlSession.getMapper(FileMapper.class);
        HandedAssignmentMapper handedAssignmentmapper =  sqlSession.getMapper(HandedAssignmentMapper.class);
        TeamMapper teammapper = sqlSession.getMapper(TeamMapper.class);
        AssignmentCorrection assignmentCorrection;

        try {
            int teamId = Integer.parseInt(input.getFormDataPart("teamId", String.class, null));

            if (teammapper.isTeacherOfTeam(cip,teamId)) {
                throw new WebApplicationException("You may not upload this file", 401);
            }

            byte[] fileData = LocalFileWriter.getFileData(input.getFormDataMap().get("file").get(0));

            Team team = teammapper.getTeam(teamId);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdHHmmssSS");

            String filePath = handedAssignmentmapper.getCorrectionFolder(team.id_assignment);
            DatabaseFile databaseFile = new DatabaseFile();
            databaseFile.name = dateFormat.format(currentTime) + "correction";
            databaseFile.displayed_name = dateFormat.format(currentTime) + "correction";
            databaseFile.path = filePath + "equipe_" + team.no_equipe + "/";
            databaseFile.extension = ".zip";

            filemapper.insertFile(databaseFile);

            assignmentCorrection = new AssignmentCorrection();
            assignmentCorrection.id_assignment = team.id_assignment;
            assignmentCorrection.id_file = databaseFile.id_file;
            assignmentCorrection.corrected_date = currentTime;
            assignmentCorrection.id_team = teamId;

            handedAssignmentmapper.insertAssignmentCorrection(assignmentCorrection);

            FileDataAccess fileDataAccess = new LocalFileWriter();
            fileDataAccess.WriteFile(databaseFile,fileData);
            sqlSession.commit();
        } catch (IOException e) {
            sqlSession.rollback();
            throw new WebApplicationException("Error while uploading files", 422);
        }
        finally {
            sqlSession.close();
        }
        return assignmentCorrection;
    }
}