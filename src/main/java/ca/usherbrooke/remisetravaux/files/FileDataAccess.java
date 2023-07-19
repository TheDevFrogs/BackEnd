package ca.usherbrooke.remisetravaux.files;

import ca.usherbrooke.remisetravaux.business.DatabaseFile;

import java.io.File;

import java.io.IOException;
import java.io.InputStream;

public interface FileDataAccess {
    void WriteFile(DatabaseFile fileInfo, byte[] data) throws IOException;
    InputStream ReadFile(DatabaseFile file) throws IOException ;
}
