package com.efsauto.erste_schritte.models.Weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SysInformation {

    private String country;


    // ===========================================================
    // Constructor
    // ===========================================================


    public SysInformation() {
    }

    public SysInformation(String country, Long sunrise, Long sunset) {
        this.country = country;
    }

    // ======================================================================
    // getter/setter
    // ======================================================================


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
