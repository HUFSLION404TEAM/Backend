package hufs.lion.team404.controller;

import hufs.lion.team404.domain.dto.response.ApiResponse;
import hufs.lion.team404.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@RestController
@RequestMapping("/api/database")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "데이터베이스", description = "데이터베이스 관리 API")
public class DatabaseController {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    private final DataSource dataSource;

    @DeleteMapping("/clear")
    @Operation(
        summary = "전체 데이터베이스 초기화",
        description = "⚠️ 주의: 모든 데이터가 삭제됩니다! 개발/테스트 환경에서만 사용하세요.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @Transactional
    public ApiResponse<Void> clearAllData() {

        try {
            log.warn("🚨 데이터베이스 전체 초기화 시작 - Profile: {}", activeProfile);

            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {

                // 외래키 제약 조건 비활성화 (H2)
                if (isH2Database(connection)) {
                    statement.execute("SET FOREIGN_KEY_CHECKS = 0");
                } else if (isMySQLDatabase(connection)) {
                    // MySQL의 경우
                    statement.execute("SET foreign_key_checks = 0");
                }

                // 테이블 삭제 순서 (외래키 의존성 고려)
                String[] tables = {
                    "favorites",
                    "reports", 
                    "reviews",
                    "notifications",
                    "ai_matching_summaries",
                    "payment_history",
                    "payments",
                    "chat_messages",
                    "matching",
                    "application_files",
                    "applications",
                    "portfolio_images", 
                    "portfolios",
                    "project_request_files",
                    "project_requests",
                    "recruiting_images",
                    "recruitings",
                    "chat_rooms",
                    "students",
                    "stores", 
                    "users"
                };

                int deletedRows = 0;
                for (String table : tables) {
                    try {
                        int rows = statement.executeUpdate("DELETE FROM " + table);
                        deletedRows += rows;
                        log.info("테이블 {} 삭제 완료: {} 행", table, rows);
                    } catch (Exception e) {
                        log.warn("테이블 {} 삭제 중 오류 (무시됨): {}", table, e.getMessage());
                    }
                }

                // AUTO_INCREMENT 초기화
                for (String table : tables) {
                    try {
                        if (isH2Database(connection)) {
                            statement.execute("ALTER TABLE " + table + " ALTER COLUMN id RESTART WITH 1");
                        } else if (isMySQLDatabase(connection)) {
                            statement.execute("ALTER TABLE " + table + " AUTO_INCREMENT = 1");
                        }
                    } catch (Exception e) {
                        // ID가 없는 테이블이거나 오류 발생시 무시
                        log.debug("테이블 {} AUTO_INCREMENT 초기화 실패 (무시됨): {}", table, e.getMessage());
                    }
                }

                // 외래키 제약 조건 재활성화
                if (isH2Database(connection)) {
                    statement.execute("SET FOREIGN_KEY_CHECKS = 1");
                } else if (isMySQLDatabase(connection)) {
                    statement.execute("SET foreign_key_checks = 1");
                }

                log.warn("🔥 데이터베이스 초기화 완료 - 총 {} 행 삭제", deletedRows);
                return ApiResponse.success(
                    String.format("데이터베이스가 성공적으로 초기화되었습니다. (총 %d 행 삭제)", deletedRows)
                );

            }

        } catch (Exception e) {
            log.error("데이터베이스 초기화 중 오류 발생", e);
            return ApiResponse.failure("데이터베이스 초기화 중 오류가 발생했습니다: " + e.getMessage(), null);
        }
    }

    @GetMapping("/status")
    @Operation(
        summary = "데이터베이스 상태 확인",
        description = "현재 데이터베이스의 테이블별 데이터 수를 확인합니다."
    )
    public ApiResponse<String> getDatabaseStatus() {
        
        try {
            StringBuilder status = new StringBuilder();
            status.append("=== 데이터베이스 상태 ===\n");
            status.append("Profile: ").append(activeProfile).append("\n\n");

            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {

                String[] tables = {
                    "users", "students", "stores", "chat_rooms", "chat_messages",
                    "recruitings", "recruiting_images", "project_requests", "project_request_files",
                    "portfolios", "portfolio_images", "applications", "application_files",
                    "matching", "reviews", "reports", "favorites", "notifications", 
                    "payments", "payment_history", "ai_matching_summaries"
                };

                int totalRows = 0;
                for (String table : tables) {
                    try {
                        var resultSet = statement.executeQuery("SELECT COUNT(*) FROM " + table);
                        if (resultSet.next()) {
                            int count = resultSet.getInt(1);
                            totalRows += count;
                            status.append(String.format("%-25s: %6d 행\n", table, count));
                        }
                    } catch (Exception e) {
                        status.append(String.format("%-25s: %s\n", table, "조회 실패"));
                    }
                }

                status.append("\n총 데이터: ").append(totalRows).append(" 행");
            }

            return ApiResponse.success("데이터베이스 상태를 조회했습니다.", status.toString());

        } catch (Exception e) {
            log.error("데이터베이스 상태 조회 중 오류 발생", e);
            return ApiResponse.failure("데이터베이스 상태 조회 중 오류가 발생했습니다: " + e.getMessage(), null);
        }
    }

    private boolean isH2Database(Connection connection) {
        try {
            return connection.getMetaData().getURL().startsWith("jdbc:h2:");
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isMySQLDatabase(Connection connection) {
        try {
            return connection.getMetaData().getURL().startsWith("jdbc:mysql:");
        } catch (Exception e) {
            return false;
        }
    }
}
