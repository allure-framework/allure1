@echo off

@REM ==== START VALIDATION ====
if not "%JAVA_HOME%" == "" goto OkJHome

echo.
echo Error: JAVA_HOME not found in your environment. >&2
echo Please set the JAVA_HOME variable in your environment to match the >&2
echo location of your Java installation. >&2
echo.
goto error

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" goto checkAllureHome

echo.
echo Error: JAVA_HOME is set to an invalid directory. >&2
echo JAVA_HOME = "%JAVA_HOME%" >&2
echo Please set the JAVA_HOME variable in your environment to match the >&2
echo location of your Java installation. >&2
echo.
goto error

:checkAllureHome
if not "%ALLURE_HOME%" == "" goto AllureHomeCurrentFolder

:AllureHomeCurrentFolder
SETLOCAL ENABLEEXTENSIONS
set ALLURE_HOME=%~dp0..
goto retryCheckAllureHome

:retryCheckAllureHome
if not "%ALLURE_HOME%" == "" goto OkAllureHome

echo.
echo Error: ALLURE_HOME not found in your environment. >&2
echo.
goto error

:OkAllureHome
SET ALLURE_COMMANDLINE_JAR="%ALLURE_HOME%\lib\allure-commandline.jar"
if exist %ALLURE_COMMANDLINE_JAR% goto init

echo.
echo Error: ALLURE_HOME is set to an invalid directory. >&2
echo ALLURE_HOME = "%ALLURE_HOME%" >&2
echo Please set the ALLURE_HOME variable in your environment to match the >&2
echo location of your Allure installation. >&2
echo.
goto error

:init
@REM Decide how to startup depending on the version of windows

@REM -- Windows NT with Novell Login
if "%OS%"=="WINNT" goto WinNTNovell

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

:WinNTNovell

@REM -- 4NT shell
if "%@eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set CMD_LINE_ARGS=%*
goto endInit

@REM The 4NT Shell from jp software
:4NTArgs
set CMD_LINE_ARGS=%$
goto endInit

:Win9xArg
@REM Slurp the command line arguments. This loop allows for an unlimited number
@REM of agruments (up to the command line limit, anyway).
set CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto endInit
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto Win9xApp

@REM Reaching here means variables are defined and arguments have been captured
:endInit
set JAVA_EXE="%JAVA_HOME%\bin\java.exe"
set ALLURE_MAIN="ru.yandex.qatools.allure.CommandLine"
set ALLURE_CP="%ALLURE_HOME%\lib\*;%ALLURE_HOME%\conf"
set ALLURE_ARGS=

call :add_system_property_arg "allure.bundle.javaOpts" "%ALLURE_BUNDLE_JAVA_OPTS%"
call :add_system_property_arg "allure.config" "%ALLURE_CONFIG%"
call :add_system_property_arg "allure.home" "%ALLURE_HOME%"

if ERRORLEVEL 1 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
if "%OS%"=="WINNT" @endlocal
set ERROR_CODE=1

:add_system_property_arg
if not "%~2"=="" set ALLURE_ARGS=%ALLURE_ARGS% -D%~1=%~2
goto:EOF

:end
%JAVA_EXE% %ALLURE_ARGS% -cp %ALLURE_CP% %ALLURE_MAIN% %CMD_LINE_ARGS%