@echo off
cmd /c mvn -f ..\features\pom.xml install
cmd /c mvn compile dependency:copy-dependencies
pause