package com.aueb.team04.ft.persistence;


import com.aueb.team04.ft.domain.AccessCard;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.smallrye.common.constraint.Assert.assertNotNull;
import static io.smallrye.common.constraint.Assert.assertTrue;

@QuarkusTest
public class AccessCardRepositoryTest {
    @Inject
    AccessCardRepository accessCardRepository;

    @Test
    public void testListAllAccessCards() {
        List<AccessCard> accessCards = accessCardRepository.listAllAccessCards();
        assertNotNull(accessCards);
    }

    @Test
    @Transactional
    public void testSaveAccessCard() {
        AccessCard accessCard = new AccessCard();
        accessCardRepository.save(accessCard);

        assertTrue(accessCardRepository.findById(accessCard.getId()) != null);
    }

    @Test
    @Transactional
    public void testDeleteAccessCardById() {
        AccessCard accessCard = new AccessCard();
        accessCardRepository.save(accessCard);

        accessCardRepository.deleteAccessCardById(accessCard.getId());

        // Flush and clear the persistence context to ensure the delete is committed
        accessCardRepository.getEntityManager().flush();
        accessCardRepository.getEntityManager().clear();

        assertTrue(accessCardRepository.findById(accessCard.getId()) == null);
    }
}