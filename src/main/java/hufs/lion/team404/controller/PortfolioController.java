package hufs.lion.team404.controller;

import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.domain.dto.response.PortfolioResponse;
import hufs.lion.team404.domain.entity.Portfolio;
import hufs.lion.team404.model.PortfolioModel;
import hufs.lion.team404.oauth.jwt.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "포트폴리오", description = "포트폴리오 관련 API")
public class PortfolioController {
    private final PortfolioModel portfolioModel;

    @PostMapping(value = "/mypage/portfolios", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "포트폴리오 생성",
            description = "새로운 포트폴리오를 생성합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<?> createPortfolio(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "progressPeriod") String progressPeriod,
            @RequestParam(value = "prize") Boolean prize,
            @RequestParam(value = "workDoneProgress") String workDoneProgress,
            @RequestParam(value = "result") String result,
            @RequestParam(value = "felt") String felt,
            @RequestParam(value = "isPrivate") Boolean isPrivate,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
            ) {

        Long portfolio_id = portfolioModel.createPortfolio(
                title, progressPeriod, prize, workDoneProgress, result, felt,
                isPrivate, images, userPrincipal.getEmail()
        );
        return ApiResponse.success(portfolio_id);
    }

    @GetMapping("/mypage/portfolios")
    @Operation(
            summary = "전체 포트폴리오 조회",
            description = "전체 포트폴리오를 조회합니다."
    )
    public ApiResponse<List<Portfolio>> getAllPortfolios() {

        List<Portfolio> portfolios = portfolioModel.getAllPortfolios();

        return ApiResponse.success("포트폴리오가 성공적으로 조회되었습니다.", portfolios);
    }

    @GetMapping("/mypage/portfolios/{portfolioId}")
    @Operation(
            summary = "포트폴리오 조회",
            description = "포트폴리오를 조회합니다."
    )
    public ApiResponse<Portfolio> getPortfolioById(@PathVariable Long portfolioId) {

        Portfolio portfolio = portfolioModel.getPortfolioById(portfolioId);

        return ApiResponse.success("포트폴리오가 성공적으로 조회되었습니다.", portfolio);
    }

//    @GetMapping("/portfolios")
//    @Operation(
//            summary = "포트폴리오 조회",
//            description = "공개 포트폴리오를 대상으로 포트폴리오를 지역/경력/구직상태/키워드에 맞게 조회합니다."
//    )
//    public ApiResponse<List<Portfolio>> getPortfolios(
//            @Parameter(name = "region", description = "지역(정확 일치)")
//            @RequestParam(name = "region",   required = false) String region,
//
//            @Parameter(name = "career", description = "경력(부분 일치)")
//            @RequestParam(name = "career",   required = false) String career,
//
//            @Parameter(name = "isJobSeeking", description = "구직 상태 (true/false)")
//            @RequestParam(name = "isJobSeeking", required = false) Boolean isJobSeeking,
//
//            @Parameter(name = "q", description = "키워드(제목/한줄소개/경력, 부분 일치)")
//            @RequestParam(required = false) String q
//    ) {
//        List<PortfolioResponse> data = portfolioModel.searchAndFilterPublic(region, career, isJobSeeking, q);
//        return ApiResponse.success("포트폴리오 필터 조회 성공", data);
//    }


    // 포폴 수정
    @PutMapping(value = "/mypage/{portfolioId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "포트폴리오 수정",
            description = "포트폴리오를 수정합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<Long> updatePortfolio(
            @PathVariable("portfolioId") Long portfolioId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "progressPeriod", required = false) String progressPeriod,
            @RequestParam(value = "prize", required = false) Boolean prize,
            @RequestParam(value = "workDoneProgress", required = false) String workDoneProgress,
            @RequestParam(value = "result", required = false) String result,
            @RequestParam(value = "felt", required = false) String felt,
            @RequestParam(value = "isPrivate", required = false) Boolean isPrivate,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        Long updatedId = portfolioModel.updatePortfolio(
                portfolioId,
                title, progressPeriod, prize, workDoneProgress, result, felt,
                isPrivate, images, userPrincipal.getEmail()
        );
        return ApiResponse.success(updatedId);
    }

    @DeleteMapping("/mypage/{portfolioId}")
    @Operation(
            summary = "포트폴리오 삭제",
            description = "포트폴리오를 삭제합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<Void> deletePortfolio(
            @PathVariable("portfolioId") Long portfolioId,
            @AuthenticationPrincipal UserPrincipal authentication) {

        String email = authentication.getEmail();
        portfolioModel.deletePortfolio(portfolioId, email);

        return ApiResponse.success("포트폴리오가 삭제되었습니다.");
    }

}
