package com.myspot.myspot.spot.domain.repository;

import com.myspot.myspot.spot.domain.entity.SpotTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpotTagRepository extends JpaRepository<SpotTagEntity, Integer> {
}
