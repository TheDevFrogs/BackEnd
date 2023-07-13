package ca.usherbrooke.remisetravaux.dto;

import ca.usherbrooke.remisetravaux.business.File;
import ca.usherbrooke.remisetravaux.business.Group;
import ca.usherbrooke.remisetravaux.business.Team;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Assignment {
    public int id_assignment;
    public String assignmentname;
    public String description;
    public Date due_date;
    public Date close_date;
    public Date available_date;
    public int id_group;
    //public List<File> files;
    //public List<Team> teams;
}