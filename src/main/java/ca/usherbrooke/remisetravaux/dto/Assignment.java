package ca.usherbrooke.remisetravaux.dto;

import ca.usherbrooke.remisetravaux.business.File;
import ca.usherbrooke.remisetravaux.business.Group;
import ca.usherbrooke.remisetravaux.business.Team;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Assignment {
    public int id_assignment;
    public String name;
    public String description;
    public Date du_date;
    public Date close_date;
    public Date available_date;
    //public List<File> files;
    //public List<Team> teams;
    public Group id_group;

}
