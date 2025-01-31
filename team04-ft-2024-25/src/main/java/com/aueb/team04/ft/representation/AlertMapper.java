package com.aueb.team04.ft.representation;

import com.aueb.team04.ft.domain.Alert;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class AlertMapper {
    public abstract AlertRepresentation toRepresentation(Alert alert);
    public abstract Alert toModel(AlertRepresentation alertRepresentation);
}