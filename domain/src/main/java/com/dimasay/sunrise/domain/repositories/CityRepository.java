package com.dimasay.sunrise.domain.repositories;

import com.dimasay.sunrise.domain.entities.City;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CityRepository extends CrudRepository<City, UUID> {
    City findByName(String name);
}
