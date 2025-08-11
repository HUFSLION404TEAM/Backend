package hufs.lion.team404.model;

import hufs.lion.team404.controller.PortfolioProjectController;
import hufs.lion.team404.domain.dto.request.PortfolioProjectCreateRequestDto;
import hufs.lion.team404.domain.dto.request.PortfolioProjectUpdateRequestDto;
import hufs.lion.team404.domain.dto.response.PortfolioProjectResponse;
import hufs.lion.team404.domain.entity.Portfolio;
import hufs.lion.team404.domain.entity.PortfolioProject;
import hufs.lion.team404.domain.entity.Student;
import hufs.lion.team404.domain.entity.User;
import hufs.lion.team404.exception.StudentNotFoundException;
import hufs.lion.team404.service.PortfolioProjectService;
import hufs.lion.team404.service.PortfolioService;
import hufs.lion.team404.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PortfolioProjectModel {
    private final PortfolioProjectService portfolioProjectService;
    private final PortfolioService portfolioService;
    private final UserService userService;

    // 생성
    public PortfolioProject createPortfolioProject(Long portfolioId, PortfolioProjectCreateRequestDto dto) {

        Portfolio portfolio = portfolioService.findById(portfolioId)
                .orElseThrow(()->new IllegalArgumentException("Portfolio not found"));

        PortfolioProject portfolioProject = new PortfolioProject();
        portfolioProject.setPortfolio(portfolio);
        portfolioProject.setTitle(dto.getTitle());
        portfolioProject.setIntroduction(dto.getIntroduction());
        portfolioProject.setOutline(dto.getOutline());
        portfolioProject.setWork(dto.getWork());
        portfolioProject.setProcess(dto.getProcess());
        portfolioProject.setResult(dto.getResult());
        portfolioProject.setGrow(dto.getGrow());
        portfolioProject.setCompetency(dto.getCompetency());
        portfolioProject.setPrize(dto.getPrize());
        portfolioProject.setDisplayOrder(dto.getDisplayOrder());

        return portfolioProjectService.save(portfolioProject);
    }

    // 조회
    @Transactional(readOnly = true)
    public List<PortfolioProjectResponse> getPortfolioProjects(Long portfolioId) {
        portfolioService.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found"));


        return portfolioProjectService.findByPortfolioIdOrderByDisplayOrder(portfolioId)
                .stream()
                .map(p->PortfolioProjectResponse.builder()
                        .id(p.getId())
                        .title(p.getTitle())
                        .introduction(p.getIntroduction())
                        .outline(p.getOutline())
                        .process(p.getProcess())
                        .result(p.getResult())
                        .grow(p.getGrow())
                        .competency(p.getCompetency())
                        .prize(p.getPrize())
                        .displayOrder(p.getDisplayOrder())
                        .build()
                )
                .toList();
       }

    // 수정
    @Transactional
    public void UpdatePortfolioProject(Long portfolioId, Long id, PortfolioProjectUpdateRequestDto dto) {

        portfolioProjectService.update(portfolioId, id, dto);
    }


    // 삭제
    public void deletePortfolioProject(Long portfolioId, Long projectId, String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        PortfolioProject portfolioProject = portfolioProjectService.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("프로젝트를 찾을 수 없습니다."));

        if (!portfolioProject.getPortfolio().getId().equals(portfolioId)) {
            throw new IllegalArgumentException("해당 포트폴리오의 프로젝트가 아닙니다.");
        }

        Long ownerId = portfolioProject.getPortfolio().getStudent().getUser().getId();
        if (!ownerId.equals(user.getId())) {
            throw new IllegalArgumentException("본인의 프로젝트만 삭제할 수 있습니다.");
        }

        portfolioProjectService.deleteById(projectId);
    }


}
