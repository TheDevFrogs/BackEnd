package ca.usherbrooke.remisetravaux.files;

import ca.usherbrooke.remisetravaux.business.DatabaseFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LocalFileWriter implements FileDataAccess {

    private static final String initialPath = "Storage/";

    @Override
    public void WriteFile(DatabaseFile fileInfo, byte[] data) throws IOException {
        //TODO implementer les mutex
        File file = new File(initialPath + fileInfo.path, fileInfo.name + fileInfo.extension);

        if (!file.exists()) {
            Files.createDirectories(Paths.get(initialPath + fileInfo.path));
            file.createNewFile();
        }

        FileOutputStream fop = new FileOutputStream(file,false);

        fop.write(data);
        fop.flush();
        fop.close();
    }

    @Override
    public byte[] ReadFile(DatabaseFile fileInfo) throws IOException {
        File file = new File(initialPath + fileInfo.path, fileInfo.name);
        FileInputStream fip = new FileInputStream(file);

        return fip.readAllBytes();
    }
}