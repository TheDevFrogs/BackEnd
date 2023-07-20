package ca.usherbrooke.remisetravaux.persistence;

import ca.usherbrooke.remisetravaux.business.Team;
import ca.usherbrooke.remisetravaux.business.TeamMember;
import ca.usherbrooke.remisetravaux.business.session.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeamMapper {
    @Select("SELECT t.id_team, t.id_assignment, t.no_equipe " +
            "from team as t " +
            "INNER JOIN teammember as tm on t.id_team = tm.id_team " +
            "WHERE cip = #{cip} AND t.id_assignment = #{id_assignment}")
    Team getStudentTeam(@Param("id_assignment") int id_assignment, @Param("cip") String cip);

    @Insert("INSERT INTO team (ID_ASSIGNMENT) " +
            "VALUES (#{team.id_assignment})")
    @Options(useGeneratedKeys = true, keyProperty = "id_team", keyColumn = "id_team")
    void insertTeam(@Param("team") Team team);
    @Select("SELECT * " +
            "FROM team as t " +
            "WHERE t.id_team = #{team_id}")
    Team getTeam(@Param("team_id") int teamId);


    @Insert("INSERT INTO teammember (cip, id_team) " +
            "VALUES (#{teamMember.cip}, #{teamMember.id_team})")
    void insertTeamMember(@Param("teamMember") TeamMember teamMember);

    @Select("Select * " +
            "FROM TEAM " +
            "WHERE id_assignment = #{id_assignment}")
    List<Team> getAllAssignmentTeams(@Param("id_assignment") int assignmentId);

    @Select("SELECT COALESCE( " +
            "               (SELECT 1 " +
            "FROM team as t " +
            "INNER JOIN assignment a on t.id_assignment = a.id_assignment " +
            "INNER JOIN groupmember as gm on gm.id_group = a.id_group " +
            "WHERE t.id_team = #{id_team} AND cip = #{cip} AND gm.id_role = 2 " +
            "LIMIT 1),0);")
    boolean isTeacherOfTeam(@Param("cip") String cip, @Param("id_team") int teamId);
}