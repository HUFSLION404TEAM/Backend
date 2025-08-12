package hufs.lion.team404.controller;

import hufs.lion.team404.domain.dto.request.PortfolioCreateRequestDto;
import hufs.lion.team404.domain.dto.request.PortfolioUpdateRequestDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "포트폴리오", description = "포트폴리오 관련 API")
public class PortfolioController {
    private final PortfolioModel portfolioModel;

    // 포폴 생성
    // 온보딩에서 만들어지는 최초 포폴
    @PostMapping("/mypage/portfolios")
    @Operation(
            summary = "포트폴리오 생성",
            description = "새로운 포트폴리오를 생성합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<Void> createPortfolio(
            @AuthenticationPrincipal UserPrincipal authentication,
            @Valid @RequestBody PortfolioCreateRequestDto portfolioCreateRequestDto) {

        String email = authentication.getEmail();
        portfolioModel.createPortfolio(portfolioCreateRequestDto, email);

        return ApiResponse.success("포트폴리오가 성공적으로 생성되었습니다.");
    }

    @GetMapping("/mypage")
    @Operation(
            summary = "내 포트폴리오 조회",
            description = "내 포트폴리오를 조회합니다."
    )
    public ApiResponse<List<PortfolioResponse>> getMyPortfolios(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        List<PortfolioResponse> portfolios = portfolioModel.getMyPortfolios(email);

        return ApiResponse.success("나의 포트폴리오가 성공적으로 조회되었습니다.", portfolios);
    }

    @GetMapping("/portfolios")
    @Operation(
            summary = "포트폴리오 조회",
            description = "공개 포트폴리오를 대상으로 포트폴리오를 지역/경력/구직상태/키워드에 맞게 조회합니다."
    )
    public ApiResponse<List<PortfolioResponse>> getPortfolios(
            @Parameter(name = "region", description = "지역(정확 일치)")
            @RequestParam(name = "region",   required = false) String region,

            @Parameter(name = "career", description = "경력(부분 일치)")
            @RequestParam(name = "career",   required = false) String career,

            @Parameter(name = "isJobSeeking", description = "구직 상태 (true/false)")
            @RequestParam(name = "isJobSeeking", required = false) Boolean isJobSeeking,

            @Parameter(name = "q", description = "키워드(제목/한줄소개/경력, 부분 일치)")
            @RequestParam(required = false) String q
    ) {
        List<PortfolioResponse> data = portfolioModel.searchAndFilterPublic(region, career, isJobSeeking, q);
        return ApiResponse.success("포트폴리오 필터 조회 성공", data);
    }


    // 포폴 수정
    @PutMapping("/mypage/{portfolioId}")
    @Operation(
            summary = "포트폴리오 수정",
            description = "포트폴리오를 수정합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    public ApiResponse<Void> updatePortfolio(
            @PathVariable("portfolioId") Long portfolioId,
            @AuthenticationPrincipal UserPrincipal authentication,
            @Valid @RequestBody PortfolioUpdateRequestDto portfolioUpdateRequestDto) {

        String email = authentication.getEmail();
        portfolioModel.updatePortfolio(portfolioId, portfolioUpdateRequestDto, email);

        return ApiResponse.success("포트폴리오가 성공적으로 수정되었습니다.");
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
