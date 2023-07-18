package ca.usherbrooke.remisetravaux.persistence;

import ca.usherbrooke.remisetravaux.business.userinfo.SessionAndRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface GroupMapper {

    @Select("SELECT COALESCE( " +
            "    (SELECT 1 from groupmember " +
            "    WHERE id_role = 2 AND cip = #{cip} AND id_group = #{group_id}),0);")
    boolean isGroupTeacher(@Param("cip") String cip,@Param("group_id") int group_id);
}