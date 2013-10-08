@echo off
rm target -r
cmd /c mvn -f ..\pom.xml install
cmd /c mvn dependency:copy-dependencies
pause