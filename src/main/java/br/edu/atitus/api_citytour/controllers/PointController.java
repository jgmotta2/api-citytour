package br.edu.atitus.api_citytour.controllers;

import java.util.List;
import java.util.UUID;

import br.edu.atitus.api_citytour.dtos.PointRatingDTO;
import br.edu.atitus.api_citytour.dtos.ReviewDTO;
import br.edu.atitus.api_citytour.entities.ReviewEntity;
import br.edu.atitus.api_citytour.services.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

		existingPoint.setName(dto.name());
		existingPoint.setDescription(dto.description());
		existingPoint.setLatitude(dto.latitude());
		existingPoint.setLongitude(dto.longitude());

		service.save(existingPoint);
		return ResponseEntity.ok(existingPoint);
	}


	@GetMapping
	public ResponseEntity<Page<PointEntity>> findAll(Pageable pageable){
		var page = service.findAll(pageable);
		return ResponseEntity.ok(page);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PointEntity> findById(@PathVariable UUID id) throws Exception {
		PointEntity point = service.findById(id)
				.orElseThrow(() -> new Exception("Place not found."));
		return ResponseEntity.ok(point);
	}

	@GetMapping("/search")
	public ResponseEntity<Page<PointEntity>> searchPoints(@RequestParam String query, Pageable pageable) {
		Page<PointEntity> resultPage = service.search(query, pageable);
		return ResponseEntity.ok(resultPage);
	}

	@GetMapping("/top-rated")
	public ResponseEntity<Page<PointRatingDTO>> getTopRatedPlaces() {
		Page<PointRatingDTO> topRated = service.getTopRated(PageRequest.of(0, 10));
		return ResponseEntity.ok(topRated);
	}

	@GetMapping("/most-visited")
	public ResponseEntity<List<PointEntity>> getMostVisitedPlaces() {
		return ResponseEntity.ok(service.getMostVisited());
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
}
