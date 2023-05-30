package ru.samborskiy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.samborskiy.model.CurrencyModel;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyModel, Integer> {
}
