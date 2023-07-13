package ca.usherbrooke.remisetravaux.business;

import ca.usherbrooke.remisetravaux.dto.Assignment;

import java.util.List;

public class Group {
    public int id_group;
    public int group_number;
    public List<Assignment> assignments;
    public List<Membre> membres;

    public Classes aclass;
}
