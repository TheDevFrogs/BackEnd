package ca.usherbrooke.remisetravaux.dto;

import java.util.ArrayList;
import java.util.List;

public class Sessions {
    public Sessions(){
        Etudiant = new ArrayList<>();
        Enseignant = new ArrayList<>();
    }
    public List<Session> Etudiant;
    public List<Session> Enseignant;
}
