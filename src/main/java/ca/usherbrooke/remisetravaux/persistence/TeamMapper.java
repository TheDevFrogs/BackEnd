package ca.usherbrooke.remisetravaux.persistence;

import ca.usherbrooke.remisetravaux.business.session.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeamMapper {
    @Select("SELECT team")
    int getTeamId(@Param("id_assignment") int id_assignment, @Param("cip") String cip);
}
