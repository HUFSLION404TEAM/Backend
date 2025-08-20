#!/bin/bash

# SSL 인증서 설정 스크립트

echo "=== SSL 인증서 설정을 시작합니다 ==="

# 1. 기본 디렉토리 생성
echo "디렉토리 생성 중..."
mkdir -p nginx/conf.d
mkdir -p ssl

# 2. 기존 nginx 서비스 중지
echo "기존 nginx 서비스 중지 중..."
sudo systemctl stop nginx || echo "nginx 서비스가 실행 중이지 않습니다."

# 3. Docker Compose로 HTTP만 먼저 실행 (Let's Encrypt 인증을 위해)
echo "HTTP 서버 임시 실행 중..."
sudo docker compose up -d mysql app nginx

# 4. Let's Encrypt 인증서 발급 대기
echo "30초 대기 중... (nginx 초기화 완료 대기)"
sleep 30

# 5. SSL 인증서 발급
echo "SSL 인증서 발급 중..."
sudo docker compose run --rm certbot

# 6. SSL 인증서 권한 설정
echo "SSL 인증서 권한 설정 중..."
sudo chmod -R 755 ssl/

# 7. 전체 서비스 재시작
echo "전체 서비스 재시작 중..."
sudo docker compose down
sudo docker compose up -d

echo "=== SSL 설정이 완료되었습니다! ==="
echo "브라우저에서 https://unibiz.lion.it.kr 에 접속해보세요."

# 8. 상태 확인
echo "서비스 상태 확인:"
sudo docker compose ps