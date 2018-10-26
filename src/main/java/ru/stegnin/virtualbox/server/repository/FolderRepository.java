package ru.stegnin.virtualbox.server.repository;

import java.util.List;

public interface FolderRepository {
    String create(String folderName);

    void remove(String folderName);

    void rename(String newName, String oldName);

    List<String> findAllFolders();


}
