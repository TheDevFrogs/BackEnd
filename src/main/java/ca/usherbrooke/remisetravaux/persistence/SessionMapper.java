package ca.usherbrooke.remisetravaux.persistence;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface SessionMapper {
    List<Class> getUserSessionClasses(String cip, String descriptionSession);
}
