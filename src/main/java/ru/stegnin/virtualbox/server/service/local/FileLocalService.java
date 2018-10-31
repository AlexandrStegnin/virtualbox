package ru.stegnin.virtualbox.server.service.local;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.stegnin.virtualbox.server.repository.FileRepository;
import ru.stegnin.virtualbox.settings.security.SecurityUserHelper;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Service
public class FileLocalService implements FileRepository {
    private final SecurityUserHelper userHelper;

    @Autowired
    public FileLocalService(SecurityUserHelper userHelper) {
        this.userHelper = userHelper;
    }

    @Override
    public boolean create(File root, String fileName) {
        final File file = new File(root, fileName);
        return file.mkdirs();
    }

    @Override
    public boolean remove(String fileName) {
        if (fileName == null || fileName.isEmpty()) return false;
        final File file = new File(getRoot(), fileName);
        return file.delete();
    }

    @Override
    public boolean rename(String newName, String oldName) {
        if (!StringUtils.hasText(newName) || !StringUtils.hasText(oldName)) return false;
        File oldFile = new File(getRoot(), oldName);
        File newFile = new File(getRoot(), newName);
        return oldFile.renameTo(newFile);
    }

    @Override
    public @NotNull List<String> getAllFiles() {
        final File root = getRoot();
        final String[] files = root.list((current, name) -> new File(current, name).isFile());
        return Arrays.asList(files);
    }

    @Override
    public boolean exist(String fileName) {
        final File existingFile = new File(getRoot(), fileName);
        return existingFile.exists();
    }

    @Nullable
    @Override
    @SneakyThrows
    public byte[] readData(String fileName) {
        if (!StringUtils.hasText(fileName)) return new byte[]{};
        final File file = new File(getRoot(), fileName);
        return Files.readAllBytes(file.toPath());
    }

    @NotNull
    private File getRoot() {
        return new File(userHelper.getUserSyncFolder());
    }
}
