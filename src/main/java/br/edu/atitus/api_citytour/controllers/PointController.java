package br.edu.atitus.api_citytour.controllers;

import java.util.List;
import java.util.UUID;

import br.edu.atitus.api_citytour.dtos.ReviewDTO;
import br.edu.atitus.api_citytour.entities.ReviewEntity;
import br.edu.atitus.api_citytour.services.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.atitus.api_citytour.dtos.PointDTO;
import br.edu.atitus.api_citytour.entities.PointEntity;
import br.edu.atitus.api_citytour.services.PointService;

@RestController
@RequestMapping("/ws/points")
public class PointController {

	public final PointService service;
	public final ReviewService reviewService;

	public PointController(PointService service, ReviewService reviewService) {
		super();
		this.service = service;
		this.reviewService = reviewService;
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable UUID id) throws Exception{
		service.deleteById(id);

		return ResponseEntity.ok("Place deleted");

	}

	@PostMapping
	public ResponseEntity<PointEntity> save(@Valid @RequestBody PointDTO dto) throws Exception {
		PointEntity point = new PointEntity();
		BeanUtils.copyProperties(dto, point);

		service.save(point);

		return ResponseEntity.status(201).body(point);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PointEntity> update(@PathVariable UUID id, @Valid @RequestBody PointDTO dto) throws Exception {
		PointEntity existingPoint = service.findById(id)
				.orElseThrow(() -> new Exception("Place not found."));
		existingPoint.setDescription(dto.description());
		existingPoint.setLatitude(dto.latitude());
		existingPoint.setLongitude(dto.longitude());

		service.save(existingPoint);
		return ResponseEntity.ok(existingPoint);
	}


	@GetMapping
	public ResponseEntity<List<PointEntity>> findAll(){
		var list = service.findAll();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PointEntity> findById(@PathVariable UUID id) throws Exception {
		PointEntity point = service.findById(id)
				.orElseThrow(() -> new Exception("Place not found."));
		return ResponseEntity.ok(point);
	}

	@GetMapping("/search")
	public ResponseEntity<List<PointEntity>> searchPoints(@RequestParam String query) {
		// You would need to add a `search` method in `PointService` and `PointRepository`
		// E.g., `List<PointEntity> searchPoints(String query);` in PointRepository
		// That searches by name or description.
		// For now, it returns all that match the query in the description (example)
		return ResponseEntity.ok(service.findByDescriptionContainingIgnoreCase(query));
	}

	@GetMapping("/top-rated")
	public ResponseEntity<List<ReviewEntity>> getTopRatedPlaces() {
		// This returns the reviews, but you might want to return the Places with their average ratings
		// For simplicity, I'm returning the reviews. The ideal would be a Place DTO with the average review score
		return ResponseEntity.ok(reviewService.getTopRatedReviews(4)); // Example: ratings >= 4 stars
	}

	// New endpoint for "Most Visited Places" (Most Visited Places Screen)
	@GetMapping("/most-visited")
	public ResponseEntity<List<PointEntity>> getMostVisitedPlaces() {
		// This functionality requires a visit counting mechanism or other criteria.
		// Returning all as a placeholder, but ideally it would be a list ordered by visits.
		return ResponseEntity.ok(service.findAll()); // Placeholder
	}

	@PostMapping("/{placeId}/reviews")
	public ResponseEntity<ReviewEntity> addReviewToPlace(
			@PathVariable UUID placeId,
			@Valid @RequestBody ReviewDTO reviewDTO) throws Exception {
		ReviewEntity review = new ReviewEntity();
		BeanUtils.copyProperties(reviewDTO, review);
		ReviewEntity savedReview = reviewService.saveReview(placeId, review);
		return ResponseEntity.status(201).body(savedReview);
	}

	@GetMapping("/{placeId}/reviews")
	public ResponseEntity<List<ReviewEntity>> getReviewsOfPlace(@PathVariable UUID placeId) throws Exception {
		List<ReviewEntity> reviews = reviewService.getReviewsByPlace(placeId);
		return ResponseEntity.ok(reviews);
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<String> handlerException(Exception ex) {
		String message = ex.getMessage().replaceAll("\r\n", "");
		return ResponseEntity.badRequest().body(message);
	}
}
