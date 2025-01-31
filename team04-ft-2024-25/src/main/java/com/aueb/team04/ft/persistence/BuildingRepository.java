package com.aueb.team04.ft.persistence;

import com.aueb.team04.ft.domain.Building;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;
import java.util.List;

@RequestScoped
public class BuildingRepository implements PanacheRepositoryBase<Building, Long> {
    public Building findByID(Long id) {
        return find("id", id).firstResult();
    }

    public List<Building> findAllBuildings() {
        return listAll();
    }

    public void save(Building building) {
        persist(building);
    }
}