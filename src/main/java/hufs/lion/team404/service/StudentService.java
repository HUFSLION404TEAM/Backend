package hufs.lion.team404.service;

import hufs.lion.team404.entity.Student;
import hufs.lion.team404.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {
    
    private final StudentRepository studentRepository;
    
    @Transactional
    public Student save(Student student) {
        return studentRepository.save(student);
    }
    
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }
    
    public Optional<Student> findByUserId(Long userId) {
        return studentRepository.findByUserId(userId);
    }
    
    public List<Student> findByIsAuthenticated(Boolean isAuthenticated) {
        return studentRepository.findByIsAuthenticated(isAuthenticated);
    }
    
    public List<Student> findBySchool(String school) {
        return studentRepository.findBySchool(school);
    }
    
    public List<Student> findAll() {
        return studentRepository.findAll();
    }
    
    @Transactional
    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }
}
