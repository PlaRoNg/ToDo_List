package com.efsauto.erste_schritte.models.Weather;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherInformation {
    private Long id;

    private String main;

    private String description;

    private String icon;


    // ===========================================================
    // Constructor
    // ===========================================================


    public WeatherInformation() {
    }

    public WeatherInformation( String main, String description, String icon) {
        this.main = main;
        this.description = description;
        this.icon = icon;
    }


    // ======================================================================
    // getter/setter
    // ======================================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}


