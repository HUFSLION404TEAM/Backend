package hufs.lion.team404.service;

import hufs.lion.team404.domain.dto.request.StudentSearchRequestDto;
import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.domain.dto.response.PageResponse;
import hufs.lion.team404.domain.dto.response.StudentResponse;
import hufs.lion.team404.domain.entity.Student;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.repository.StudentRepository;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    
    /**
     * 공개된 학생 프로필 조회 (가게에서 볼 수 있는)
     */
    public Optional<Student> findPublicProfileById(Long studentId) {
        return studentRepository.findById(studentId)
                .filter(student -> Boolean.TRUE.equals(student.getIsPublic()));
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

    /**
     * 공개된 모든 학생 프로필 조회
     */
    public List<Student> findAllPublicProfiles() {
        return studentRepository.findByIsPublicTrueOrderByTemperatureDescCreatedAtDesc();
    }

    // 페이징 - 포폴 조회하기
    public Page<Student> getList(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return this.studentRepository.findAll(pageable);
    }

    public Page<Student> search(StudentSearchRequestDto dto, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Specification<Student> spec = Specification.where(alwaysTrue());

        // 지역 처리
        if (!isBlank(dto.getRegion())) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("region"), dto.getRegion())
            );
        }

        // 경력 처리
        if (!isBlank(dto.getCareer())) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("career")),
                            "%" + dto.getCareer().toLowerCase() + "%"
                    )
            );
        }

        // 구직 중/휴식 중
        if (dto.getIsEmployment() != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("isEmployment"), dto.getIsEmployment())
            );
        }

        // 검색 (이름, 소개, 학교로 해도 나오게)
        if (!isBlank(dto.getKeyword())) {
            String like = "%" + dto.getKeyword().toLowerCase() + "%";
            spec = spec.and((root, criteriaQuery, criteriaBuilder) -> {
                Join<Student, User> userJoin = root.join("user", JoinType.INNER);

                Expression<String> userName     = criteriaBuilder.lower(userJoin.get("name"));
                Expression<String> introduction = criteriaBuilder.lower(root.get("introduction"));
                Expression<String> school       = criteriaBuilder.lower(root.get("school"));

                criteriaQuery.distinct(true);
                return criteriaBuilder.or(
                        criteriaBuilder.like(userName, like),
                        criteriaBuilder.like(introduction, like),
                        criteriaBuilder.like(school, like)
                );
            });
        }

        return studentRepository.findAll(spec, pageable);
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static <T> org.springframework.data.jpa.domain.Specification<T> alwaysTrue() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }


    @Transactional
    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }
}
