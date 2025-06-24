package br.edu.atitus.api_citytour.repositories;

import java.util.List;
import java.util.UUID;

import br.edu.atitus.api_citytour.dtos.PointRatingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.atitus.api_citytour.entities.PointEntity;
import br.edu.atitus.api_citytour.entities.UserEntity;

@Repository
public interface PointRepository extends JpaRepository<PointEntity, UUID>{
	
	List<PointEntity> findByUser(UserEntity user);

	List<PointEntity> findTop10ByVisitCountGreaterThanOrderByVisitCountDesc(int visitCount);

	@Query("SELECT p FROM PointEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :term, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :term, '%'))")
	Page<PointEntity> searchByTerm(@Param("term") String term, Pageable pageable);

	@Query("SELECT new br.edu.atitus.api_citytour.dtos.PointRatingDTO(p.id, p.description, AVG(r.rating), COUNT(r)) " +
			"FROM PointEntity p JOIN ReviewEntity r ON r.place.id = p.id " +
			"GROUP BY p.id, p.description " +
			"ORDER BY AVG(r.rating) DESC, COUNT(r) DESC")
	Page<PointRatingDTO> findTopRatedPoints(Pageable pageable);


}
