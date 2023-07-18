package ca.usherbrooke.remisetravaux.persistence;

import ca.usherbrooke.remisetravaux.business.session.*;
import ca.usherbrooke.remisetravaux.business.userinfo.SessionAndRole;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface SessionMapper {
    @Select("SELECT sr.sessionnom, sr.id_session, sr.rolename, sr.id_role"+
            " FROM sessionrole as sr" +
            " WHERE sr.cip = #{id}")
    List<SessionAndRole> getAllUserSessions(String id);

    @Results({
            @Result(property = "id_group", column = "id_group"),
            @Result(property = "name", column = "name"),
            @Result(property = "classTag", column = "classTag"),
            @Result(property = "no_group", column = "no_group"),
            @Result(property = "teachers", column = "id_group", many = @Many(select = "getGroupTeachers")),
            @Result(property = "assigments", column = "{group_id=id_group,student_cip=student_cip}", many = @Many(select = "getStudentAssignmentPreview"))
    })
    @Select("SELECT sg.id_group as id_group, sg.description as name, sg.id_class as classTag," +
            "               sg.no_group as no_group, #{cip} as student_cip" +
            "        FROM class as c" +
            "        INNER JOIN (" +
            "            SELECT g.id_group, g.id_class, c.description, g.no_group" +
            "            FROM groupmember as gm" +
            "        INNER JOIN groupe as g on g.id_group = gm.id_group" +
            "        inner join class c on g.id_class = c.id_class" +
            "        WHERE g.id_session = #{sessionId} AND gm.id_role = 1 AND cip = #{cip}" +
            "        ) as sg on sg.id_class = c.id_class;")
    List<StudentSessionClass> getAllStudentClasses(@Param("cip") String cip, @Param("sessionId") int sessionId);

    @Results({
            @Result(property = "id_assignment", column = "id_assignment"),
            @Result(property = "name", column = "name")
    })
    @ConstructorArgs({
            @Arg(column = "available_date", javaType = Date.class),
            @Arg(column = "due_date", javaType = Date.class),
            @Arg(column = "close_date", javaType = Date.class),
            @Arg(column = "first_handed_work", javaType = Date.class)
    })
    @Select("SELECT a.id_assignment as id_assignment, a.name as name ,a.available_date as available_date, a.due_date as due_date, " +
            "       a.close_date as close_date, handedAssigment.handed_date as first_handed_work " +
            "FROM AvailableAssignment as a " +
            "LEFT JOIN( " +
            "    SELECT t.id_assignment, h.handed_date " +
            "    FROM teammember as tm " +
            "    INNER JOIN handedassignment as h on tm.id_team = h.id_team " +
            "    INNER JOIN team as t on t.id_team = tm.id_team " +
            "    WHERE  tm.cip = #{student_cip} " +
            "    order by h.handed_date " +
            "    fetch first 1 rows only " +
            ") AS handedAssigment on handedAssigment.id_assignment = a.id_assignment " +
            "WHERE a.id_group = #{group_id};")
    List<StudentAssigmentPreview> getStudentAssignmentPreview(@Param("group_id") int group_id, @Param("student_cip") String student_cip);

    @Select("SELECT m.cip as cip, m.first_name, m.last_name " +
            "FROM groupe as g " +
            "INNER JOIN groupmember as gm on gm.id_group = g.id_group " +
            "INNER JOIN member as m on m.cip = gm.cip " +
            "WHERE g.id_group = #{id_group} and gm.id_role = 2")
    List<Teacher> getGroupTeachers(@Param("id_group") int id_group);

    @Results({
            @Result(property = "id_group", column = "id_group"),
            @Result(property = "name", column = "name"),
            @Result(property = "classTag", column = "classTag"),
            @Result(property = "no_group", column = "no_group"),
            @Result(property = "teachers", column = "id_group", many = @Many(select = "getGroupTeachers")),
            @Result(property = "assigments", column = "id_group", many = @Many(select = "getTeacherAssignmentPreview"))
    })
    @Select("SELECT sg.id_group as id_group, sg.description as name, sg.id_class as classTag," +
            "               sg.no_group as no_group, #{cip} as teacher_cip" +
            "        FROM class as c" +
            "        INNER JOIN (" +
            "            SELECT g.id_group, g.id_class, c.description, g.no_group" +
            "            FROM groupmember as gm" +
            "        INNER JOIN groupe as g on g.id_group = gm.id_group" +
            "        inner join class c on g.id_class = c.id_class" +
            "        WHERE g.id_session = #{sessionId} AND gm.id_role = 2 AND cip = #{cip}" +
            "        ) as sg on sg.id_class = c.id_class;")
    List<TeacherSessionClass> getAllTeacherClasses(@Param("cip") String cip, @Param("sessionId") int sessionId);


    @Select("SELECT a.id_assignment as id_assignment, a.name as name, a.due_date as due_date " +
            "FROM AvailableAssignment as a " +
            "WHERE id_group = #{group_id}")
    List<TeacherAssignmentPreview> getTeacherAssignmentPreview(@Param("group_id") int group_id);
}
