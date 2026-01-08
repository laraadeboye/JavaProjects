#!/bin/bash
javac --module-path /usr/share/openjfx/lib \
      --add-modules javafx.controls,javafx.fxml \
      *.java

java --module-path /usr/share/openjfx/lib \
     --add-modules javafx.controls,javafx.fxml \
     MainWindow
