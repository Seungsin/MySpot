package com.myspot.myspot.spot.domain.repository;

import com.myspot.myspot.spot.domain.entity.SpotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpotRepository extends JpaRepository<SpotEntity, Integer> {
    List<SpotEntity> findAllByUseremail(String userEmail);

    List<SpotEntity> findAllByLocationnum(Integer locationnum);

    Long countByLocationnum(Integer locationnum);
}
