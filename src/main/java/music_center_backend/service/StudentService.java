package music_center_backend.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import music_center_backend.exception.exceptions.IllegalOperationException;
import music_center_backend.exception.exceptions.UserNotFoundException;
import music_center_backend.model.dto.student.CreateStudentRequest;
import music_center_backend.model.dto.student.StudentResponse;
import music_center_backend.model.dto.student.UpdateStudentRequest;
import music_center_backend.model.entity.Student;
import music_center_backend.model.entity.Teacher;
import music_center_backend.repository.StudentRepository;
import music_center_backend.repository.TeacherRepository;
import music_center_backend.security.CurrentUserService;
import music_center_backend.util.HashGenerator;

@Service
@Transactional(readOnly = true)
public class StudentService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserService currentUserService;

    public StudentService(StudentRepository studentRepository, TeacherRepository teacherRepository, PasswordEncoder passwordEncoder, CurrentUserService currentUserService) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
        this.currentUserService = currentUserService;
    }

    public List<StudentResponse> getAll() {
        return toResponse(studentRepository.findAll());
    }
    public StudentResponse getByPublicId(String publicId) {
        Student student = studentRepository.findByPublicId(publicId)
                            .orElseThrow(() -> new UserNotFoundException("No student with id " + publicId));
        return toResponse(student);
    }
    @PreAuthorize("hasRole('ADMIN') or (hasRole('TEACHER') and #teacherPublicId == @currentUserService.getPublicId())")
    public List<StudentResponse> getByTeacherPublicId(String teacherPublicId) {
        teacherRepository.findByPublicId(teacherPublicId)
                .orElseThrow(() -> new UserNotFoundException("No teacher with id " + teacherPublicId));
        
        return toResponse(studentRepository.findByTeacherPublicId(teacherPublicId));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public StudentResponse createStudent(CreateStudentRequest request) {
        if (request.getTeacherPublicId() == null) {
            request.setTeacherPublicId(currentUserService.getPublicId());
        }

        String publicId = createPublicId(request);
        Student newStudent = mapFromCreateRequest(publicId, request);

        String accessCodeHash = passwordEncoder.encode(request.getAccessCode());
        newStudent.setAccessCode(accessCodeHash);
        
        return toResponse(studentRepository.save(newStudent));
    }
    
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or (hasRole('TEACHER') and @authoritiesChecker.currentOwnStudent(#studentPublicId))")
    public StudentResponse updateStudent(String publicId, String teacherPublicId, UpdateStudentRequest request) {
        if (teacherPublicId == null) {
            return teacherUpdateStudent(publicId, request);
        }

        return adminUpdateStudent(publicId, teacherPublicId, request);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or (hasRole('TEACHER') and @authoritiesChecker.currentOwnStudent(#studentPublicId))")
    public void deleteStudent(String studentPublicId) {
        Student student = studentRepository.findByPublicId(studentPublicId)
                .orElseThrow(() -> new UserNotFoundException("No student with id " + studentPublicId));
        
        studentRepository.deleteById(student.getId());
    }
    
    private Student mapFromCreateRequest(String publicId, CreateStudentRequest request) {
        Teacher teacher = teacherRepository.findByPublicId(request.getTeacherPublicId())
                                    .orElseThrow(() -> new UserNotFoundException("No teacher with id " + request.getTeacherPublicId()));
        Student student = new Student(publicId, request.getName(), request.getDateOfStart(), request.getInstrument(), teacher,request.isTakingSolfeige());
        return student;
    }
    private StudentResponse toResponse(Student student) {
        return new StudentResponse(
                        student.getPublicId(),
                        student.getName(),
                        student.getDateOfStart(),
                        student.getInstrument(),
                        student.getTeacher().getName(),
                        student.isTakingSolfeige()
                    );
    }
    private List<StudentResponse> toResponse(List<Student> students) {
        return students.stream().map(this::toResponse).toList();
    }
    private String createPublicId(CreateStudentRequest request) {
        StringBuilder publicIdBuilder = new StringBuilder();
        publicIdBuilder
                    .append(request.getName())
                    .append("-")
                    .append(HashGenerator.generateRandomHash(8));
        
        return publicIdBuilder.toString();
    }
    @Transactional
    private StudentResponse teacherUpdateStudent(String publicId, UpdateStudentRequest request) {
        Student student = studentRepository.findByPublicId(publicId)
                .orElseThrow(() -> new UserNotFoundException("No student with id " + publicId));
        teacherRepository.findByPublicId(currentUserService.getPublicId())
                .orElseThrow(() -> new UserNotFoundException("No teacher with id " + currentUserService.getPublicId()));
        
        if (request.getName() != null) {
            student.setName(request.getName());
        }
        if (request.getInstrument() != null) {
            student.setInstrument(request.getInstrument());
        }
        if(request.getTakingSolfeige() != null) {
            student.setTakingSolfeige(request.getTakingSolfeige());
        }

        return toResponse(student);
    }
    @Transactional
    private StudentResponse adminUpdateStudent(String publicId, String teacherPublicId, UpdateStudentRequest request) {
        Student student = studentRepository.findByPublicId(publicId)
                .orElseThrow(() -> new UserNotFoundException("No student with id " + publicId));
        Teacher teacher =  teacherRepository.findByPublicId(teacherPublicId)
                .orElseThrow(() -> new UserNotFoundException("No teacher with id " + teacherPublicId));

        if (!teacher.isAdmin() && !student.getTeacher().getPublicId().equals(teacherPublicId)) {
            throw new IllegalOperationException("You are not the teacher of this student");
        }

        if (request.getName() != null) {
            student.setName(request.getName());
        }
        if (request.getInstrument() != null) {
            student.setInstrument(request.getInstrument());
        }
        if(request.getTakingSolfeige() != null) {
            student.setTakingSolfeige(request.getTakingSolfeige());
        }
        if (request.getTeacherPublicId() != null) {
            if (teacher.isAdmin()) {
                Teacher newTeacher = teacherRepository.findByPublicId(request.getTeacherPublicId())
                        .orElseThrow(() -> new UserNotFoundException("No teacher with id " + request.getTeacherPublicId()));
                student.setTeacher(newTeacher);
            } else {
                throw new IllegalOperationException("Only admin teachers can change a student's teacher");
            }
        }

        return toResponse(student);
    }
}
