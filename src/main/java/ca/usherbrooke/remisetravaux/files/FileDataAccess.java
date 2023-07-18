package ca.usherbrooke.remisetravaux.files;

import ca.usherbrooke.remisetravaux.business.DatabaseFile;

import java.io.File;

import java.io.IOException;

public interface FileDataAccess {
    void WriteFile(DatabaseFile fileInfo, byte[] data) throws IOException;
    byte[] ReadFile(DatabaseFile file) throws IOException ;
}
