package ru.stegnin.virtualbox.server.repository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface FileRepository {

    boolean create(String fileName);

    boolean remove(String fileName);

    boolean rename(String newName, String oldName);

    @NotNull List<String> getAllFiles();

    boolean exist(String fileName);

    @Nullable byte[] readData(String fileName);
}
