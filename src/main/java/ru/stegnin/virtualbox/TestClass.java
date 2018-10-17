package ru.stegnin.virtualbox;

import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;

import javax.jcr.*;
import java.net.MalformedURLException;

public class TestClass {
    final private static String URL = "http://localhost:8282/rmi";

    public static void main(String[] args) {
        try {
            final Repository repository = new URLRemoteRepository(URL);
            final Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));

            Workspace workspace = session.getWorkspace();
            String newWs = "newWorkspace";
//            workspace.createWorkspace(newWs);
            String[] workspaces = workspace.getAccessibleWorkspaceNames();
            for (String ws :
                    workspaces) {
                System.out.println(ws);
            }
            workspace.deleteWorkspace(newWs);
            for (String ws :
                    workspaces) {
                System.out.println(ws);
            }
////            UserManager um = ((JackrabbitSession) session).getUserManager();
////            um.createUser("guest", "guest");
////            session.save();
//
//            final Node root = session.getRootNode();
//            final NodeIterator iterator = root.getNodes();
//            StringBuilder pref = new StringBuilder("-");
//            while (iterator.hasNext()) {
//                final Node node = iterator.nextNode();
//                if (node.hasNodes()) pref.append("-");
//                System.out.println(pref + node.getName());
//            }

//            try {
//                Node root = session.getRootNode();
//
//                // Store content
//                Node hello = root.addNode("hello");
//                Node world = hello.addNode("world");
//                world.setProperty("message", "Hello, World!");
//                session.save();
//
//                // Retrieve content
//                Node node = root.getNode("hello/world");
//                System.out.println(node.getPath());
//                System.out.println(node.getProperty("message").getString());
//
//                // Remove content
////                root.getNode("hello").remove();
////                session.save();
//            } finally {
//                session.logout();
//            }
//            try {
//                String user = session.getUserID();
//                String name = repository.getDescriptor(Repository.REP_NAME_DESC);
//                System.out.println(
//                        "Logged in as " + user + " to a " + name + " repository.");
//            } finally {
//                session.logout();
//            }

//Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJndWVzdCIsImV4cCI6MTUzOTE3Mjk4OCwiYXV0aG9yaXRpZXMiOiJST0xFX0dVRVNUIn0.NRLrbfRw2IVYFUFOL_5c7Y2iAVvaT4daoELJwIEwL4T-ikorSWPCwWMcW6bHcE14BXME2zCvjZklRTS4O0rqvA

        } catch (RepositoryException | MalformedURLException e) {
            e.printStackTrace();
        }
//        Base64.Decoder decoder = Base64.getUrlDecoder();
//        String src = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJndWVzdCIsImV4cCI6MTUzOTE3Mjk4OCwiYXV0aG9yaXRpZXMiOiJST0xFX0dVRVNUIn0.NRLrbfRw2IVYFUFOL_5c7Y2iAVvaT4daoELJwIEwL4T-ikorSWPCwWMcW6bHcE14BXME2zCvjZklRTS4O0rqvA";
//        String[] parts = src.substring("Bearer ".length()).split("\\.");
//        System.out.println(parts[0]);
//        System.out.println(parts[1]);
//        System.out.println(parts[2]);

    }
}
