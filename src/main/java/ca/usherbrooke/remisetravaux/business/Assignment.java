package ca.usherbrooke.remisetravaux.business;

import java.util.Date;

public class Assignment {
    public int id_assignment;
    public int id_group;
    public String name;
    public String description;
    public Date due_date;
    public Date close_date;
    public Date available_date;
    public int team_size;

    public void setdefaultValues(){

    }

    public int getId_assignment(){return id_assignment;}
    public int getId_group(){return id_group;}
    public String getName(){return name;}
    public String getDescription(){return description;}
    public Date getDue_date(){return due_date;}
    public Date getClose_date(){return close_date;}
    public Date getAvailable_date(){return available_date;}
    public int getTeam_size(){return team_size;}
}
