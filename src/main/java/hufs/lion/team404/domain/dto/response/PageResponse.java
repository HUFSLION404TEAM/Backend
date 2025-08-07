package hufs.lion.team404.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 페이징 응답 클래스
 * @param <T> 데이터 타입
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    
    private List<T> content;        // 실제 데이터 목록
    private int page;               // 현재 페이지 번호 (0부터 시작)
    private int size;               // 페이지 크기
    private long totalElements;     // 전체 요소 개수
    private int totalPages;         // 전체 페이지 개수
    private boolean first;          // 첫 번째 페이지 여부
    private boolean last;           // 마지막 페이지 여부
    private boolean hasNext;        // 다음 페이지 존재 여부
    private boolean hasPrevious;    // 이전 페이지 존재 여부
    
    /**
     * Spring Data Page 객체로부터 PageResponse 생성
     */
    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isFirst(),
            page.isLast(),
            page.hasNext(),
            page.hasPrevious()
        );
    }
    
    /**
     * 성공 응답 생성 메서드
     */
    public static <T> ApiResponse<PageResponse<T>> success(Page<T> page) {
        return ApiResponse.success("목록을 성공적으로 조회했습니다.", PageResponse.of(page));
    }
    
    public static <T> ApiResponse<PageResponse<T>> success(String message, Page<T> page) {
        return ApiResponse.success(message, PageResponse.of(page));
    }
}
