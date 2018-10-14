package ru.stegnin.virtualbox.server.repository;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public interface FolderRepository {
    Node create(String folderName) throws RepositoryException;

    Node remove(String folderName) throws RepositoryException;

    Node update(String newName, String oldName) throws RepositoryException;

    Node findByFolderName(String folderName) throws RepositoryException;
}
