package ca.usherbrooke.remisetravaux.persistence;

import ca.usherbrooke.remisetravaux.business.HandedAssignment;
import org.apache.ibatis.annotations.*;

@Mapper
public interface HandedAssignmentMapper {
    @Select("SELECT CONCAT(g.id_session, '/',g.id_class,'/',no_group,'/',a.id_assignment,'/remises/',t.no_equipe,'/') as filePath " +
            "FROM team as t " +
            "INNER JOIN AvailableAssignment a on a.id_assignment = t.id_assignment " +
            "INNER JOIN groupe AS g ON g.id_group = a.id_group " +
            "WHERE id_team = #{teamId} ")
    String getHandedAssignmentFilePath(@Param("teamId") int teamId);

    @Insert("INSERT INTO handedassignment ( id_team, id_file, handed_date) " +
            "VALUES (#{handedAssignment.id_team},#{handedAssignment.id_file},#{handedAssignment.handed_date})")
    @Options(useGeneratedKeys = true, keyProperty = "id_handedassignment", keyColumn = "id_handedassignment")
    void insertHandedAssignment(@Param("handedAssignment") HandedAssignment handedAssignment);
}