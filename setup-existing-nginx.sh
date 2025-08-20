#!/bin/bash

echo "=== 기존 nginx를 활용한 설정을 시작합니다 ==="

# 1. nginx 설정 파일 복사
echo "nginx 설정 파일 생성 중..."
sudo cp nginx-host-config.conf /etc/nginx/sites-available/unibiz

# 2. 심볼릭 링크 생성
echo "site 활성화 중..."
sudo ln -sf /etc/nginx/sites-available/unibiz /etc/nginx/sites-enabled/unibiz

# 3. 기본 설정 비활성화 (선택사항)
sudo rm -f /etc/nginx/sites-enabled/default

# 4. Let's Encrypt 설치 (없다면)
echo "certbot 설치 중..."
sudo yum install certbot python3-certbot-nginx -y || sudo dnf install certbot python3-certbot-nginx -y

# 5. SSL 인증서 발급
echo "SSL 인증서 발급 중..."
sudo certbot --nginx -d unibiz.lion.it.kr --non-interactive --agree-tos --email your-email@example.com

# 6. nginx 설정 테스트
echo "nginx 설정 테스트 중..."
sudo nginx -t

# 7. nginx 재시작
echo "nginx 재시작 중..."
sudo systemctl restart nginx
sudo systemctl enable nginx

# 8. Docker Compose 실행 (nginx 없이)
echo "Docker 서비스 실행 중..."
sudo docker compose -f docker-compose.no-nginx.yml up -d

echo "=== 설정이 완료되었습니다! ==="
echo "브라우저에서 https://unibiz.lion.it.kr 에 접속해보세요."

# 상태 확인
echo "서비스 상태 확인:"
sudo systemctl status nginx
sudo docker compose -f docker-compose.no-nginx.yml ps