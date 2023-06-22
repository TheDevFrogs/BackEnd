package ca.usherbrooke.remisetravaux.persistence;

import ca.usherbrooke.remisetravaux.business.Classes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SessionMapper {

    @Select(
            """
                    select id_class,name FROM class
            """
            )
    List<Classes> getUserSessionClasses(String cip, String descriptionSession);
}
