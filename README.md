# Rackalerta

Aplicación Android (Kotlin + Jetpack Compose) para revisar el estado de racks de servidores.
Proyecto semestral – Duoc UC.

## Funcionalidades

- **Registro e inicio de sesión de clientes** con correo y contraseña contra Xano.
- **Sesión persistente (como YouTube):** el token se guarda en Room. Si la app se cierra o se
  elimina de segundo plano, al abrirla de nuevo entra directo al listado. Solo se sale con
  "Cerrar sesión" o si el token expira.
- **Listado de racks** descargado desde Xano, con botón para recargar.
- **Nuevo rack / actualizar rack**: se toma la foto con la cámara, se asigna un estado aleatorio
  (Disponible o Crítico) con una alerta animada, se completan nombre y número y se confirma en
  un modal. El rack y su foto se suben a Xano.
- **Detalle del rack** con foto, número, estado, encargado y opción de eliminar (con modal de confirmación).
- **Encargado del rack:** quien crea un rack queda como su encargado. Todos ven todos los racks,
  pero solo el encargado puede actualizarlo o eliminarlo (Xano también lo valida).
- **Perfil** con los datos del cliente y credencial digital.

## Endpoints de Xano (7)

| # | Método | Ruta | Uso |
|---|---|---|---|
| 1 | POST | `/auth/signup` | Registrar cliente (`name`, `email`, `password`) |
| 2 | POST | `/auth/login` | Iniciar sesión (`email`, `password`) → `authToken` |
| 3 | GET | `/auth/me` | Datos del cliente con sesión iniciada |
| 4 | GET | `/racks` | Listar racks |
| 5 | POST | `/racks` | Crear rack (multipart, requiere sesión, guarda al encargado) |
| 6 | PATCH | `/racks/{racks_id}` | Actualizar rack (multipart, solo el encargado) |
| 7 | DELETE | `/racks/{racks_id}` | Eliminar rack (solo el encargado) |

### Configuración en Xano

1. **Tabla `user`** (la de autenticación de Xano): `name`, `email`, `password`, `cargo`.
2. **Tabla `racks`**: `nombre` (text), `numero` (integer), `estado` (text: `DISPONIBLE` o `CRITICO`),
   `foto` (image), `encargado_id` (integer) y `encargado_nombre` (text).
3. En **POST /racks** y **PATCH /racks/{racks_id}** el input `foto` debe ser de tipo *file resource*
   (opcional) y antes de guardar se usa la función **Create Image Metadata** sobre ese input.
   La app envía `nombre`, `numero`, `estado` y `foto` como `multipart/form-data`.
4. Para que la sesión dure como en YouTube, en `/auth/login` y `/auth/signup` subir la
   expiración del token (`expiration`) a, por ejemplo, `2592000` segundos (30 días).
5. Las URL base de cada grupo de API están en
   `app/src/main/java/com/duoc/rackalerta/data/remote/ConfiguracionXano.kt`:

```kotlin
const val URL_CLIENTES = "https://x8ki-letl-twmt.n7.xano.io/api:4RlybLwy/"
const val URL_RACKS = "https://x8ki-letl-twmt.n7.xano.io/api:0otTBzc-/"
```

## Usuarios de prueba (en Xano)

| Nombre | Cargo | Correo | Contraseña | Racks a cargo |
|---|---|---|---|---|
| Camila Rojas | Jefa de Data Center | camila.rojas@rackalerta.cl | Camila2026 | Rack Core Principal (#1), Rack Almacenamiento (#2) |
| Matías Fuentes | Técnico de Redes | matias.fuentes@rackalerta.cl | Matias2026 | Rack Switches Piso 2 (#3) |
| Valentina Soto | Ingeniera de Infraestructura | valentina.soto@rackalerta.cl | Valentina2026 | Rack Servidores Web (#4), Rack Respaldo (#5) |

## Arquitectura

```
data/
├── local/    Room: sesión del cliente (token, nombre, correo)
└── remote/   Retrofit: endpoints de Xano, modelos y manejo de errores
repository/   RepositorioUsuario (clientes + sesión) y RepositorioRacks (CRUD)
viewmodel/    Un ViewModel por pantalla
ui/           Vistas en Compose
```

## Uso de recursos nativos

La cámara se usa con `ActivityResultContracts.TakePicture` y un `FileProvider`.
La foto se comprime antes de subirla a Xano. Se declara `uses-feature` de cámara como opcional.

## Requisitos

- Android Studio con soporte para AGP 9.2 y Kotlin 2.2.10.
- minSdk 24, targetSdk 37.
- Conexión a internet.

## Pruebas

```
./gradlew test
```

- `ValidadorLoginTest`: validación de correo, contraseña, nombre y registro.
- `ValidadorFormularioRackTest`: validación del formulario de racks y conversión del estado
  que llega desde Xano.
