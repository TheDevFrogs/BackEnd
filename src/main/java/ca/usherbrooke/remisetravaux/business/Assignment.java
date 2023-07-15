package ca.usherbrooke.remisetravaux.business;

import java.util.Calendar;
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
        //AvailableDate 168h
        //Fermeture 72h

        if (close_date == null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(due_date);
            calendar.add(Calendar.HOUR_OF_DAY, 72);
            close_date =  calendar.getTime();
        }

        if (available_date == null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(due_date);
            calendar.add(Calendar.HOUR_OF_DAY, 168);
            available_date =  calendar.getTime();
        }
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
