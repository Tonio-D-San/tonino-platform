@echo off
setlocal

rem Usage: mvn-deploy.cmd [verify|deploy]
set "DEPLOY_GOAL=%~1"
if not defined DEPLOY_GOAL set "DEPLOY_GOAL=deploy"
if /I "%DEPLOY_GOAL%"=="verify" goto validGoal
if /I "%DEPLOY_GOAL%"=="deploy" goto validGoal
echo Usage: mvn-deploy.cmd [verify^|deploy]
exit /b 2

:validGoal
if not defined JAVA_HOME set "JAVA_HOME=%USERPROFILE%\.jdks\corretto-25.0.4.1"
set "PATH=%JAVA_HOME%\bin;%PATH%"
if not defined MAVEN_SETTINGS_FILE set "MAVEN_SETTINGS_FILE=%USERPROFILE%\.m2\settings-tonino.xml"

rem Build the complete reactor with tests. deployAtEnd is configured in the parent POM.
call "%~dp0mvnw.cmd" ^
  -s "%MAVEN_SETTINGS_FILE%" ^
  -Dmaven.repo.local="%USERPROFILE%\.m2\repository" ^
  -f "%~dp0pom.xml" ^
  clean %DEPLOY_GOAL%
set "DEPLOY_EXIT_CODE=%ERRORLEVEL%"
if not "%DEPLOY_EXIT_CODE%"=="0" echo Build/deploy failed. If uploads already started, use a new release version; see RELEASING.md.
exit /b %DEPLOY_EXIT_CODE%
