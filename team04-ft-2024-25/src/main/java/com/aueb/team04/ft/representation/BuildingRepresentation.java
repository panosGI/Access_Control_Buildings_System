package com.aueb.team04.ft.representation;
import com.aueb.team04.ft.domain.Address;
import com.aueb.team04.ft.domain.Admin;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildingRepresentation {
    public Admin admin;
    public String name;
    public String belongsTo;
    public Address address;
}