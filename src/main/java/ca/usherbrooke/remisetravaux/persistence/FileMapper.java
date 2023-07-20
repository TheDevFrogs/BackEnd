package ca.usherbrooke.remisetravaux.persistence;

import ca.usherbrooke.remisetravaux.business.DatabaseFile;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FileMapper {

    @Insert("INSERT INTO file (path, name, extension, displayed_name) " +
            "VALUES (#{f.path},#{f.name},#{f.extension},#{f.displayed_name})")
    @Options(useGeneratedKeys = true, keyProperty = "id_file", keyColumn = "id_file")
    void insertFile(@Param("f") DatabaseFile file);

    @Select("SELECT COALESCE(" +
            "            (SELECT 1" +
            "             FROM file " +
            "                INNER JOIN assignment a on file.id_file = a.id_file " +
            "                inner join groupmember g on a.id_group = g.id_group " +
            "             WHERE file.id_file = #{id_file} AND cip = #{cip} " +
            "    LIMIT 1), 0)")
    boolean canDownloadAssignmentFile(@Param("cip") String cip, @Param("id_file") int id_file);

    @Select("SELECT f.id_file, f.name, f.displayed_name, f.extension, f.path " +
            "from assignment as a " +
            "INNER JOIN file as f on f.id_file = a.id_file " +
            "WHERE a.id_assignment = #{assignment_id}")
    DatabaseFile getAssignmentFile(@Param("assignment_id") int assignment_id);
    @Select("SELECT * " +
            "FROM file AS f " +
            "WHERE f.id_file = #{id_file}")
    DatabaseFile getFile(@Param("id_file") int id_file);

    @Select("SELECT COALESCE( " +
            "               (SELECT 1 " +
            "        FROM assignmentcorrection as ac " +
            "        INNER JOIN teammember as tm on tm.id_team = ac.id_team " +
            "        INNER JOIN team as t on ac.id_team = t.id_team " +
            "        INNER JOIN assignment as a on a.id_assignment = t.id_assignment " +
            "        INNER JOIN groupmember gm on a.id_group = gm.id_group " +
            "        WHERE ac.id_file = #{file_id} AND (tm.cip = #{cip} OR (gm.cip = #{cip} AND gm.id_role = 2)) " +
            "        LIMIT 1) " +
            "           , 0);")
    boolean canDownloadCorrectionFile(@Param("cip") String cip, @Param("file_id") int file_id);

}
