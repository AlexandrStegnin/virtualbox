package ru.stegnin.virtualbox.server.repository;

import java.util.List;

public interface FolderRepository {
    boolean create(String folderName);

    boolean remove(String folderName);

    boolean rename(String newName, String oldName);

    List<String> findAllFolders();

}
