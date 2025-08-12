package hufs.lion.team404.model;

import hufs.lion.team404.domain.dto.request.PortfolioCreateRequestDto;
import hufs.lion.team404.domain.dto.request.PortfolioUpdateRequestDto;
import hufs.lion.team404.domain.dto.response.PortfolioResponse;
import hufs.lion.team404.domain.entity.Portfolio;
import hufs.lion.team404.domain.entity.Student;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.domain.enums.UserRole;
import hufs.lion.team404.exception.StudentNotFoundException;
import hufs.lion.team404.service.PortfolioService;
import hufs.lion.team404.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PortfolioModel {
    private final PortfolioService portfolioService;
    private final UserService userService;

    // 생성
    public Portfolio createPortfolio(PortfolioCreateRequestDto portfolioCreateRequestDto, String email) {
        User user = userService.findByEmail(email).orElseThrow(()->new NotFoundException("User not found"));

        Student student = user.getStudent();
        if (student == null) {
            throw new StudentNotFoundException(user.getId()+"에 해당하는 학생이 없습니다.");
        }

        Portfolio portfolio = new Portfolio();
        portfolio.setStudent(student);
        portfolio.setTitle(portfolioCreateRequestDto.getTitle());
        portfolio.setRegion(portfolioCreateRequestDto.getRegion());
        portfolio.setRepresentSentence(portfolioCreateRequestDto.getRepresentSentence());
        portfolio.setCareer(portfolioCreateRequestDto.getCareer());
        portfolio.setIsPublic(portfolioCreateRequestDto.getIsPublic());
        portfolio.setIsJobSeeking(portfolioCreateRequestDto.getIsJobSeeking());

        user.setUserRole(UserRole.STUDENT);

        return portfolioService.save(portfolio);
    }

    // 내 포폴 조회
    @Transactional(readOnly = true)
    public List<PortfolioResponse> getMyPortfolios(String email){
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Student student = user.getStudent();
        if(student == null) {
            throw new StudentNotFoundException(user.getId()+"에 해당하는 학생이 존재하지 않습니다.");
        }

        return portfolioService.findByStudentId(student.getId())
                .stream()
                .map(p -> PortfolioResponse.builder()
                        .id(p.getId())
                        .title(p.getTitle())
                        .region(p.getRegion())
                        .representSentence(p.getRepresentSentence())
                        .career(p.getCareer())
                        .isPublic(p.getIsPublic())
                        .isJobSeeking(p.getIsJobSeeking())
                        .createdAt(p.getCreatedAt())
                        .updatedAt(p.getUpdatedAt())
                        .build())
                .toList();
    }

//    // 조회 - 필터링 조회 (공개, 지역, 경력)
//    @Transactional(readOnly = true)
//    public List<PortfolioResponse> getPortfoliosFiltered(Boolean isPublic, String region, String career){
//        boolean hasRegion = region != null && !region.isBlank();
//        boolean hasCareer = career != null && !career.isBlank();
//
//        List<Portfolio> isPublicPortfolio =
//                Boolean.TRUE.equals(isPublic) ? portfolioService.findByIsPublic(true) :
//                Boolean.FALSE.equals(isPublic) ? portfolioService.findByIsPublic(false) :
//                        portfolioService.findAll();
//
//        List<PortfolioResponse> result = new ArrayList<>();
//
//        for (Portfolio portfolio : isPublicPortfolio) {
//            if (hasRegion && (portfolio.getRegion() == null || !portfolio.getRegion().equals(region))) continue;
//            if (hasCareer && (portfolio.getCareer() == null || !portfolio.getCareer().contains(career))) continue;
//
//            result.add(PortfolioResponse.builder()
//                    .id(portfolio.getId())
//                    .title(portfolio.getTitle())
//                    .region(portfolio.getRegion())
//                    .representSentence(portfolio.getRepresentSentence())
//                    .career(portfolio.getCareer())
//                    .studentName(portfolio.getStudent().getUser().getName())
//                    .isPublic(portfolio.getIsPublic())
//                    .createdAt(portfolio.getCreatedAt())
//                    .updatedAt(portfolio.getUpdatedAt())
//                    .build());
//        }
//        return result;
//    }

    // 수정
    @Transactional
    public void updatePortfolio(Long portfolioId, PortfolioUpdateRequestDto dto, String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getStudent() == null) {
            throw new StudentNotFoundException(user.getId() + "에 해당하는 학생이 없습니다.");
        }

        portfolioService.update(portfolioId, dto, email);
    }


    public void deletePortfolio(Long portfolioId, String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));

        Student student = user.getStudent();
        if (student == null) {
            throw new StudentNotFoundException(user.getId()+"에 해당하는 학생이 없습니다.");
        }

        Portfolio portfolio = portfolioService.findById(portfolioId)
                .orElseThrow(() -> new EntityNotFoundException("포트폴리오를 찾을 수 없습니다."));

        if (!portfolio.getStudent().getId().equals(student.getId())) {
            throw new IllegalArgumentException("본인의 포트폴리오만 삭제할 수 있습니다.");
        }

        portfolioService.deleteById(portfolioId);
    }

    //  검색
    @Transactional(readOnly = true)
    public List<PortfolioResponse> searchAndFilterPublic(String region, String career, Boolean isJobSeeking, String q) {

        List<Portfolio> list = portfolioService.searchAndFilterPublic(region, career, isJobSeeking, q);

        return list.stream()
                .map(p -> PortfolioResponse.builder()
                        .id(p.getId())
                        .title(p.getTitle())
                        .region(p.getRegion())
                        .representSentence(p.getRepresentSentence())
                        .career(p.getCareer())
                        .studentName(p.getStudent().getUser().getName())
                        .isPublic(p.getIsPublic())
                        .isJobSeeking(p.getIsJobSeeking())
                        .createdAt(p.getCreatedAt())
                        .updatedAt(p.getUpdatedAt())
                        .build())
                .toList();
    }




}
