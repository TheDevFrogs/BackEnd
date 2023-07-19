package ca.usherbrooke.remisetravaux.files;

import ca.usherbrooke.remisetravaux.business.DatabaseFile;

import javax.ws.rs.core.StreamingOutput;
import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileDataAccess {
    void WriteFile(DatabaseFile fileInfo, byte[] data) throws IOException;
    InputStream ReadFile(DatabaseFile file) throws IOException ;

    StreamingOutput getFolderAsZip(String path) throws IOException;
}
