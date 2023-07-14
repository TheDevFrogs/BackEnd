package ca.usherbrooke.remisetravaux.files;

import ca.usherbrooke.remisetravaux.business.File;

public interface FileDateAccess {
    void WriteFile(File file, byte[] data);
    void ReadFile(File file);
}
