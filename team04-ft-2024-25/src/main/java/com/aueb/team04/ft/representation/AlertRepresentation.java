package com.aueb.team04.ft.representation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlertRepresentation {
    public Long id;
    public String alertType;
    public int severity;
}