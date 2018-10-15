package ru.stegnin.virtualbox.server.service;

import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import ru.stegnin.virtualbox.server.repository.AbstractServerRepository;
import ru.stegnin.virtualbox.server.repository.FolderRepository;
import ru.stegnin.virtualbox.settings.support.Constants;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.SimpleCredentials;
import java.net.MalformedURLException;

@Service
public class FolderRemoteService extends AbstractServerRepository implements FolderRepository {
    @Override
    public Node create(String folderName) throws RepositoryException {
        if (session == null) {
            try {
                openSession("admin", "admin");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        folderName = folderName.trim();
        Node root = session.getRootNode();
        Node newNode = root.addNode(folderName, Constants.JR_NT_FOLDER);
        session.save();
        return newNode;
    }

    @Override
    public void remove(String folderName) throws RepositoryException {
        Node folderToDelete = findByFolderName(folderName);
        if (folderToDelete != null) {
            folderToDelete.remove();
            session.save();
        }
    }

    @Nullable
    @Override
    public Node update(String newName, String oldName) throws RepositoryException {
        Node oldFolder = findByFolderName(oldName);
        if (oldFolder != null) {

            oldFolder.getSession().move(oldFolder.getPath(), oldFolder.getParent().getPath() + newName);

            session.save();
        }
        return null;
    }

    @Nullable
    @Override
    public Node findByFolderName(String folderName) throws RepositoryException {
        if (session == null) {
            try {
                openSession("admin", "admin");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        Node result = null;
        final Node root = session.getRootNode();
        final NodeIterator iterator = root.getNodes();
        while (iterator.hasNext()) {

            Node next = iterator.nextNode();
            if (folderName.equalsIgnoreCase(next.getName())) {
                result = next;
            }
        }
        return result;
    }

    private void openSession(String login, @NotNull String password) throws MalformedURLException, RepositoryException {
        repository = new URLRemoteRepository(Constants.SERVER_URL);
        session = repository.login(new SimpleCredentials(login, password.toCharArray()));
    }
}
