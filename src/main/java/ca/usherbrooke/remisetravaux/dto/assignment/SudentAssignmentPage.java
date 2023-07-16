package ca.usherbrooke.remisetravaux.dto.assignment;


import java.util.Date;
import java.util.List;

public class SudentAssignmentPage {
    public int assignment_id;
    public String name;
    public String description;
    public Date available_date;
    public Date due_date;
    public Date close_date;
    public int joinedFileId;
    //Coupure de fonctionalite les team members nsont mis en stannd by
    //public List<User> teamMember;

    public List<AssignmentFile> handed_work_file;
    public List<AssignmentFile> corrected_work_file;
}
