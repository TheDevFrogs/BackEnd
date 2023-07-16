package ca.usherbrooke.remisetravaux.persistence;

import ca.usherbrooke.remisetravaux.business.Assignment;
import ca.usherbrooke.remisetravaux.business.session.StudentAssigmentPreview;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AssignmentMapper {
    @Insert("INSERT INTO assignment (id_group, name, description, due_date, close_date, available_date, team_size, id_file) " +
            "VALUES (#{a.id_group},#{a.name},#{a.description},#{a.due_date},#{a.close_date},#{a.available_date},#{a.team_size},#{a.id_file});")
    @Options(useGeneratedKeys = true, keyProperty = "id_assignment", keyColumn = "id_assignment")
    void addAssignment(@Param("a") Assignment assignment);

    @Select("SELECT CONCAT(g.id_session, '/',g.id_class,'/',no_group,'/',a.id_assignment,'/') as filePath " +
            "FROM assignment as a " +
            "INNER JOIN groupe as g on a.id_group = g.id_group " +
            "WHERE a.id_assignment = #{assignmentId}")
    String getAssignmentFilePath(@Param("assignmentId") int id);

    @Update("UPDATE assignment " +
            "SET id_file = #{fileId} " +
            "WHERE id_assignment = #{assignmentId};")
    void updateAssignmentFile(@Param("assignmentId") int id, @Param("fileId")  int fileId);
}
