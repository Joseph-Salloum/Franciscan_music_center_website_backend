package music_center_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import music_center_backend.service.MedalService;

@RestController
@RequestMapping("/api/v1/medals")
public class MedalController {
    private final MedalService medalService;

    public MedalController(MedalService medalService) {
        this.medalService = medalService;
    }

    // TODO: Add endpoints for medals
}
