# 404 프로젝트 Docker Compose 설정

## 개요
이 프로젝트는 Spring Boot 애플리케이션과 MySQL 데이터베이스를 Docker Compose로 관리합니다.

## 파일 구조
```
404/
├── docker-compose.yml         # 프로덕션용 Docker Compose
├── docker-compose.local.yml   # 로컬 개발용 (MySQL만)
├── init.sql                   # MySQL 초기화 스크립트
├── Dockerfile                 # 애플리케이션 Docker 이미지 빌드
└── src/main/resources/
    ├── application.yml        # 로컬 개발 설정
    └── application-prod.yml   # 프로덕션 설정
```

## 사용 방법

### 1. 서버에서 전체 실행 (애플리케이션 + MySQL)
```bash
# 전체 서비스 실행
sudo docker-compose up -d

# 로그 확인
sudo docker-compose logs -f

# 서비스 중지
sudo docker-compose down
```

### 2. 로컬 개발용 (MySQL만 실행)
```bash
# MySQL만 실행
sudo docker-compose -f docker-compose.local.yml up -d

# 애플리케이션은 IDE에서 실행
# application.yml의 datasource.url을 localhost:3306으로 설정
```

### 3. 개별 컨테이너 관리
```bash
# 특정 서비스만 재시작
sudo docker-compose restart app
sudo docker-compose restart mysql

# 컨테이너 상태 확인
sudo docker-compose ps

# 특정 컨테이너 로그 확인
sudo docker-compose logs app
sudo docker-compose logs mysql
```

## 데이터베이스 연결 정보
- **Host**: mysql (Docker 네트워크 내) 또는 localhost (로컬)
- **Port**: 3306
- **Database**: unibiz
- **Username**: myappuser
- **Password**: MyApp#1234

## 환경 변수 설정
Docker Compose에서 다음 환경 변수가 자동으로 설정됩니다:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_DATASOURCE_DRIVER_CLASS_NAME`

## 주요 변경사항
1. **네트워크 분리**: MySQL과 애플리케이션이 동일한 Docker 네트워크를 사용
2. **데이터 영속성**: MySQL 데이터는 Docker Volume에 저장되어 컨테이너 재시작 시에도 유지
3. **초기화 스크립트**: `init.sql`을 통한 데이터베이스 초기 설정
4. **환경별 설정**: 로컬과 프로덕션 환경 분리

## 트러블슈팅

### 포트 충돌
```bash
# 기존 MySQL 서비스 확인
sudo systemctl status mysql
sudo systemctl stop mysql  # 필요시 중지
```

### 컨테이너 초기화
```bash
# 모든 데이터 삭제 후 재시작
sudo docker-compose down --volumes
sudo docker-compose up -d
```

### 데이터베이스 접속
```bash
# MySQL 컨테이너에 직접 접속
sudo docker-compose exec mysql mysql -u myappuser -p unibiz
```

## CI/CD 변경사항
GitHub Actions 워크플로우가 Docker Compose를 사용하도록 업데이트되었습니다:
- 기존 컨테이너 정리
- Docker Compose를 통한 서비스 실행
- 로그 및 상태 모니터링 개선