package com.aueb.team04.ft.persistence;

import com.aueb.team04.ft.domain.AccessCard;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;
import java.util.List;

@RequestScoped
public class AccessCardRepository implements PanacheRepositoryBase<AccessCard, Long> {
    public List<AccessCard> listAllAccessCards() {
        return listAll();
    }
    public void save(AccessCard accessCard) {
        persist(accessCard);
    }

    public void deleteAccessCardById(Long accessCardId) {
        delete("id", accessCardId);
    }
}