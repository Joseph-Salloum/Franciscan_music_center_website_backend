package music_center_backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import music_center_backend.exception.exceptions.InvalidCredentialsException;
import music_center_backend.exception.exceptions.MalformedIdException;
import music_center_backend.model.entity.Student;
import music_center_backend.model.entity.Teacher;
import music_center_backend.repository.StudentRepository;
import music_center_backend.repository.TeacherRepository;
import music_center_backend.security.jwt.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String authenticate(String publicId, String accessCode) {
        String token = switch(publicId.split("-").length) {
            case 1 -> authenticateDev(publicId, accessCode);
            case 2 -> authenticateStudent(publicId, accessCode);
            case 3 -> authenticateTeacher(publicId, accessCode);
            default -> throw new MalformedIdException("Illegal publicId format " + publicId);
        };

        return token;
    }

    //remove before production with every related thing
    private String authenticateDev(String publicId, String accessCode) {
        if (publicId.equals("dev123abc") && accessCode.equals("12345678")) {
            return jwtService.generateDevToken(publicId);
        }
        throw new InvalidCredentialsException("Not a dev");
    }
    private String authenticateStudent(String publicId, String accessCode) {
        Student student = studentRepository.findByPublicId(publicId)
                    .orElseThrow(() -> new InvalidCredentialsException("Invalid Credentials"));
        
        if (!passwordEncoder.matches(accessCode, student.getAccessCode())) {
            throw new InvalidCredentialsException("Invalid Credentials");
        }
        
        return jwtService.generateToken(student.getPublicId());
    }
    private String authenticateTeacher(String publicId, String accessCode) {
        Teacher teacher = teacherRepository.findByPublicId(publicId)
                    .orElseThrow(() -> new InvalidCredentialsException("Invalid Credentials"));
        
        if (!passwordEncoder.matches(accessCode, teacher.getAccessCode())) {
            throw new InvalidCredentialsException("Invalid Credentials");
        }
        
        return jwtService.generateToken(teacher.getPublicId());
    }
}
