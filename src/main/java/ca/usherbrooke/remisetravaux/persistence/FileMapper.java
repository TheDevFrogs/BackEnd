package ca.usherbrooke.remisetravaux.persistence;

import ca.usherbrooke.remisetravaux.business.DatabaseFile;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FileMapper {

    @Insert("INSERT INTO file (path, name) " +
            "VALUES (#{f.path},#{f.name})")
    @Options(useGeneratedKeys = true, keyProperty = "id_file", keyColumn = "id_file")
    void insertFile(@Param("f") DatabaseFile file);

    @Select("")
    boolean canDownloadAssignmentFile(@Param("cip") String cip, @Param("file_id") int file_id);

    @Select("")
    DatabaseFile getFile(@Param("file_id") int file_id);
}
