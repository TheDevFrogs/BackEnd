package ca.usherbrooke.remisetravaux.business;

import java.util.Date;
import java.util.List;

public class Assignment {
    public int id_assignment;
    public String name;
    public String description;
    public Date du_date;
    public Date close_date;
    public Date availableDate;
    public int team_size;
    public List<File> files;
    public List<Team> teams;
    public Group group;
}
