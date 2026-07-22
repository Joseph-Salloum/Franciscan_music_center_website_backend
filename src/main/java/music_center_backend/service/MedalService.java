package music_center_backend.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import music_center_backend.exception.exceptions.DuplicateMedalNameException;
import music_center_backend.exception.exceptions.MedalNotFoundException;
import music_center_backend.model.dto.medal.CreateMedalRequest;
import music_center_backend.model.dto.medal.MedalResponse;
import music_center_backend.model.dto.medal.UpdateMedalRequest;
import music_center_backend.model.entity.Medal;
import music_center_backend.repository.MedalRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MedalService {
    private final MedalRepository medalRepository;

    public List<MedalResponse> getAllMedals() {
        return toResponse(medalRepository.findAll());
    }
    public MedalResponse getByMedalName(String medalName) {
        Medal medal = medalRepository.findByMedalName(medalName)
                            .orElseThrow(() -> new MedalNotFoundException("Medal with name \'" + medalName + "\' not found"));
        return toResponse(medal);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public MedalResponse createMedal(CreateMedalRequest request) {
        if (medalRepository.findByMedalName(request.getMedalName()).isPresent()) {
            throw new IllegalArgumentException("A medal with the name \'" + request.getMedalName() + "\' already exists");
        }

        Medal newMedal = mapFromCreateRequest(request);
        return toResponse(medalRepository.save(newMedal));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public MedalResponse updateMedal(String medalName, UpdateMedalRequest request) {
        Medal medal = medalRepository.findByMedalName(medalName)
                            .orElseThrow(() -> new MedalNotFoundException("Medal with name \'" + medalName + "\' not found"));

        if (request.getMedalName() != null) {
            if (request.getMedalName().isBlank()) {
                throw new IllegalArgumentException("Medal name cannot be blank");
            }
            
            if (!request.getMedalName().equals(medalName)
                    && medalRepository.existsByMedalName(request.getMedalName())) {

                throw new DuplicateMedalNameException(
                        "Medal with name \'" + request.getMedalName() + "\' already exists");
            }

            medal.setMedalName(request.getMedalName());
        }
        if (request.getMedalDescription() != null) {
            if (request.getMedalDescription().isBlank()) {
                throw new IllegalArgumentException("Medal description cannot be blank");
            }

            medal.setMedalDescription(request.getMedalDescription());
        }

        return toResponse(medal);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteMedal(String medalName) {
        Medal medal = medalRepository.findByMedalName(medalName)
                            .orElseThrow(() -> new MedalNotFoundException("Medal with name \'" + medalName + "\' not found"));
        medalRepository.deleteById(medal.getId());
    }

    private Medal mapFromCreateRequest(CreateMedalRequest request) {
        return new Medal(request.getMedalName(), request.getMedalDescription());
    }
    private MedalResponse toResponse(Medal medal) {
        return new MedalResponse(
                            medal.getMedalName(), 
                            medal.getMedalDescription()
                    );
    }
    private List<MedalResponse> toResponse(List<Medal> medals) {
        return medals.stream().map(this::toResponse).toList();
    }
}
