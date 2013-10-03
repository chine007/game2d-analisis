@echo off
cmd /c mvn -f ..\features\pom.xml install
cmd /c mvn dependency:copy-dependencies
pause