#!/bin/bash

set -e

echo "=== ECR Login ==="
aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 904923506932.dkr.ecr.ap-northeast-2.amazonaws.com

echo "=== Pull Latest Image ==="
docker compose -f docker-compose.prod.yml pull api

echo "=== Restart Containers ==="
docker compose -f docker-compose.prod.yml down api
docker compose -f docker-compose.prod.yml up -d api

echo "=== Clean Old Images ==="
docker image prune -af

echo "=== Deployment Complete ==="
docker compose -f docker-compose.prod.yml ps