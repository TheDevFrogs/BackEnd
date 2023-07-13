package ca.usherbrooke.remisetravaux.persistence;

import ca.usherbrooke.remisetravaux.business.userinfo.SessionAndRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GroupMapper {
    @Insert("SELECT sr.sessionnom,sr.id_session, sr.rolename, sr.id_role"+
            " FROM sessionrole as sr" +
            " WHERE sr.cip = #{id}")
    List<SessionAndRole> getAllUserSessions(String id);

}
