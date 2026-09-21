@echo off
setlocal

set "JAVA_HOME=%USERPROFILE%\.jdks\corretto-25.0.4.1"
set "PATH=%JAVA_HOME%\bin;%PATH%"

call "%~dp0mvnw.cmd" ^
  -s "%USERPROFILE%\.m2\settings-tonino.xml" ^
  -Dmaven.repo.local="%USERPROFILE%\.m2\repository" ^
  clean deploy ^
  -DskipTests ^
  -pl .,libs/common-core,libs/common-graphql,libs/common-jpa,libs/common-keycloak,libs/common-people-core,libs/common-people-graphql-jpa,libs/common-people-rest-jpa,libs/common-rest ^
  -f "%~dp0pom.xml"
