package ca.usherbrooke.remisetravaux.business;

import java.util.List;
import java.util.Date;

public class Session {
    public int id_session;
    public Date start_date;
    public Date end_date;
    public String name;
    public List<Class> classes;
}
