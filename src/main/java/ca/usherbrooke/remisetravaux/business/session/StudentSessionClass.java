package ca.usherbrooke.remisetravaux.business.session;

import java.util.List;

public class StudentSessionClass {
    public int id_group;
    public String name;
    public String classTag;
    public int no_group;
    public List<Teacher> teachers;
    public List<StudentAssigmentPreview> assigments;
}