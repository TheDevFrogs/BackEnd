package ca.usherbrooke.remisetravaux.business.session;
import java.util.Date;

public class AssigmentPreview {
    //We pass the element in the assignment to set the status to the correct one
    //It is way easier to do it in java than in sql
    public AssigmentPreview(Date available_date, Date due_date, Date close_date, Date first_handed_work){

        if (available_date.after(new Date()))
            status = "scheduled";
        else if (first_handed_work == null && due_date.after(new Date()))
            status = "todo";
        else if (first_handed_work != null && first_handed_work.before(due_date))
            status = "handed";
        else if (first_handed_work != null && first_handed_work.after(due_date) && first_handed_work.before(close_date))
            status = "late";
        else
            status = "not_handed";
    }
    public int id_assignment;
    public String name;
    public String status;
}