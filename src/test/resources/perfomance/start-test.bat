@echo off
SET PATH=%PATH%;C:\\SOFTWARE\\apache-jmeter-5.1.1\\bin

set CURRENTDATE=%date:~6,4%-%date:~3,2%-%date:~0,2%-%time:~0,2%-%time:~3,2%-%time:~6,2%

set RESULT_FILE=jmeter-%CURRENTDATE%.jtl
set LOG_FILE=jmeter-%CURRENTDATE%.log

jmeter -n -t SD-CORR-CAM.jmx -l %RESULT_FILE% -j %LOG_FILE%  -p user.properties
