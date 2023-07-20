package ca.usherbrooke.remisetravaux.files;

import ca.usherbrooke.remisetravaux.business.DatabaseFile;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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
    public InputStream ReadFile(DatabaseFile fileInfo) throws IOException {
        File file = new File(initialPath + fileInfo.path, fileInfo.name + fileInfo.extension);
        FileInputStream fip = new FileInputStream(file);

        return fip;
    }

    @Override
    public StreamingOutput getFolderAsZip(String path) throws IOException {
        File dir = new File(initialPath + path);
        List<String> files = populateFilesList( dir);


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(byteArrayOutputStream);
        for(String filePath : files){
            //for ZipEntry we need to keep only relative file path, so we used substring on absolute path
            ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length() + 1, filePath.length()));
            zos.putNextEntry(ze);
            //read the file and write to ZipOutputStream
            FileInputStream fis = new FileInputStream(filePath);

            zos.write(fis.readAllBytes());

            zos.closeEntry();
            fis.close();
        }
        zos.finish();
        zos.flush();
        zos.close();
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();

        return output -> output.write(byteArrayOutputStream.toByteArray());
    }

    private List<String>  populateFilesList(File dir) throws IOException {
        List<String> filesListInDir = new ArrayList<String>();
        File[] files = dir.listFiles();
        for(File file : files){
            if(file.isFile()) filesListInDir.add(file.getAbsolutePath());
            else filesListInDir.addAll(populateFilesList(file));
        }
        return filesListInDir;
    }

    public static byte[] getFileData(InputPart inputPart) throws IOException {
        MultivaluedMap<String, String> header = inputPart.getHeaders();

        // convert the uploaded file to inputstream
        InputStream inputStream = inputPart.getBody(InputStream.class, null);
        return IOUtils.toByteArray(inputStream);
    }
}