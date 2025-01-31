package com.aueb.team04.ft.persistence;

import com.aueb.team04.ft.domain.Admin;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class AdminRepository implements PanacheRepositoryBase<Admin, Long> {
    public Admin findByUsername(String username) {
        return find("username", username).firstResult();
    }
}