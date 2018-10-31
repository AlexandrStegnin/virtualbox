package ru.stegnin.virtualbox.server.service.local;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.stegnin.virtualbox.server.repository.FolderRepository;
import ru.stegnin.virtualbox.settings.security.SecurityUserHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Service
public class FolderLocalService implements FolderRepository {

    private final SecurityUserHelper userHelper;

    @Autowired
    public FolderLocalService(SecurityUserHelper userHelper) {
        this.userHelper = userHelper;
    }

    @Override
    public boolean create(String folderName) {
        final File file = new File(folderName);
        return file.mkdirs();
    }

    @Override
    public boolean remove(String folderName) {
        if (folderName == null || folderName.isEmpty()) return false;
        final File file = new File(getRoot(), folderName);
        return file.delete();
    }

    @Override
    public boolean rename(String newName, String oldName) {
        if (!StringUtils.hasText(newName) || !StringUtils.hasText(oldName)) return false;
        File oldDir = new File(getRoot(), oldName);
        File newDir = new File(getRoot(), newName);
        return oldDir.renameTo(newDir);
    }

    @Override
    public List<String> findAllFolders() {
        final File root = getRoot();
        final String[] directories = root.list((current, name) -> new File(current, name).isDirectory());
        return Arrays.asList(directories);
    }

    @NotNull
    private File getRoot() {
        final Path rootDir = Paths.get(userHelper.getUserSyncFolder());
        if (Files.notExists(rootDir)) {
            try {
                Files.createDirectories(rootDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rootDir.toFile();
    }
}
