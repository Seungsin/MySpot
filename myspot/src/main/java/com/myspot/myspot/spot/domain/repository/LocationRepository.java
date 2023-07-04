package com.myspot.myspot.spot.domain.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.myspot.myspot.spot.domain.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Integer> {
    Optional<LocationEntity> findByLocationname(String locationName);

    Optional<LocationEntity> findByLocationmapcode(String LocationMapCode);

    List<LocationEntity> findAllByLocationmapcode(String locationMapCode);
}
