# Rackalerta

Aplicación Android (Kotlin + Jetpack Compose) para revisar el estado de racks de servidores.
Proyecto semestral – Evaluación 2, Duoc UC.

## Funcionalidades

- **Login** con usuario y contraseña. Las credenciales se guardan en Room.
- **Listado de racks** con los 5 racks iniciales (definidos en código) y el estado de cada uno.
- **Nuevo rack / actualizar rack**: primero se toma la foto con la cámara. Al tomarla se asigna
  un estado aleatorio (Disponible o Crítico) y aparece una alerta animada. Luego se completan
  nombre y número, y se confirma en un modal antes de guardar.
- **Detalle del rack** con foto, número y estado.
- **Perfil** con datos del usuario y credencial digital.
- **Cambio de contraseña** con validación y modal de confirmación.

## Credenciales de prueba

- Usuario: `admin`
- Contraseña: `123456`

## Uso de recursos nativos

La cámara se usa con `ActivityResultContracts.TakePicture` y un `FileProvider`
para que la foto quede guardada en la app. Se declara `uses-feature` de cámara
como opcional.

## Requisitos

- Android Studio con soporte para AGP 9.2 y Kotlin 2.2.10.
- minSdk 24, targetSdk 36.

## Pruebas

```
./gradlew test
```

Las pruebas unitarias cubren las reglas de validación del login y del formulario de racks.
