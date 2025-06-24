package br.edu.atitus.api_citytour.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.edu.atitus.api_citytour.components.ResourceNotFoundExcep;
import br.edu.atitus.api_citytour.dtos.PointRatingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.edu.atitus.api_citytour.entities.PointEntity;
import br.edu.atitus.api_citytour.entities.UserEntity;
import br.edu.atitus.api_citytour.repositories.PointRepository;

@Service
public class PointService {

	private final PointRepository repository;

	public PointService(PointRepository repository) {
		super();
		this.repository = repository;
	}

	public PointEntity save(PointEntity point) throws ResourceNotFoundExcep {
		if (point == null)
			throw new ResourceNotFoundExcep("Object cannot be null");

		if (point.getDescription() == null || point.getDescription().isEmpty())
			throw new ResourceNotFoundExcep("Invalid Description");
		point.setDescription(point.getDescription().trim());

		if (point.getLatitude() < -90 || point.getLatitude() > 90)
			throw new ResourceNotFoundExcep("Invalid Latitude");

		if (point.getLongitude() < -180 || point.getLongitude() > 180)
			throw new ResourceNotFoundExcep("Invalid Longitude");

		if (point.getId() == null) {
			UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			point.setUser(userAuth);
		} else {
			Optional<PointEntity> existingPointOpt = repository.findById(point.getId());
			if (existingPointOpt.isPresent()) {
				PointEntity existingPoint = existingPointOpt.get();
				UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				if (!existingPoint.getUser().getId().equals(userAuth.getId())) {
					throw new ResourceNotFoundExcep("You do not have permission to update this record.");
				}
				point.setUser(existingPoint.getUser());
			} else {
				throw new ResourceNotFoundExcep("Point not found for update.");
			}
		}

		return repository.save(point);
	}

	public void deleteById(UUID id) throws ResourceNotFoundExcep {
		var point = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundExcep("Point with this ID does not exist."));

		UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!point.getUser().getId().equals(userAuth.getId()))
			throw new ResourceNotFoundExcep("You do not have permission to delete this record.");

		repository.deleteById(id);
	}

	public Page<PointEntity> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Optional<PointEntity> findById(UUID id) {

		Optional<PointEntity> pointOpt = repository.findById(id);
		if (pointOpt.isPresent()) {
			PointEntity point = pointOpt.get();
			point.setVisitCount(point.getVisitCount() + 1);
			repository.save(point);
		}
		return pointOpt;
	}

	public List<PointEntity> findByDescriptionContainingIgnoreCase(String query) {
		return repository.findByDescriptionContainingIgnoreCase(query);
	}

	public List<PointEntity> getMostVisited() {
		return repository.findTop10ByOrderByVisitCountDesc();
	}

	public Page<PointRatingDTO> getTopRated(Pageable pageable) {
		return repository.findTopRatedPoints(pageable);
	}
}
