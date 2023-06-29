package ca.usherbrooke.remisetravaux.persistence;

import ca.usherbrooke.remisetravaux.business.Classes;
import ca.usherbrooke.remisetravaux.business.userinfo.SessionAndRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SessionMapper {
    @Select("SELECT sr.sessionnom, sr.rolename FROM sessionrole as sr WHERE sr.cip = #{id}")
    List<SessionAndRole> getAllUserSessions(String id);
}
