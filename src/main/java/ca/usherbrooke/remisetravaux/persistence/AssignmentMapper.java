package ca.usherbrooke.remisetravaux.persistence;

import ca.usherbrooke.remisetravaux.business.session.Teacher;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AssignmentMapper {
    @Insert("SELECT 1 " +
            "from groupmember " +
            "WHERE id_role = 2 AND cip = 'lavm2134' AND id_group = ")
    void addHandedAssignment();
}
