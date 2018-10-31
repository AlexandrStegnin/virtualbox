package ru.stegnin.virtualbox.event;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.stegnin.virtualbox.server.repository.FileRepository;
import ru.stegnin.virtualbox.server.repository.FolderRepository;
import ru.stegnin.virtualbox.settings.security.SecurityUserHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;

@Async
@Service
public class SyncEventListener implements Observer {

    private FolderRepository folderRepository;

    private FileRepository fileRepository;

    private Path path;

    private SecurityUserHelper userHelper;

    @Autowired
    public SyncEventListener(@Qualifier("folderLocalService") FolderRepository folderRepository, FileRepository fileRepository,
                             SecurityUserHelper userHelper) {
        this.folderRepository = folderRepository;
        this.fileRepository = fileRepository;
        this.userHelper = userHelper;
    }

    @Override
    public void update(Observable o, Object newPath) {
        this.path = Paths.get(getRoot() + "/" + newPath);
        if (Files.notExists(path, LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @NotNull
    private Path getRoot() {
        final Path rootDir = Paths.get(userHelper.getUserSyncFolder());
        if (Files.notExists(rootDir)) {
            try {
                Files.createDirectories(rootDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rootDir;
    }
}
