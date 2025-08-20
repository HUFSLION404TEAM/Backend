-- MySQL 초기화 스크립트
-- 데이터베이스와 사용자가 이미 환경변수로 생성되므로 추가 설정만 진행

-- UTF8 설정 확인
ALTER DATABASE unibiz CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 권한 재확인 (이미 환경변수로 설정되지만 명시적으로 추가)
GRANT ALL PRIVILEGES ON unibiz.* TO 'myappuser'@'%';
FLUSH PRIVILEGES;

-- 기본 테이블 생성 (필요시)
-- 여기에 초기 테이블 생성 SQL을 추가할 수 있습니다.