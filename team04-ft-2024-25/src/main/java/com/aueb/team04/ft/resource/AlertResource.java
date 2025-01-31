package com.aueb.team04.ft.resource;

import java.util.List;
import com.aueb.team04.ft.domain.Alert;
import com.aueb.team04.ft.persistence.AlertRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.MediaType;

@RequestScoped
@Path(AccessControlURI.ALERT)
@Consumes(MediaType.APPLICATION_JSON)
public class AlertResource {

    @Context
    private SecurityContext securityContext;

    @Inject
    AlertRepository alertRepository;

    @Context
    UriInfo uriInfo;

    @GET
    @RolesAllowed("ADMIN")
    public Response getAlerts() {
        if (!securityContext.isUserInRole("ADMIN")) {
            return Response.status(Response.Status.FORBIDDEN).entity("Only ADMINS can view the Alerts").build();
        }
        List<Alert> alerts = alertRepository.findAllAlerts();
        return Response.ok(alerts).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    @Transactional
    public Response deleteAlertById(@PathParam("id") Long id) {
        Alert alert = alertRepository.findById(id);
        if (alert == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Alert not found").build();
        }
        alertRepository.deleteAlertById(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/severity/{id}")
    @RolesAllowed("ADMIN")
    public Response getAlertBySeverity(@PathParam("id") int severity) {
        List<Alert> alerts = alertRepository.findAlertBySeverity(severity);
        return Response.ok(alerts).build();
    }
}
