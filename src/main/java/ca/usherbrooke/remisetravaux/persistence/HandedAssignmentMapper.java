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

    @Select("SELECT COALESCE( " +
            "               (SELECT 1 " +
            "                FROM handedassignment as ha " +
            "                INNER JOIN teammember as tm on tm.id_team = ha.id_team " +
            "                INNER JOIN team as t on ha.id_team = t.id_team " +
            "                INNER JOIN assignment as a on a.id_assignment = t.id_assignment " +
            "                INNER JOIN groupmember gm on a.id_group = gm.id_group " +
            "                WHERE ha.id_file = 1 AND (tm.cip = #{cip} OR (gm.cip = #{cip} AND gm.id_role = 2)) " +
            "                LIMIT 1) " +
            "           , 0);")
    boolean canDownloadHandedAssignmentFile(@Param("cip") String cip);
    @Select("SELECT CONCAT(g.id_session, '/',g.id_class,'/', g.no_group,'/',a.id_assignment,'/remises/') as filePath " +
            "               FROM assignment as a " +
            "               INNER JOIN groupe g on a.id_group = g.id_group " +
            "                WHERE a.id_assignment = #{assignmentId};")
    String getHandedAssignmentFolder(@Param("assignmentId") int assignmentId);
}