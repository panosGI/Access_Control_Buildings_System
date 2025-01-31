package com.aueb.team04.ft.representation;

import com.aueb.team04.ft.domain.Building;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "jakarta", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class BuildingMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "belongsTo", target = "belongsTo")
    @Mapping(source = "address", target = "address")
    public abstract BuildingRepresentation toRepresentation(Building building);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "belongsTo", target = "belongsTo")
    @Mapping(source = "address", target = "address")
    public abstract  Building toModel(BuildingRepresentation buildingRepresentation);
}