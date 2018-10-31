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

    // TODO: 31.10.2018 Брать время синхронизации из настроек пользователя (User.settings)

    @Scheduled(cron = "* 0/2 * * * ?")
    public void sync() {
        if (!running) {
            syncEvent.addObserver(syncEventListener);
            List<String> remoteFolders = folderRemoteRepository.findAllFolders();
            List<String> localFolders = folderLocalRepository.findAllFolders();
            if ((remoteFolders.size() != localFolders.size()) || !remoteFolders.containsAll(localFolders)) {
                running = true;
                syncFolders(remoteFolders, localFolders);
                syncFolders(localFolders, remoteFolders);
                running = false;
            }
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
