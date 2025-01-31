package com.aueb.team04.ft.persistence;

import com.aueb.team04.ft.domain.AccessPoint;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;

import java.util.List;

@RequestScoped
public class AccessPointRepository implements PanacheRepositoryBase<AccessPoint, Long> {

    public AccessPoint getAccessPointById(Long id) {
        return find("id", id).firstResult();
    }

    public List<AccessPoint> getAccessPoints() {
        return listAll();
    }
    public void saveAccessPoint(AccessPoint accessPoint) {
        persist(accessPoint);
    }
}
