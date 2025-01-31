package com.aueb.team04.ft.persistence;

import com.aueb.team04.ft.domain.Building;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class BuildingRepositoryTest {
    @Inject
    BuildingRepository buildingRepository;

    @Test
    public void testFindAllBuildings() {
        assertNotNull(buildingRepository.findAllBuildings());
    }

    @Test
    public void testFindByID() {
        assertNotNull(buildingRepository.findByID(1111L));
    }

    @Test
    @Transactional
    public void testSave() {
        Building building = new Building();
        buildingRepository.save(building);
        assertNotNull(buildingRepository.findByID(building.getId()));
    }
}
