package ca.usherbrooke.remisetravaux.persistence;

import ca.usherbrooke.remisetravaux.business.Assignment;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AssignmentMapper {
    @Insert("INSERT INTO assignment (id_group, name, description, due_date, close_date, available_date, team_size) " +
            "VALUES (#{a.id_group},#{a.name},#{a.description},#{a.due_date},#{a.close_date},#{a.available_date},#{a.team_size});")
    @Options(useGeneratedKeys = true, keyProperty = "id_assignment", keyColumn = "id_assignment")
    void addAssignment(@Param("a") Assignment assignment);

    @Select("SELECT CONCAT(g.id_session, '/',g.id_class,'/',no_group,'/',a.id_assignment,'/') as filePath " +
            "FROM assignment as a " +
            "INNER JOIN groupe as g on a.id_group = g.id_group " +
            "WHERE a.id_assignment = #{assignmentId}")
    String getAssignmentFilePath(@Param("assignmentId") int id);
}
