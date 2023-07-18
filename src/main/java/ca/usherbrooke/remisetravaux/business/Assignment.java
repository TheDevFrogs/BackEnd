package ca.usherbrooke.remisetravaux.business;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public Integer id_file = null;

    public Assignment(){}
    //This will read everything except the id's for security reasons
    public Assignment(MultipartFormDataInput input) throws IOException, ParseException {

        this.description = input.getFormDataPart("description", String.class, null);
        this.name = input.getFormDataPart("name", String.class, null);
        this.due_date = fromStringToDate(input.getFormDataPart("due_date", String.class, null));
        this.close_date = fromStringToDate(input.getFormDataPart("close_date", String.class, null));
        this.available_date = fromStringToDate(input.getFormDataPart("available_date", String.class, null));
        //TODO CHANGE THIS
        this.team_size = 1;
        this.setdefaultValues();
    }


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

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private Date fromStringToDate(String date) throws ParseException {

        if (date != null && !date.isEmpty())
            return dateFormat.parse(date);
        else
            return null;
    }
}
