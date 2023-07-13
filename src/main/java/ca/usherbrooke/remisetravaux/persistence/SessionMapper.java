package ca.usherbrooke.remisetravaux.persistence;

import ca.usherbrooke.remisetravaux.business.Classes;
import ca.usherbrooke.remisetravaux.business.userinfo.SessionAndRole;
import ca.usherbrooke.remisetravaux.dto.Sessions;
import ca.usherbrooke.remisetravaux.dto.Assignment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SessionMapper {
    @Select("SELECT sr.sessionnom, sr.rolename FROM sessionrole as sr WHERE sr.cip = #{id}")
    List<SessionAndRole> getAllUserSessions(String id);

    @Select("SELECT id_assignment, assignmentname, description, due_date, close_date, available_date, id_group" +
            "FROM  studentclass AS sc" +
            "where sc.cip = #{cip} and" +
            "      sc.id_assignment = #{AssignmentID} and" +
            "      sc.session = #{SessionID};")
    Assignment getAssignment(String cip, String AssignmentID, String SessionID);
}
