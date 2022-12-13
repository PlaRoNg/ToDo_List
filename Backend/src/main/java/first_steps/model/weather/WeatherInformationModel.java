package com.efsauto.erste_schritte.models.Weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherInformationModel {

    //    @JsonProperty("OpenWeatherList")
    private List<WeatherInformation> weather;

    //    @JsonProperty("OpenWeatherWeather")
    private MainInformation main;

    //    @JsonProperty("OpenWeatherSysInformation")
    private SysInformation sys;

    //    @JsonProperty("OpenWeatherName")
    private String name;

    //    @JsonProperty("OpenWeatherName")

    private Long dt;


    // ===========================================================
    // Constructor
    // ===========================================================
    public WeatherInformationModel() {
    }

    public WeatherInformationModel(String name) {
        this.name = name;
    }

    public WeatherInformationModel(List<WeatherInformation> weather, MainInformation main, SysInformation sys, String name, Long dt) {
        this.weather = weather;
        this.main = main;
        this.sys = sys;
        this.name = name;
        this.dt = dt;
    }

    // ======================================================================
    // getter/setter
    // ======================================================================

    public List<WeatherInformation> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherInformation> weather) {
        this.weather = weather;
    }

    public MainInformation getMain() {
        return main;
    }

    public void setMain(MainInformation main) {
        this.main = main;
    }

    public SysInformation getSys() {
        return sys;
    }

    public void setSys(SysInformation sys) {
        this.sys = sys;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDt() {
        return dt;
    }

    public void setDt(Long dt) {
        this.dt = dt;
    }
}


