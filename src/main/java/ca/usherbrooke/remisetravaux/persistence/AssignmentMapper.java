package ca.usherbrooke.remisetravaux.persistence;

import ca.usherbrooke.remisetravaux.business.Assignment;
import ca.usherbrooke.remisetravaux.dto.assignment.AssignmentFile;
import ca.usherbrooke.remisetravaux.dto.assignment.TeacherAssignmentPage;
import org.apache.ibatis.annotations.*;
import ca.usherbrooke.remisetravaux.dto.assignment.StudentAssignmentPage;

import java.util.List;

@Mapper
public interface AssignmentMapper {
    @Insert("INSERT INTO assignment (id_group, name, description, due_date, close_date, available_date, team_size, id_file) " +
            "VALUES (#{a.id_group},#{a.name},#{a.description},#{a.due_date},#{a.close_date},#{a.available_date},#{a.team_size},#{a.id_file});")
    @Options(useGeneratedKeys = true, keyProperty = "id_assignment", keyColumn = "id_assignment")
    void addAssignment(@Param("a") Assignment assignment);

    @Select("SELECT CONCAT(g.id_session, '/',g.id_class,'/',no_group,'/',a.id_assignment,'/') as filePath " +
            "FROM AvailableAssignment as a " +
            "INNER JOIN groupe as g on a.id_group = g.id_group " +
            "WHERE a.id_assignment = #{assignmentId}")
    String getAssignmentFilePath(@Param("assignmentId") int id);

    @Update("UPDATE assignment " +
            "SET id_file = #{fileId} " +
            "WHERE id_assignment = #{assignmentId};")
    void updateAssignmentFile(@Param("assignmentId") int id, @Param("fileId")  int fileId);

    @Results({
            @Result(property = "id_assignment", column = "id_assignment"),
            @Result(property = "name", column = "name"),
            @Result(property = "description", column = "description"),
            @Result(property = "available_date", column = "available_date"),
            @Result(property = "due_date", column = "due_date"),
            @Result(property = "close_date", column = "close_date"),
            @Result(property = "file.id_file", column = "id_file"),
            @Result(property = "file.displayed_name", column = "displayed_name"),
            @Result(property = "file.extension", column = "extension"),
            @Result(property = "handed_work_files", column = "{id_assignment=id_assignment,cip=cip}", many = @Many(select = "getHandedWorkFile")),
            @Result(property = "corrected_work_files", column = "{id_assignment=id_assignment,cip=cip}", many = @Many(select = "getCorrectedWorkFile"))
    })
    @Select("SELECT a.id_assignment, a.name as name, a.description, a.available_date," +
                    " a.due_date, a.close_date, f.id_file, f.displayed_name, f.extension ,#{cip} as cip " +
            "FROM AvailableAssignment as a " +
            "LEFT JOIN file AS F ON a.id_file = f.id_file " +
            "WHERE a.id_assignment = #{assignmentId}")
    StudentAssignmentPage geStudentAssignmentPage(@Param("assignmentId") int assignmentId, @Param("cip") String cip);

    @Results({
            @Result(property = "id_assignment", column = "id_assignment"),
            @Result(property = "name", column = "name"),
            @Result(property = "description", column = "description"),
            @Result(property = "available_date", column = "available_date"),
            @Result(property = "due_date", column = "due_date"),
            @Result(property = "close_date", column = "close_date"),
            @Result(property = "file.id_file", column = "id_file"),
            @Result(property = "file.file_name", column = "file_name")})
    @Select("SELECT a.id_assignment, a.name as name, a.description, a.available_date," +
            " a.due_date, a.close_date, f.id_file, f.name as filename " +
            "FROM AvailableAssignment as a " +
            "LEFT JOIN file AS F ON a.id_file = f.id_file " +
            "WHERE a.id_assignment = #{assignmentId}")
    TeacherAssignmentPage getTeacherAssignmentPage(@Param("assignmentId") int assignmentId);

    @Select("SELECT ha.handed_date, f.displayed_name, f.id_file, f.extension " +
            "FROM teammember as tm " +
            "INNER JOIN team AS t on tm.id_team = t.id_team " +
            "INNER JOIN handedassignment as ha on ha.id_team = t.id_team " +
            "INNER JOIN file as f on f.id_file = ha.id_file " +
            "WHERE tm.cip = #{cip} AND t.id_assignment = #{id_assignment}")
    AssignmentFile getHandedWorkFile(@Param("id_assignment") int id_assignment, @Param("cip") String cip);

    @Select("SELECT ac.corrected_date as handed_date , f.displayed_name, f.id_file, f.extension " +
            "FROM teammember as tm " +
            "INNER JOIN team AS t on tm.id_team = t.id_team " +
            "INNER JOIN assignmentcorrection as ac on ac.id_team = tm.id_team " +
            "INNER JOIN file as f on f.id_file = ac.id_file " +
            "WHERE tm.cip = #{cip} AND t.id_assignment = #{id_assignment}")
    AssignmentFile getCorrectedWorkFile(@Param("id_assignment") int id_assignment, @Param("cip") String cip);

    @Select("SELECT ha.handed_date, f.displayed_name, f.id_file, f.extension " +
            "FROM team AS t " +
            "INNER JOIN handedassignment as ha on ha.id_team = t.id_team " +
            "INNER JOIN file as f on f.id_file = ha.id_file " +
            "WHERE t.id_team = #{team_id};")
    List<AssignmentFile> getHandedWorkFiles(@Param("team_id") int team_id);

    @Select("SELECT ac.corrected_date as handed_date , f.displayed_name, f.id_file, f.extension " +
            "FROM team AS t " +
            "INNER JOIN assignmentcorrection as ac on ac.id_team = t.id_team " +
            "INNER JOIN file as f on f.id_file = ac.id_file " +
            "WHERE t.id_team = #{team_id}")
    List<AssignmentFile> getCorrectedWorkFiles(@Param("team_id") int team_id);

    @Select("SELECT COALESCE( " +
            "    (SELECT 1 " +
            "FROM AvailableAssignment as a " +
            "INNER JOIN groupmember gm on a.id_group = gm.id_group " +
            "WHERE a.id_assignment = #{assignmentId} and gm.cip = #{cip} and gm.id_role = 1),0);")
    boolean isStudentOfAssingment(@Param("assignmentId") int assignmentId, @Param("cip") String cip);

    @Update("UPDATE assignment " +
            "SET is_deleted = true " +
            "WHERE id_assignment = #{assignmentId};")
    void deleteAssignment(@Param("assignmentId") int id);
    @Select("SELECT COALESCE( " +
            "                (SELECT 1 from groupmember as gm " +
            "                    INNER JOIN AvailableAssignment as a on a.id_group = gm.id_group " +
            "                WHERE gm.id_role = 2 AND gm.cip = #{cip} AND a.id_assignment = #{assignmentId}),0);")
    boolean isTeacherOfAssignment(@Param("assignmentId") int assignmentId,@Param("cip") String cip);

    @Update("UPDATE assignment " +
            "SET name = #{assignmentObject.name}," +
            "    description = #{assignmentObject.description}," +
            "    due_date = #{assignmentObject.due_date}," +
            "    close_date = #{assignmentObject.close_date}," +
            "    available_date = #{assignmentObject.available_date}," +
            "    team_size = #{assignmentObject.team_size} " +
            "WHERE id_assignment = #{assignmentObject.id_assignment};")
    void updateAssignment(@Param("assignmentObject") Assignment assignment);
    @Select("SELECT * " +
            "FROM assignment as a " +
            "WHERE a.id_assignment = #{id_assignment}")
    Assignment getAssignment(@Param("id_assignment") int assignmentId);

}
