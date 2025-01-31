package com.aueb.team04.ft.persistence;

import com.aueb.team04.ft.domain.AccessRequest;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;

import java.util.List;

@RequestScoped
public class AccessRequestRepository implements PanacheRepositoryBase<AccessRequest, Long> {
    public AccessRequest findByAccessRequestId(Long accessRequestId) {
        return find("requestID = ?1", accessRequestId).firstResult();
    }

    public List<AccessRequest> findAllAccessRequests() {
        return listAll();
    }

    public void saveAccessRequest(AccessRequest accessRequest) {
        persist(accessRequest);
    }
}