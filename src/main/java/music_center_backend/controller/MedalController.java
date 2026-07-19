package music_center_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import music_center_backend.model.dto.medal.MedalResponse;
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
}
