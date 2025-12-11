@echo off
echo Compilando Sistema Universidad...
if not exist "bin" mkdir bin

javac -encoding UTF-8 -d bin -cp "lib/*" src/com/universidad/model/*.java
javac -encoding UTF-8 -d bin -cp "lib/*;bin" ^
    src/com/universidad/dao/*.java ^
    src/com/universidad/service/*.java ^
    src/com/universidad/view/*.java ^
    src/com/universidad/*.java

if %errorlevel% == 0 (
    echo Compilacion exitosa!
) else (
    echo Error en compilacion
)
pause