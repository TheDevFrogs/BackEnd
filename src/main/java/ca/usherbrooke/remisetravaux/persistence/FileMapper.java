package ca.usherbrooke.remisetravaux.persistence;

import ca.usherbrooke.remisetravaux.business.DatabaseFile;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FileMapper {

    @Insert("INSERT INTO file (path, name, extension, displayed_name) " +
            "VALUES (#{f.path},#{f.name},#{f.extension},#{f.displayed_name})")
    @Options(useGeneratedKeys = true, keyProperty = "id_file", keyColumn = "id_file")
    void insertFile(@Param("f") DatabaseFile file);

    @Select("")
    boolean canDownloadAssignmentFile(@Param("cip") String cip, @Param("file_id") int file_id);

    @Select("SELECT f.id_file, f.name, f.displayed_name, f.extension, f.path " +
            "from assignment as a " +
            "INNER JOIN file as f on f.id_file = a.id_file " +
            "WHERE a.id_assignment = #{assignment_id}")
    DatabaseFile getAssignmentFile(@Param("assignment_id") int assignment_id);
}
