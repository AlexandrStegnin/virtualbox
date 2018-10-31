package ru.stegnin.virtualbox.event;

import org.springframework.stereotype.Component;

import java.util.Observable;

@Component
public class SyncEvent extends Observable {
    public void syncFiles(String path) {
        setChanged();
        notifyObservers(path);
    }

    public void syncFolders(String path) {
        setChanged();
        notifyObservers(path);
    }
}
