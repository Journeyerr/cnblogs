#!/bin/bash

# API测试脚本
BASE_URL="http://localhost:8080"

echo "=== Hospital Admin API 测试 ==="
echo ""

# 1. 测试公开接口
echo "1. 测试公开接口..."
curl -X GET "$BASE_URL/api/test/public" \
  -H "Content-Type: application/json"
echo ""
echo ""

# 2. 测试登录（正确密码）
echo "2. 测试用户登录（正确密码）..."
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "123456"
  }')

echo "$LOGIN_RESPONSE"
echo ""

# 提取token
TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

if [ -n "$TOKEN" ]; then
    echo "获取到Token: ${TOKEN:0:50}..."
    echo ""

    # 3. 测试受保护接口
    echo "3. 测试受保护接口..."
    curl -X GET "$BASE_URL/api/test/protected" \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer $TOKEN"
    echo ""
    echo ""

    # 4. 测试获取用户信息
    echo "4. 测试获取用户信息..."
    curl -X GET "$BASE_URL/api/auth/info" \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer $TOKEN"
    echo ""
    echo ""

    # 5. 测试注销
    echo "5. 测试用户注销..."
    curl -X POST "$BASE_URL/api/auth/logout" \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer $TOKEN"
    echo ""
    echo ""

    # 6. 测试注销后的token是否失效
    echo "6. 测试注销后的token是否失效..."
    curl -X GET "$BASE_URL/api/test/protected" \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer $TOKEN"
    echo ""
    echo ""
else
    echo "登录失败，无法获取Token"
fi

# 7. 测试登录（错误密码）
echo "7. 测试用户登录（错误密码）..."
curl -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "wrongpassword"
  }'
echo ""
echo ""

# 8. 测试禁用用户登录
echo "8. 测试禁用用户登录..."
curl -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test",
    "password": "123456"
  }'
echo ""
echo ""

# 9. 测试无token访问受保护接口
echo "9. 测试无token访问受保护接口..."
curl -X GET "$BASE_URL/api/test/protected" \
  -H "Content-Type: application/json"
echo ""
echo ""

# 10. 测试角色权限
echo "10. 测试角色权限..."
echo "10.1 测试超级管理员接口..."
curl -X GET "$BASE_URL/api/test/super-admin" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN"
echo ""
echo ""

echo "10.2 测试医生接口..."
curl -X GET "$BASE_URL/api/test/doctor" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN"
echo ""
echo ""

echo "10.3 测试护士接口..."
curl -X GET "$BASE_URL/api/test/nurse" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN"
echo ""
echo ""

# 11. 测试具体权限
echo "11. 测试具体权限..."
echo "11.1 测试用户查询权限..."
curl -X GET "$BASE_URL/api/test/user-query" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN"
echo ""
echo ""

echo "11.2 测试患者查询权限..."
curl -X GET "$BASE_URL/api/test/patient-query" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN"
echo ""
echo ""

echo "11.3 测试患者管理权限..."
curl -X GET "$BASE_URL/api/test/patient-manage" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN"
echo ""
echo ""

# 12. 测试角色管理接口
echo "12. 测试角色管理接口..."
echo "12.1 获取角色列表..."
curl -X GET "$BASE_URL/api/role/list" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN"
echo ""
echo ""

# 13. 测试权限管理接口
echo "13. 测试权限管理接口..."
echo "13.1 获取权限列表..."
curl -X GET "$BASE_URL/api/permission/list" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN"
echo ""
echo ""

echo "13.2 获取权限树..."
curl -X GET "$BASE_URL/api/permission/tree" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN"
echo ""
echo ""

echo "=== 测试完成 ===" 