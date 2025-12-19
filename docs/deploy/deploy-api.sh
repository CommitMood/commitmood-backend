#!/bin/bash

set -e

echo "=== ECR Login ==="
aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 904923506932.dkr.ecr.ap-northeast-2.amazonaws.com

echo "=== Pull Latest Image ==="
docker-compose pull api

echo "=== Restart Containers ==="
docker-compose down api
docker-compose up -d api

echo "=== Clean Old Images ==="
docker image prune -af

echo "=== Deployment Complete ==="
docker-compose ps