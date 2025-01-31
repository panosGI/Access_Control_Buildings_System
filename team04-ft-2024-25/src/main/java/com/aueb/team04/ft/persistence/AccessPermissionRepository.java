package com.aueb.team04.ft.persistence;

import com.aueb.team04.ft.domain.AccessPermission;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class AccessPermissionRepository implements PanacheRepositoryBase<AccessPermission, Long> {
    public void save(AccessPermission accessPermission) {
        persist(accessPermission);
    }
}
