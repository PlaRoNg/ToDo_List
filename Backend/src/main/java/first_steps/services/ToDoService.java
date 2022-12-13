package com.efsauto.erste_schritte.service;

import com.efsauto.erste_schritte.exception.ToDoException;
import com.efsauto.erste_schritte.models.ToDo;
import com.efsauto.erste_schritte.repositories.ToDoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ToDoService {

    private static final Logger LOG = LoggerFactory.getLogger(ToDoService.class);

    private final ToDoRepository toDoRepository;


    public ToDoService(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;

    }

    /**
     * get all objects
     *
     * @return a list with all todos
     */
    public List<ToDo> getAllToDos() throws ToDoException {

        return toDoRepository.findAll();
    }


    /**
     * get one specific ToDo defined
     *
     * @param id: is the todo id
     * @return the todo Object corresponding to the id
     */
    public ToDo getSingleToDo(Long id) throws ToDoException {

        Optional<ToDo> myToDo = toDoRepository.findById(id);

        if (!myToDo.isPresent()) {
            LOG.debug("The toDo doesn't exist in the list");
            throw new ToDoException("SORRY.... The toDo entity with the id " + id + " doesn't exist", HttpStatus.NOT_FOUND);
        }

        return myToDo.get();
    }


    /**
     * create/add a new todo in the list
     *
     * @param newToDo : the new todo element to be added. It's a single object
     * @return : new toDo Object
     */
    // new ToDo() verwenden mit default Werten ==> DONE
    public ToDo createToDo(ToDo newToDo) throws ToDoException {

        ToDo analysedToDo = createToDoInputTesting(newToDo);
        checkIfTitleIsAlreadyPresent(analysedToDo);

        return toDoRepository.saveAndFlush(analysedToDo);
    }


    /**
     * update todo
     *
     * @param id         : is the todo ID
     * @param updateToDo : a new todo with the element to be updated
     * @return :  updated toDo object
     */

    public ToDo updateToDo(Long id, ToDo updateToDo) throws ToDoException {

        //ID darf nicht eingegeben werden
        if (updateToDo.getId() != null) {
            LOG.debug("Id can't be set");
            throw new ToDoException("SORRY... Id can't be set.Please remove it", HttpStatus.FORBIDDEN);

        }
        if (((updateToDo.getTitle() == null) || updateToDo.getTitle().trim().equalsIgnoreCase("")) && updateToDo.getChecked() == null) {
            LOG.debug("No relevant information");
            throw new ToDoException("SORRY....No relevant information. Please give an \"title\" and/or a \"checked\"", HttpStatus.BAD_REQUEST);
        }

        // prüfen ob id und Key existieren
        ToDo toDoToBeUpdated = this.getSingleToDo(id);

        if ((updateToDo.getTitle() != null) && !(updateToDo.getTitle().trim().equalsIgnoreCase(toDoToBeUpdated.getTitle().trim()))) {

            if (toDoRepository.existsByTitle(updateToDo.getTitle().trim())) {
                LOG.debug("Title already exists ");
                throw new ToDoException("SORRY... Title already exists.Please choose another title", HttpStatus.BAD_REQUEST);
            }
            if (!updateToDo.getTitle().trim().equalsIgnoreCase("")) {
                LOG.debug("The \"title\" for the id {} has been updated", id);

                toDoToBeUpdated.setTitle(updateToDo.getTitle().trim());
            } else {
                LOG.debug("\"title\" is null or unchanged.No update needed");

            }
        }
        if ((updateToDo.getChecked() != null) && !updateToDo.getChecked().equals(toDoToBeUpdated.getChecked())) {
            LOG.debug("The \"checked\" for the id {} has been updated", id);

            toDoToBeUpdated.setChecked(updateToDo.getChecked());
        } else {
            LOG.debug(" \"checked\" is null or unchanged. No update needed");
        }

        return toDoRepository.saveAndFlush(toDoToBeUpdated);

    }


    /**
     * delete a todo defined by it's id
     *
     * @param id: is the todo ID of the toDo to be deleted
     */
    public String deleteToDoById(Long id) throws ToDoException {
        try {
            toDoRepository.deleteById(id);
            return "Delete toDo with id " + id + " - DONE";
        } catch (EmptyResultDataAccessException ex) {
            throw new ToDoException("SORRY.... No ToDo entity with the id " + id, HttpStatus.NOT_FOUND);
        }
    }


    /**
     * check todo if it's unchecked
     *
     * @param id : is the todo ID
     * @return : an updated toDo entity where the key "checked" has a value "true"
     */
    public ToDo checkOrUncheckToDoById(Long id) throws ToDoException {

        // prüfen ob id und Key existieren
        ToDo selectedToDo = this.getSingleToDo(id);
        selectedToDo.setChecked(!selectedToDo.getChecked());

        return toDoRepository.saveAndFlush(selectedToDo);
    }


    /**
     * check all todos if they are unchecked
     *
     * @return
     * @throws ToDoException
     */
    public List<ToDo> checkAllToDos(boolean checked) throws ToDoException {

        // finde alle unchecked toDos und setze sie auf true
        List<ToDo> uncheckToDos = toDoRepository.findByChecked(!checked);
        for (ToDo toDo : uncheckToDos) {
            toDo.setChecked(checked);
        }

        toDoRepository.saveAllAndFlush(uncheckToDos);
        return this.getAllToDos();
    }


    /**
     * Delete all  ToDo entity
     *
     * @return
     * @throws ToDoException
     */
    public String deleteAllToDos() throws ToDoException {

        if (this.getAllToDos().isEmpty()) {
            return "No toDo to delete. The list is empty";
        }

        toDoRepository.deleteAll();
        return "Delete all toDos - DONE";
    }

    /**
     * Delete all checked ToDo entity
     *
     * @param isChecked
     * @return
     * @throws ToDoException
     */
    public String deleteAllCheckedToDos(boolean isChecked) throws ToDoException {

        List<ToDo> checkedToDos = toDoRepository.findByChecked(isChecked);
        if (checkedToDos.isEmpty()) {
            return "No checked toDo to delete.";
        }
        toDoRepository.deleteAll(checkedToDos);
        return "Delete all checked toDos - DONE";
    }


    private ToDo createToDoInputTesting(ToDo newToDo) throws ToDoException {
        //ID darf nicht eingegeben werden
        if (newToDo.getId() != null) {
            LOG.debug("Id can't be set");
            throw new ToDoException("SORRY... Id can't be set.Please remove it", HttpStatus.FORBIDDEN);

        }

        // prüfe ob der Titel null ist oder ein leerer String
        if ((newToDo.getTitle() == null) || newToDo.getTitle().trim().equalsIgnoreCase("")) {

            LOG.debug("The toDo has no title");
            throw new ToDoException("SORRY....ToDo has no title. Please give it a title", HttpStatus.BAD_REQUEST);
        }

        // prüfe ob toDo check ist null und nutze den Kontruktor
        if (newToDo.getChecked() == null) {
            return new ToDo(newToDo.getTitle());

        } else {

            return new ToDo(newToDo.getTitle().trim(), newToDo.getChecked());
        }

    }

    private void checkIfTitleIsAlreadyPresent(ToDo newToDo) throws ToDoException {
        // Prüfen ob der Titlel schon vergeben ist
        if (toDoRepository.existsByTitle(newToDo.getTitle().trim())) {
            LOG.debug("Title already exists ");
            throw new ToDoException("SORRY... Title already exists.Please choose another title", HttpStatus.BAD_REQUEST);
        }
    }

}

