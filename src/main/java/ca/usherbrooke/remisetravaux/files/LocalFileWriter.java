package ca.usherbrooke.remisetravaux.files;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LocalFileWriter implements FileDataAccess {

    private static final String initialPath = "Storage/";

    @Override
    public void WriteFile(String filePath, String fileName, byte[] data) throws IOException {
        //TODO implementer les mutex
        File file = new File(initialPath + filePath, fileName);

        if (!file.exists()) {
            Files.createDirectories(Paths.get(initialPath + filePath));
            file.createNewFile();
        }

        FileOutputStream fop = new FileOutputStream(file);

        fop.write(data);
        fop.flush();
        fop.close();
    }

    @Override
    public byte[] ReadFile(String filePath, String fileName) throws IOException {
        File file = new File(initialPath + filePath, fileName);
        FileInputStream fip = new FileInputStream(file);

        return fip.readAllBytes();
    }
}