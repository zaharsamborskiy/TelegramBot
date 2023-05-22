package ru.samborskiy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.samborskiy.model.CurrencyModel;

@Repository
@Transactional
public interface Repo extends JpaRepository<CurrencyModel, Integer> {
}
