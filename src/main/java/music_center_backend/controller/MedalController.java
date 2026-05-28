package music_center_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import music_center_backend.model.dto.medal.CreateMedalRequest;
import music_center_backend.model.dto.medal.MedalResponse;
import music_center_backend.model.dto.medal.UpdateMedalRequest;
import music_center_backend.service.MedalService;

@RestController
@RequestMapping("/api/v1/medals")
public class MedalController {
    private final MedalService medalService;

    public MedalController(MedalService medalService) {
        this.medalService = medalService;
    }

    @GetMapping
    public List<MedalResponse> getAll() {
        return medalService.getAllMedals();
    }
    @GetMapping("/{medalName}")
    public MedalResponse getByMedalName(@PathVariable String medalName) {
        return medalService.getByMedalName(medalName);
    }

    @PostMapping
    public ResponseEntity<MedalResponse> createMedal(@Valid @RequestBody CreateMedalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medalService.createMedal(request));
    }

    @PatchMapping("/{medalName}")
    public MedalResponse updateMedal(@PathVariable String medalName, @Valid @RequestBody UpdateMedalRequest request) {
        return medalService.updateMedal(medalName, request);
    }

    @DeleteMapping("/{medalName}")
    public ResponseEntity<Void> deleteMedal(@PathVariable String medalName) {
        medalService.deleteMedal(medalName);
        return ResponseEntity.noContent().build();
    }
}
