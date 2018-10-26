package ru.stegnin.virtualbox.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stegnin.virtualbox.api.repository.AuthRepository;
import ru.stegnin.virtualbox.server.service.remote.FolderRemoteService;
import ru.stegnin.virtualbox.settings.support.Constants;
import ru.stegnin.virtualbox.settings.support.GenericResponse;

import java.util.logging.Logger;

@RestController
@RequestMapping(value = Constants.API_JR_WORKSPACE)
public class FolderController {
    private final FolderRemoteService folderRemoteService;
    private final AuthRepository auth;
    private Logger logger = Logger.getLogger(FolderController.class.getName());
    private GenericResponse message;

    @Autowired
    public FolderController(FolderRemoteService folderRemoteService, AuthRepository auth) {
        this.folderRemoteService = folderRemoteService;
        this.auth = auth;
    }

    /**
     * Создать папку
     *
     * @param folderName - название папки
     * @return - response с сообщением
     */
    @PostMapping
    public ResponseEntity
    create(@RequestBody String folderName) {
        folderRemoteService.create(folderName);
        message = new GenericResponse.Builder().withMessage("Folder with name: " + folderName + " created").build();
        logger.info(message.getMessage());
        return ResponseEntity.created(auth.getUri(this.getClass(), "create", (Object) folderName)).body(folderName);
    }

    /**
     * Изменить название папки
     *
     * @param folderName - название папки
     * @return - response с сообщением
     */
    @PutMapping(value = Constants.API_JR_FOLDERS_FOLDER_NAME, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable(Constants.API_JR_FOLDER_NAME) String folderName, @RequestParam(name = "newFolderName") String newFolderName) {
        folderRemoteService.rename(newFolderName, folderName);
        message = new GenericResponse.Builder().withMessage(
                String.format("Folder with name '%s' successful updated. New name is '%s'.", folderName, newFolderName)).build();
        logger.info(message.getMessage());
        return ResponseEntity.ok(message.getMessage());
    }

    /**
     * Удалить папку по имени
     *
     * @param folderName - имя папки
     * @return - response с сообщением
     */
    @DeleteMapping(value = Constants.API_JR_FOLDERS_FOLDER_NAME)
    public ResponseEntity remove(@PathVariable(Constants.API_JR_FOLDER_NAME) String folderName) {
        folderRemoteService.remove(folderName);
        message = new GenericResponse.Builder().withMessage(String.format("Folder with name '%s' deleted successful.", folderName)).build();
        logger.info(message.getMessage());
        return ResponseEntity.ok().body(message.getMessage());
    }
}
