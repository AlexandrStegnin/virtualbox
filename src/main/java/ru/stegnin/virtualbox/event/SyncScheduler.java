package ru.stegnin.virtualbox.event;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.stegnin.virtualbox.server.repository.FileRepository;
import ru.stegnin.virtualbox.server.repository.FolderRepository;
import ru.stegnin.virtualbox.settings.security.SecurityUserHelper;

import java.util.List;

@Component
public class SyncScheduler {

    private FolderRepository folderRemoteRepository;
    private FolderRepository folderLocalRepository;
    private FileRepository fileRepository;
    private boolean running = false;
    private SyncEvent syncEvent;
    private SyncEventListener syncEventListener;
    private SecurityUserHelper userHelper;

    public SyncScheduler(@Qualifier("folderRemoteService") FolderRepository folderRemoteRepository,
                         @Qualifier("folderLocalService") FolderRepository folderLocalRepository,
                         FileRepository fileRepository,
                         SyncEvent syncEvent,
                         SyncEventListener syncEventListener,
                         SecurityUserHelper userHelper) {
        this.folderRemoteRepository = folderRemoteRepository;
        this.folderLocalRepository = folderLocalRepository;
        this.fileRepository = fileRepository;
        this.syncEvent = syncEvent;
        this.syncEventListener = syncEventListener;
        this.userHelper = userHelper;
    }

    @Scheduled(cron = "* 0/2 * * * ?")
    public void sync() {
        if (!running) {
            syncEvent.addObserver(syncEventListener);
            running = true;
            List<String> remoteFolders = folderRemoteRepository.findAllFolders();
            List<String> localFolders = folderLocalRepository.findAllFolders();
            syncFolders(remoteFolders, localFolders);
            syncFolders(localFolders, remoteFolders);
            running = false;
        }
    }

    private void syncFolders(List<String> sourceFolders, List<String> destFolders) {
        sourceFolders.forEach(sf -> {
            if (!destFolders.contains(sf)) {
                syncEvent.syncFolders(sf);
            }
        });
    }
}
