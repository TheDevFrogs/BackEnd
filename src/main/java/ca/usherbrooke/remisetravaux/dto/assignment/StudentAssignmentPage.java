package ca.usherbrooke.remisetravaux.dto.assignment;


import ca.usherbrooke.remisetravaux.dto.DisplayedFile;

import java.util.Date;
import java.util.List;

public class StudentAssignmentPage {
    public int id_assignment;
    public String name;
    public String description;
    public Date available_date;
    public Date due_date;
    public Date close_date;

    public DisplayedFile file = new DisplayedFile();
    //Coupure de fonctionalite les team members nsont mis en stannd by
    //public List<User> teamMember;

    public List<AssignmentFile> handed_work_files;
    public List<AssignmentFile> corrected_work_files;
}