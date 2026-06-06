@echo off
echo Compilation des fichiers Java...
if not exist bin mkdir bin
powershell -Command "$files = Get-ChildItem -Path 'src' -Filter *.java -Recurse | Select-Object -ExpandProperty FullName; if ($files) { & javac --module-path 'javafx-sdk-21.0.2\lib' --add-modules javafx.controls,javafx.fxml -cp 'lib\sqlite-jdbc-3.42.0.0.jar' -d bin $files }"

echo Lancement de l'application MASI JavaFX...
java --module-path "javafx-sdk-21.0.2\lib" --add-modules javafx.controls,javafx.fxml -cp "bin;lib\sqlite-jdbc-3.42.0.0.jar" App
pause
