package com.aueb.team04.ft.representation;

import com.aueb.team04.ft.domain.AccessRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jakarta", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {BuildingMapper.class, EmployeeMapper.class})
public abstract class AccessRequestMapper {
    public abstract AccessRequestRepresentation toRepresentation(AccessRequest accessRequest);
    public abstract AccessRequest toModel(AccessRequestRepresentation accessRequestRepresentation);
    public abstract List<AccessRequestRepresentation> toRepresentationList(List<AccessRequest> accessRequests);
}