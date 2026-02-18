@echo off
setlocal enabledelayedexpansion

:START
set /p TYPE="Enter type: "

if not defined TYPE (
    echo Please enter deploy type
    goto START
)

if /i "%TYPE%"=="FIX" (
    set /p TEXT="Enter text: "
    git add --all
    git commit -m "Fix (!TEXT!)"
    git push -u origin main
    echo Task complete
    goto END
)

if /i "%TYPE%"=="UPDATE" (
    set /p TEXT="Enter text: "
    git add --all
    git commit -m "Update (!TEXT!)"
    git push -u origin main
    echo Task complete
    goto END
)

if /i "%TYPE%"=="PROD" (
    set /p TEXT="Enter text: "
    git add --all
    git commit -m "Production"
    git push -u origin main
    echo Task complete
    goto END
)

if /i "%TYPE%"=="INIT" (
    set /p NAME="Enter repositorie name: "
    git init
    git add --all
    git commit -m "Init"
    git branch -M main
    git remote remove origin 2>nul
    git remote add origin "!NAME!"
    git push -u origin main
    echo Task complete
    goto END
)

if /i "%TYPE%"=="DEV" (
    set /p TEXT="Enter text: "
    git add --all
    git commit -m "Dev (!TEXT!)"
    git push -u origin main
    echo Task complete
    goto END
)

echo Invalid type
goto START

:END
endlocal