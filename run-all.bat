@echo off
setlocal

cd /d "%~dp0"

echo [1/4] Preparation des dependances...
if not exist "target\dependency\hsqldb-2.7.2.jar" (
  mvn dependency:copy-dependencies
  if errorlevel 1 (
    echo Echec: impossible de recuperer les dependances Maven.
    exit /b 1
  )
)

echo [2/4] Demarrage du serveur HSQLDB...
if not exist "data" mkdir "data"
start "HSQLDB Server" cmd /k "cd /d \"%~dp0data\" && java -cp ..\target\dependency\hsqldb-2.7.2.jar org.hsqldb.Server"

timeout /t 2 /nobreak >nul

echo [3/4] Ouverture du client HSQLDB...
start "HSQLDB Browser" cmd /k "cd /d \"%~dp0\" && java -cp .\target\dependency\hsqldb-2.7.2.jar org.hsqldb.util.DatabaseManagerSwing --driver org.hsqldb.jdbcDriver --url jdbc:hsqldb:hsql://localhost/ --user SA"

echo [4/4] Demarrage de l'API (Jetty)...
mvn jetty:run

