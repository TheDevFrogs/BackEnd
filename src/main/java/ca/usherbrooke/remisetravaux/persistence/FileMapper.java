package ca.usherbrooke.remisetravaux.persistence;

import ca.usherbrooke.remisetravaux.business.AssignmentFile;
import ca.usherbrooke.remisetravaux.business.DatabaseFile;
import org.apache.ibatis.annotations.*;

import java.io.File;

@Mapper
public interface FileMapper {

    @Insert("INSERT INTO file (path, name) " +
            "VALUES (#{f.path},#{f.name})")
    @Options(useGeneratedKeys = true, keyProperty = "id_file", keyColumn = "id_file")
    void insertFile(@Param("f") DatabaseFile file);

    @Insert("INSERT INTO assignmentfile (id_assignment, id_file) " +
            "VALUES (#{f.id_assignment},#{f.id_file})")
    void insertAssignmentFile(@Param("f") AssignmentFile file);
}
