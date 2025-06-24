package br.edu.atitus.api_citytour.repositories;

import br.edu.atitus.api_citytour.entities.PointEntity;
import br.edu.atitus.api_citytour.entities.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, UUID> {
    List<ReviewEntity> findByPlace(PointEntity place);
    long countByPlace(PointEntity place);
    List<ReviewEntity> findByRatingGreaterThanEqualOrderByRatingDesc(int rating);
}
