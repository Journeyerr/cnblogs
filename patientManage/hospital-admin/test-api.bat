@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

set BASE_URL=http://localhost:8080

echo === Hospital Admin API 测试 ===
echo.

echo 1. 测试公开接口...
curl -X GET "%BASE_URL%/api/test/public" -H "Content-Type: application/json"
echo.
echo.

echo 2. 测试用户登录（正确密码）...
for /f "delims=" %%i in ('curl -s -X POST "%BASE_URL%/api/auth/login" -H "Content-Type: application/json" -d "{\"username\": \"admin\", \"password\": \"123456\"}"') do set LOGIN_RESPONSE=%%i

echo !LOGIN_RESPONSE!
echo.

REM 提取token（简化版本）
for /f "tokens=2 delims=:" %%a in ('echo !LOGIN_RESPONSE! ^| findstr "token"') do set TOKEN=%%a
set TOKEN=!TOKEN:"=!
set TOKEN=!TOKEN:,=!

if not "!TOKEN!"=="" (
    echo 获取到Token: !TOKEN:~0,50!...
    echo.

    echo 3. 测试受保护接口...
    curl -X GET "%BASE_URL%/api/test/protected" -H "Content-Type: application/json" -H "Authorization: Bearer !TOKEN!"
    echo.
    echo.

    echo 4. 测试获取用户信息...
    curl -X GET "%BASE_URL%/api/auth/info" -H "Content-Type: application/json" -H "Authorization: Bearer !TOKEN!"
    echo.
    echo.

    echo 5. 测试用户注销...
    curl -X POST "%BASE_URL%/api/auth/logout" -H "Content-Type: application/json" -H "Authorization: Bearer !TOKEN!"
    echo.
    echo.

    echo 6. 测试注销后的token是否失效...
    curl -X GET "%BASE_URL%/api/test/protected" -H "Content-Type: application/json" -H "Authorization: Bearer !TOKEN!"
    echo.
    echo.
) else (
    echo 登录失败，无法获取Token
)

echo 7. 测试用户登录（错误密码）...
curl -X POST "%BASE_URL%/api/auth/login" -H "Content-Type: application/json" -d "{\"username\": \"admin\", \"password\": \"wrongpassword\"}"
echo.
echo.

echo 8. 测试禁用用户登录...
curl -X POST "%BASE_URL%/api/auth/login" -H "Content-Type: application/json" -d "{\"username\": \"test\", \"password\": \"123456\"}"
echo.
echo.

echo 9. 测试无token访问受保护接口...
curl -X GET "%BASE_URL%/api/test/protected" -H "Content-Type: application/json"
echo.
echo.

echo === 测试完成 ===
pause 