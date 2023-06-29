package ca.usherbrooke.remisetravaux.dto;

import java.util.ArrayList;
import java.util.List;

public class Sessions {
    public Sessions(){
        Etudiant = new ArrayList<>();
        Enseignant = new ArrayList<>();
    }
    public List<String> Etudiant;
    public List<String> Enseignant;
}
