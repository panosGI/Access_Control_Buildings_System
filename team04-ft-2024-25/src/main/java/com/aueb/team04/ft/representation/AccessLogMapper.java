package com.aueb.team04.ft.representation;

import com.aueb.team04.ft.domain.AccessLog;
import java.util.List;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {AlertMapper.class})
public abstract class AccessLogMapper {

    @Mapping(source = "alerts", target = "alerts")
    @Mapping(source = "accessCard.id", target = "accessCardId")
    @Mapping(source = "accessPoint.id", target = "accessPointId")
    public abstract AccessLogRepresentation toRepresentation(AccessLog accessLog);

    @Mapping(source = "alerts", target = "alerts")
    // @Mapping(source = "accessCardId", target = "accessCard.id")
    // @Mapping(source = "accessPointId", target = "accessPoint.id")
    public abstract AccessLog toModel(AccessLogRepresentation accessLogRepresentation);
    
    public abstract List<AccessLogRepresentation> toRepresentationList(List<AccessLog> accessLogs);
}