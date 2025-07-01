# Mamoru

Mamoru es una mascota virtual desarrollada en Java utilizando JavaFX. El usuario puede interactuar con Mamoru y gestionar sus estados como hambre, energía e higiene. Si alguno de estos llega a cero, Mamoru puede morir, por lo que requiere cuidado constante.

## Requisitos

- Java 17 o superior
- JavaFX correctamente configurado
- IntelliJ IDEA (recomendado)

## Configuración de JavaFX

Este proyecto necesita JavaFX para funcionar. Asegúrate de:

1. Descargar JavaFX desde: https://gluonhq.com/products/javafx/
2. Agregar la carpeta `lib/` de JavaFX como una librería externa en tu proyecto.
3. En `Run > Edit Configurations...`, añade lo siguiente en los **VM options**:

   --module-path /ruta/a/javafx/lib --add-modules javafx.controls,javafx.fxml

   (Reemplaza `/ruta/a/javafx/lib` con la ruta real en tu computadora).

## Cómo ejecutar

1. Clona este repositorio:
   git clone https://github.com/Gzm4n/Mamoru.git

2. Abre el proyecto en IntelliJ IDEA.
3. Asegúrate de que JavaFX esté configurado.
4. Ejecuta la clase `Main.java`.
