package com.aueb.team04.ft.resource;

import com.aueb.team04.ft.persistence.AccessLogRepository;
import com.aueb.team04.ft.domain.AccessLog;
import com.aueb.team04.ft.representation.AccessLogMapper;
import com.aueb.team04.ft.representation.AccessLogRepresentation;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;

import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Path(AccessControlURI.ACCESS_LOGS)
@Consumes(MediaType.APPLICATION_JSON)
public class AccessLogResource {

    private Long accessPointId;
    
    @Context
    private SecurityContext securityContext;

    @Inject
    AccessLogRepository accessLogRepository;

    @Inject
    AccessLogMapper accessLogMapper;

    @Context
    UriInfo uriInfo;

    @GET
    @RolesAllowed("ADMIN")
    public Response getAccessLogs() {
        if (!securityContext.isUserInRole("ADMIN")) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only ADMINS can view the logs").build();
        }

        List<AccessLog> accessLogs = accessLogRepository.findAllAccessLogs();
        List<AccessLogRepresentation> representations = accessLogs.stream()
                .map(accessLogMapper::toRepresentation)
                .collect(Collectors.toList());
        return Response.ok(representations).build();
    }

    @GET
    @Path("/{id}")
    public AccessLogRepresentation getAccessLogById(@PathParam("id") Long id) {
        return accessLogMapper.toRepresentation(accessLogRepository.findById(id));
    }

    // public void setAccessPointId(Long accessPointId) {
    //     this.accessPointId = accessPointId;
    // }

    // public void setSecurityContext(SecurityContext securityContext) {
    //     this.securityContext = securityContext;
    // }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Transactional
    public Response deleteAccessLogById(@PathParam("id") Long id) {
        AccessLog accessLog = accessLogRepository.findById(id);
        if (accessLog == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("AccessLog not found").build();
        }
        accessLogRepository.deleteAccessLogById(id);
        return Response.noContent().build();
    }

}