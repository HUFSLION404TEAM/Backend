# 404 프로젝트 Docker Compose 설정 (Nginx + HTTPS)

## 개요
이 프로젝트는 Spring Boot 애플리케이션, MySQL 데이터베이스, Nginx 리버스 프록시, Let's Encrypt SSL 인증서를 Docker Compose로 관리합니다.

## 서비스 구성
- **MySQL**: 데이터베이스 (포트 3306)
- **Spring Boot App**: 백엔드 애플리케이션 (내부 포트 8080)
- **Nginx**: 리버스 프록시 + SSL 터미네이션 (포트 80, 443)
- **Certbot**: Let's Encrypt SSL 인증서 자동 발급

## 도메인 및 SSL 설정
- **도메인**: unibiz.lion.it.kr
- **서버 IP**: 3.39.235.21
- **SSL**: Let's Encrypt 자동 인증서
- **HTTPS 리다이렉트**: HTTP → HTTPS 자동 리다이렉트

## 파일 구조
```
404/
├── docker-compose.yml         # 전체 서비스 구성
├── nginx/
│   ├── nginx.conf            # Nginx 메인 설정
│   └── conf.d/
│       ├── default.conf      # HTTPS 가상호스트 설정
│       └── temp-http.conf    # 임시 HTTP 설정 (인증서 발급용)
├── ssl/                      # SSL 인증서 저장소
├── init.sql                  # MySQL 초기화 스크립트
├── setup-ssl.sh              # SSL 설정 자동화 스크립트
└── src/main/resources/
    └── application-prod.yml   # 프로덕션 설정 (HTTPS 도메인)
```

## 🚀 배포 방법

### 1. 자동 SSL 설정 (추천)
```bash
# SSL 설정 스크립트 실행 권한 부여
chmod +x setup-ssl.sh

# 자동 SSL 설정 실행
./setup-ssl.sh
```

### 2. 수동 설정
```bash
# 1. 기존 nginx 중지
sudo systemctl stop nginx

# 2. HTTP만 먼저 실행
sudo docker compose up -d mysql app nginx

# 3. SSL 인증서 발급 (이메일 주소 수정 필요)
sudo docker compose run --rm certbot

# 4. 전체 재시작
sudo docker compose down
sudo docker compose up -d
```

## 🔧 관리 명령어

### 서비스 관리
```bash
# 전체 서비스 실행
sudo docker compose up -d

# 서비스 상태 확인
sudo docker compose ps

# 로그 확인
sudo docker compose logs -f

# 특정 서비스 로그
sudo docker compose logs nginx
sudo docker compose logs app

# 서비스 중지
sudo docker compose down
```

### SSL 인증서 관리
```bash
# 인증서 갱신 (90일마다 필요)
sudo docker compose run --rm certbot renew

# 인증서 상태 확인
sudo docker compose exec nginx ls -la /etc/nginx/ssl/live/unibiz.lion.it.kr/

# nginx 설정 테스트
sudo docker compose exec nginx nginx -t

# nginx 재로드
sudo docker compose exec nginx nginx -s reload
```

### 데이터베이스 관리
```bash
# MySQL 컨테이너 접속
sudo docker compose exec mysql mysql -u myappuser -p

# 데이터베이스 백업
sudo docker compose exec mysql mysqldump -u myappuser -p unibiz > backup.sql

# 데이터베이스 복원
sudo docker compose exec -T mysql mysql -u myappuser -p unibiz < backup.sql
```

## 🌐 접속 정보

### 웹 접속
- **HTTPS**: https://unibiz.lion.it.kr
- **HTTP**: http://unibiz.lion.it.kr (자동으로 HTTPS로 리다이렉트)

### 데이터베이스 연결
- **Host**: 3.39.235.21:3306 (외부 접속) 또는 mysql:3306 (Docker 내부)
- **Database**: unibiz
- **Username**: myappuser
- **Password**: MyApp#1234

### OAuth 리다이렉트 URL
- **Kakao**: https://unibiz.lion.it.kr/login/oauth2/code/kakao
- **Naver**: https://unibiz.lion.it.kr/login/oauth2/code/naver
- **Google**: 자동 설정

## 🔒 보안 설정

### SSL/TLS 설정
- TLS 1.2, 1.3 지원
- HSTS (HTTP Strict Transport Security) 활성화
- 보안 헤더 자동 추가

### 방화벽 설정 (필요시)
```bash
# 포트 허용
sudo firewall-cmd --permanent --add-port=80/tcp
sudo firewall-cmd --permanent --add-port=443/tcp
sudo firewall-cmd --reload
```

## 🐛 트러블슈팅

### 1. SSL 인증서 발급 실패
```bash
# 도메인 DNS 설정 확인
nslookup unibiz.lion.it.kr

# Let's Encrypt 로그 확인
sudo docker compose logs certbot

# 수동 인증서 발급
sudo docker compose run --rm certbot certonly --manual --preferred-challenges dns -d unibiz.lion.it.kr
```

### 2. Nginx 502 Bad Gateway
```bash
# 애플리케이션 상태 확인
sudo docker compose ps

# 애플리케이션 로그 확인
sudo docker compose logs app

# 네트워크 연결 확인
sudo docker compose exec nginx ping app
```

### 3. 인증서 갱신 자동화
```bash
# cron 작업 추가 (매월 1일 인증서 갱신)
sudo crontab -e

# 다음 라인 추가:
0 0 1 * * cd /path/to/project && docker compose run --rm certbot renew && docker compose exec nginx nginx -s reload
```

## 📊 모니터링

### 상태 확인
```bash
# 서비스 상태
curl -I https://unibiz.lion.it.kr

# SSL 인증서 확인
openssl s_client -connect unibiz.lion.it.kr:443 -servername unibiz.lion.it.kr

# 성능 테스트
curl -w "@curl-format.txt" -o /dev/null -s https://unibiz.lion.it.kr
```

이제 `https://unibiz.lion.it.kr`로 안전하게 접속할 수 있습니다!