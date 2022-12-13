package com.efsauto.erste_schritte.Controller;


import com.efsauto.erste_schritte.exception.ToDoExceptionHandler;
import com.efsauto.erste_schritte.exception.ToDoException;
import com.efsauto.erste_schritte.models.ToDo;
import com.efsauto.erste_schritte.service.ToDoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"ToDo"})
@RestController
@RequestMapping(value = "/ToDo")
public class ToDoController {

    private static final Logger LOG = LoggerFactory.getLogger(ToDoController.class);

    private final ToDoService toDoService;

    private final ToDoExceptionHandler toDoExceptionHandler;


    public ToDoController(ToDoService toDoService,
                          ToDoExceptionHandler toDoExceptionHandler) {
        this.toDoService = toDoService;
        this.toDoExceptionHandler = toDoExceptionHandler;
    }


    /**
     * get all ToDos
     *
     * @return a list with all todos
     */
    @Operation(summary = "Get all ToDos")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All ToDos found"),
            @ApiResponse(code = 404, message = "ToDos not found"),
            @ApiResponse(code = 500, message = "An error occurred")
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getAllToDos() {
        try {
            LOG.debug("Try to get all toDos");

            List<ToDo> toDoList = toDoService.getAllToDos();
            LOG.debug("Get all toDos - Done");

            return ResponseEntity.ok(toDoList);

        } catch (ToDoException ex) {
            return toDoExceptionHandler.handleToDoException(ex);
        }
    }


    /**
     * Get a single toDO by ID
     *
     * @param id : is the todo ID
     * @return the todo Object corresponding to the ID
     */
    @Operation(summary = "Get ToDO by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "todo found"),
            @ApiResponse(code = 404, message = "todo not found"),
            @ApiResponse(code = 500, message = "An error occurred")
    })
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<?> getSingleToDo(@ApiParam(value = "id for the toDo to be retrieve", required = true) @PathVariable Long id) {
        try {

            LOG.debug("Try to get toDo with id {}", id);

            ToDo toDo = toDoService.getSingleToDo(id);
            LOG.debug("Get toDo with id {} - Done", id);

            return ResponseEntity.ok(toDo);

        } catch (ToDoException ex) {
            return toDoExceptionHandler.handleToDoException(ex);
        }
    }


    /**
     * Add toDo to the List
     *
     * @param newToDo : the new toDo element to be added
     * @return :
     */
    @Operation(summary = "Add toDo to the List")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful: New toDo created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 403, message = "Forbidden: Id not allowed to be set or Title already exists"),
            @ApiResponse(code = 500, message = "Server error: An error occurred")
    })
    @PostMapping(produces = "application/json")
    public ResponseEntity<?> createToDo(@RequestBody ToDo newToDo) {
        try {
            LOG.debug("Try to create a new toDo element with the name:{}", newToDo.getTitle());

            ToDo toDo = toDoService.createToDo(newToDo);
            LOG.debug("Create new toDo - Done");

            return ResponseEntity.status(HttpStatus.CREATED).body(toDo);
        } catch (ToDoException ex) {
            return toDoExceptionHandler.handleToDoException(ex);
        }
    }


    /**
     * Update ToDO by ID
     *
     * @param updateToDo : a new todo with the element to be updated
     * @param id         :      is the todo ID
     * @return
     */

    @Operation(summary = "Update ToDO by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "toDo successfully updated"),
            @ApiResponse(code = 400, message = "Bad request: no new or relevant information for the selected id"),
            @ApiResponse(code = 404, message = "toDO Not Found"),
            @ApiResponse(code = 500, message = "Server error: An error occurred")
    })
    @PutMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<?> updateToDo(@RequestBody ToDo updateToDo, @PathVariable Long id) {
        try {

            LOG.debug("Try to update toDo element with id {}", id);

            ToDo updatedToDo = toDoService.updateToDo(id, updateToDo);
            LOG.debug("toDo  with id {} updated", id);

            return ResponseEntity.ok(updatedToDo);

        } catch (ToDoException ex) {
            return toDoExceptionHandler.handleToDoException(ex);
        }
    }

    /**
     * Delete ToDo by ID
     *
     * @param id: is the todo ID that should be deleted
     * @return
     */
    @Operation(summary = "Delete ToDo by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "toDo deleted"),
            @ApiResponse(code = 404, message = "toDO Not Found"),
            @ApiResponse(code = 500, message = "Server error: An error occurred")
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteToDoById(@PathVariable Long id) {
        try {

            LOG.debug("Try to delete toDo element with id {}", id);

            String deleteMessage = toDoService.deleteToDoById(id);
            LOG.debug("toDo with id {} deleted", id);

            return ResponseEntity.ok().build();

        } catch (ToDoException ex) {
            return toDoExceptionHandler.handleToDoException(ex);
        }
    }


    /**
     * Select checkbox for the toDo by ID
     *
     * @param id : is the todo ID
     * @return : ResponseEntity
     */
    @Operation(summary = "Check or uncheck ToDo by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "\"check\" successfully updated"),
            @ApiResponse(code = 404, message = "toDO Not Found"),
            @ApiResponse(code = 500, message = "Server error: An error occurred")
    })
    @PutMapping(path = "/check/{id}", produces = "application/json")
    public ResponseEntity<?> checkOrUncheckToDo(@PathVariable Long id) {
        try {

            LOG.debug("Try to update \"check\" value for the toDo with the ID {}", id);

            ToDo checkedToDo = toDoService.checkOrUncheckToDoById(id);
            LOG.debug("Update \"check\" for the toDo with the ID {} - DONE", id);

            return ResponseEntity.status(HttpStatus.OK).body(checkedToDo);
        } catch (ToDoException ex) {
            return toDoExceptionHandler.handleToDoException(ex);
        }
    }


    /**
     * Check all ToDos
     *
     * @return
     */
    @Operation(summary = "Check all ToDos")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "\"check\" successfully updated for all id"),
            @ApiResponse(code = 404, message = "No toDo Found"),
            @ApiResponse(code = 500, message = "Server error: An error occurred")
    })
    @PutMapping("/checkAll")
    public ResponseEntity<?> checkAllToDos() {
        try {
            LOG.debug("Try to update \"check\" value for all toDos");

            List<ToDo> toDoList = toDoService.checkAllToDos(true);
            LOG.debug("Update \"check\" value for all toDos - DONE");

            return ResponseEntity.ok(toDoList);
        } catch (ToDoException ex) {
            return toDoExceptionHandler.handleToDoException(ex);
        }
    }


    /**
     * UnCheck all ToDos
     *
     * @return
     */
    @Operation(summary = "UnCheck all ToDos")
    @PutMapping("/uncheckAll")
    public ResponseEntity<?> uncheckAllToDos() {
        try {
            LOG.debug("Try to update \"check\" value for all toDos");

            List<ToDo> toDoList = toDoService.checkAllToDos(false);
            LOG.debug("Update \"check\" value for all toDos - DONE");

            return ResponseEntity.ok(toDoList);
        } catch (ToDoException ex) {
            return toDoExceptionHandler.handleToDoException(ex);
        }
    }

    /**
     * Delete all toDos entity
     *
     * @return
     */
    @Operation(summary = "Delete all  toDos ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All  toDos deleted"),
            @ApiResponse(code = 500, message = "Server error: An error occurred")
    })
    @DeleteMapping()
    public ResponseEntity<?> deleteAllToDos() {
        try {

            LOG.debug("Try to delete all  toDos");

            String deleteMessage = toDoService.deleteAllToDos();
            LOG.debug("Delete all  toDos- DONE");
            return ResponseEntity.ok().build();

        } catch (ToDoException ex) {
            return toDoExceptionHandler.handleToDoException(ex);
        }
    }

    /**
     * Delete all checked toDos entity
     *
     * @return
     */
    @Operation(summary = "Delete all checked toDos ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All checked toDos deleted"),
            @ApiResponse(code = 500, message = "Server error: An error occurred")
    })
    @DeleteMapping("/deleteChecked")
    public ResponseEntity<?> deleteAllCheckedToDos() {
        try {

            LOG.debug("Try to delete all checked toDos");

//            String deleteMessage = toDoService.deleteAllCheckedToDos(true);
            toDoService.deleteAllCheckedToDos(true);
            LOG.debug("Delete all checked toDos- DONE");
            return ResponseEntity.ok().build();

        } catch (ToDoException ex) {
            return toDoExceptionHandler.handleToDoException(ex);
        }
    }

}