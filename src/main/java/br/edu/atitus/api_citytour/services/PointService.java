package br.edu.atitus.api_citytour.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

	public PointEntity save(PointEntity point) throws Exception {
		if (point == null)
			throw new Exception("Object cannot be null");

		if (point.getDescription() == null || point.getDescription().isEmpty())
			throw new Exception("Invalid Description");
		point.setDescription(point.getDescription().trim());

		if (point.getLatitude() < -90 || point.getLatitude() > 90)
			throw new Exception("Invalid Latitude");

		if (point.getLongitude() < -180 || point.getLongitude() > 180)
			throw new Exception("Invalid Longitude");

		if (point.getId() == null) {
			UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			point.setUser(userAuth);
		} else {
			Optional<PointEntity> existingPointOpt = repository.findById(point.getId());
			if (existingPointOpt.isPresent()) {
				PointEntity existingPoint = existingPointOpt.get();
				UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				if (!existingPoint.getUser().getId().equals(userAuth.getId())) {
					throw new Exception("You do not have permission to update this record.");
				}
				point.setUser(existingPoint.getUser());
			} else {
				throw new Exception("Point not found for update.");
			}
		}

		return repository.save(point);
	}

	public void deleteById(UUID id) throws Exception {
		var point = repository.findById(id)
				.orElseThrow(() -> new Exception("Point with this ID does not exist."));

		UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!point.getUser().getId().equals(userAuth.getId()))
			throw new Exception("You do not have permission to delete this record.");

		repository.deleteById(id);
	}

	public List<PointEntity> findAll() {
		return repository.findAll();
	}

	public Optional<PointEntity> findById(UUID id) {
		return repository.findById(id);
	}

	public List<PointEntity> findByDescriptionContainingIgnoreCase(String query) {
		return repository.findByDescriptionContainingIgnoreCase(query);
	}
}
