@echo off
rem cmd /c mvn -f ..\features\pom.xml install
cmd /c mvn -f ..\pom.xml install
cmd /c mvn compile dependency:copy-dependencies
pause