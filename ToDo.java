package com.efsauto.erste_schritte.models;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@Table(name = "ToDo")
@ApiModel(description = "ToDo Details")
public class ToDo {

    @Id
    @GeneratedValue
    @ApiModelProperty(notes = "The unique ID of the ToDo", hidden = true)
    private Long id;

    @Column
    @ApiModelProperty(notes = "The ToDo content")
    private String title;

    @Column
    @ApiModelProperty(notes = "Is the ToDo checked?")
    private Boolean checked;


    // ===========================================================
    // Constructor
    // ===========================================================

    public ToDo() {

    }

    public ToDo(String title) {
        this.title = title;
        this.checked = false;
    }

    public ToDo(String title, Boolean checked) {
        this.title = title;
        this.checked = checked;
    }


    // ======================================================================
    // getter/setter
    // ======================================================================

    public Long getId() {
        return id;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
