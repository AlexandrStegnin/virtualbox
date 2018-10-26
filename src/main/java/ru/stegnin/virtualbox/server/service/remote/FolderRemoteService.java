package ru.stegnin.virtualbox.server.service.remote;

import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import ru.stegnin.virtualbox.server.repository.AbstractServerRepository;
import ru.stegnin.virtualbox.server.repository.FolderRepository;
import ru.stegnin.virtualbox.settings.annotation.Loggable;
import ru.stegnin.virtualbox.settings.support.Constants;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.SimpleCredentials;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@Loggable
@Service
public class FolderRemoteService extends AbstractServerRepository implements FolderRepository {

    @Nullable
    @Override
    public boolean create(String folderName) {
        if (session == null) {
            try {
                openSession("admin", "admin");
            } catch (MalformedURLException | RepositoryException e) {
                e.printStackTrace();
            }
        }
        folderName = folderName.trim();
        Node newNode;
        String nodeName = null;
        Node root;
        try {
            root = session.getRootNode();
            newNode = root.addNode(folderName, Constants.JR_NT_FOLDER);
            session.save();
            nodeName = newNode.getName();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        return nodeName != null;
    }

    @Override
    public boolean remove(String folderName) {
        Node folderToDelete;
        try {
            folderToDelete = findByName(folderName);
            if (folderToDelete != null) {
                folderToDelete.remove();
                session.save();
                return true;
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean rename(String newName, String oldName) {
        Node oldFolder;
        try {
            oldFolder = findByName(oldName);
            if (oldFolder != null) {
                oldFolder.getSession().move(oldFolder.getPath(), oldFolder.getParent().getPath() + newName);
                session.save();
                return true;
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void openSession(String login, @NotNull String password) throws MalformedURLException, RepositoryException {
        repository = new URLRemoteRepository(Constants.SERVER_URL);
        session = repository.login(new SimpleCredentials(login, password.toCharArray()));
    }

    @Nullable
    public Node findByName(String nodeName) throws RepositoryException {
        final Node root = session.getRootNode();
        final NodeIterator iterator = root.getNodes();
        Node result = null;
        while (iterator.hasNext()) {
            Node next = iterator.nextNode();
            if (nodeName.equalsIgnoreCase(next.getName())) {
                result = next;
            }
        }
        return result;
    }

    @Override
    public List<String> findAllFolders() {
        List<String> result = new ArrayList<>();
        try {
            final Node root = session.getRootNode();
            final NodeIterator iterator = root.getNodes();
            while (iterator.hasNext()) {
                Node next = iterator.nextNode();
                result.add(next.getName());
            }
            return result;
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return null;
    }
}
