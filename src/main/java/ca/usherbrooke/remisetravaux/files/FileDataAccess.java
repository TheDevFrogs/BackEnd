package ca.usherbrooke.remisetravaux.files;

import java.io.File;

import java.io.IOException;

public interface FileDataAccess {
    void WriteFile(String filePath, String fileName, byte[] data) throws IOException;
    byte[] ReadFile(String filePath, String fileName) throws IOException ;
}
