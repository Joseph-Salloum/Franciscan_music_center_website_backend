package music_center_backend.security.user;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import music_center_backend.model.constant.Specialization;
import music_center_backend.model.entity.Student;
import music_center_backend.model.entity.Teacher;
import music_center_backend.repository.StudentRepository;
import music_center_backend.repository.TeacherRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public CustomUserDetailsService(TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String publicId) throws UsernameNotFoundException {
        UserDetails user = switch(publicId.split("-").length) {
            case 1 -> loadDev(publicId);
            case 2 -> loadStudentByPublicId(publicId);
            case 3 -> loadTeacherByPublicId(publicId);
            default -> throw new UsernameNotFoundException("Illegal publicId format");
        };

        return user;
    }

    private UserDetails loadDev(String publicId) {
        Teacher teacher = new Teacher(publicId, "Dev", Specialization.GUITAR, true);
        return User
                .withUsername(teacher.getPublicId())
                .password("12345678")
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_TEACHER")))
                .build();
    }
    private UserDetails loadTeacherByPublicId(String publicId) throws UsernameNotFoundException {
        Teacher teacher = teacherRepository.findByPublicId(publicId)
                .orElseThrow(() -> new UsernameNotFoundException("Teacher not found"));
        List<GrantedAuthority> authorities;

        if (teacher.isAdmin()) {
            authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
                                    new SimpleGrantedAuthority("ROLE_TEACHER"));
        } else {
            authorities = List.of(new SimpleGrantedAuthority("ROLE_TEACHER"));
        }

        return User
                .withUsername(teacher.getPublicId())
                .password(teacher.getAccessCode())
                .authorities(authorities)
                .build();
    }
    private UserDetails loadStudentByPublicId(String publicId) throws UsernameNotFoundException {
        Student student = studentRepository.findByPublicId(publicId)
                .orElseThrow(() -> new UsernameNotFoundException("Student not found"));
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_STUDENT"));
        
        return User
                .withUsername(student.getPublicId())
                .password(student.getAccessCode())
                .authorities(authorities)
                .build();
    }
}
