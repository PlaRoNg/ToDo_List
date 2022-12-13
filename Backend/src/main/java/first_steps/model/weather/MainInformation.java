package com.efsauto.erste_schritte.models.Weather;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MainInformation {


    private Float temp;

    private Float feels_like;

    private Float temp_min;

    private Float temp_max;

    // ===========================================================
    // Constructor
    // ===========================================================

    public MainInformation() {
    }

    public MainInformation(Float temp, Float feels_like, Float temp_min, Float temp_max, String pressure, String humidity, String sea_level, String grnd_level) {
        this.temp = temp;
        this.feels_like = feels_like;
        this.temp_min = temp_min;
        this.temp_max = temp_max;

    }

    // ======================================================================
    // getter/setter
    // ======================================================================


    public Float getTemp() {
        return temp;
    }

    public void setTemp(Float temp) {
        this.temp = temp;
    }

    public Float getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(Float feels_like) {
        this.feels_like = feels_like;
    }

    public Float getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(Float temp_min) {
        this.temp_min = temp_min;
    }

    public Float getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(Float temp_max) {
        this.temp_max = temp_max;
    }


}
