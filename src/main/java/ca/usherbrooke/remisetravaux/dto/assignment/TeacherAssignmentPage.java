package ca.usherbrooke.remisetravaux.dto.assignment;

import ca.usherbrooke.remisetravaux.dto.DisplayedFile;

import java.util.Date;
import java.util.List;

public class TeacherAssignmentPage {
    public int id_assignment;
    public String name;
    public String description;
    public Date available_date;
    public Date due_date;
    public Date close_date;
    DisplayedFile file = new DisplayedFile();
}
