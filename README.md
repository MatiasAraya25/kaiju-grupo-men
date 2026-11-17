# Kaiju · Inventario (DSY1105 – Parcial 2)

App Android en Kotlin + Jetpack Compose (MVVM + Repository + Room) para el control de inventario de Kaiju. Datos 100% ficticios.

**Integrantes:** Matías Araya · Esteban Acuña · Natanael Valenzuela
**Trello:** https://trello.com/b/TFru9VV7/desarrollomobile

## Usuarios de prueba

| Usuario | Rol | Puede |
|---|---|---|
| Matias | ADMINISTRADOR | Crear/editar productos, registrar ENTRADA y AJUSTE, ver Alertas |
| Esteban | VENDEDOR | Registrar SALIDA_VENTA y SALIDA_CONSUMO |
| Natanael | ENCARGADO_INVENTARIO | Registrar los 4 tipos de movimiento, ver Alertas |

## Estructura

`model/` · `repository/` (Room: DAO + Database + Repository) · `viewmodel/` · `navigation/` · `ui/screen`, `ui/components`, `ui/theme` · `data/` (modelo y cliente de la API, EA3)

## EA3 · Easter egg: Pokédex secreta (PokéAPI + Retrofit)

### API elegida

[PokéAPI](https://pokeapi.co/): API pública de Pokémon, solo lectura (GET) y sin autenticación.

- **URL base:** `https://pokeapi.co/api/v2/`
- **Endpoint:** `GET pokemon/{nombre}`. Acepta el nombre (`rayquaza`) o el número (`384`).
- **Respuestas:** `200` con los datos del Pokémon · `404` si no existe.
- **Datos que mostramos:** número, nombre, imagen animada (`sprites.other.showdown.front_default`, un GIF; si no existe se usa el PNG `sprites.front_default`), tipos, altura (`height` viene en decímetros, se divide entre 10 para tener metros) y peso (`weight` viene en hectogramos, se divide entre 10 para tener kg).

### Cómo se activa

1. Iniciar sesión con cualquier usuario.
2. En el **Catálogo**, abrir el producto **K-013 · Polera Rayquaza** (se encuentra rápido buscando "rayquaza" o filtrando por *Videojuegos*).
3. En el detalle, **tocar 5 veces la imagen de la polera**. Los toques no tienen efecto visual, para que el secreto no se note.
4. Se abre la **Pokédex secreta**, que ya muestra a Rayquaza. Desde ahí se puede buscar cualquier otro Pokémon por nombre o número.

> Si la app ya estaba instalada, la K-013 se agrega sola al abrirla: `cargarDatosIniciales()` inserta los productos de prueba que falten con `OnConflictStrategy.IGNORE`, sin tocar el stock ni las ediciones de los que ya existen.

### Arquitectura (MVVM): del toque a la pantalla

```
PantallaDetalleProducto ──5 toques──▶ navega a Rutas.Pokedex
PantallaPokedex (View)       muestra uiState y envía eventos (escribir, Buscar, Reintentar)
   ▼
PokedexViewModel             valida la entrada, evita peticiones dobles, traduce errores y expone uiState (StateFlow)
   ▼
PokemonRepository            normaliza la entrada (trim + minúsculas) y guarda una caché en memoria
   ▼
PokeApi + PokeApiClient      Retrofit hace GET pokemon/{nombre}; Gson convierte el JSON en PokemonDto
   ▼
PokéAPI
```

| Capa | Archivo | Responsabilidad |
|---|---|---|
| Modelo | `data/model/PokemonDto.kt` | Clases Kotlin con la forma del JSON (`@SerializedName("front_default")`) |
| Remoto | `data/remote/PokeApi.kt`, `PokeApiClient.kt` | Interfaz Retrofit (`@GET` + `@Path`) y cliente único con timeout de 20 s |
| Repository | `repository/PokemonRepository.kt` | Pide los datos y guarda en caché por texto buscado, nombre e id (si buscas `rayquaza` y después `384`, no hay segunda petición) |
| ViewModel | `viewmodel/PokedexViewModel.kt`, `PokedexUiState.kt` | Estados de carga, resultado y error. Distingue el 404, otros errores HTTP, la falta de conexión (`IOException`) y el JSON inválido |
| View | `ui/screen/PantallaPokedex.kt` | Compose + Material 3. Coil (`AsyncImage`) carga la imagen. Botón Reintentar en el error |
| Activador | `ui/screen/PantallaDetalleProducto.kt`, `ui/components/ImagenProducto.kt` | Imagen de la polera y contador de toques (estado de la UI) |

**Peticiones controladas:** la consulta solo sale desde el `init` del ViewModel (la primera búsqueda de Rayquaza) o desde los botones Buscar y Reintentar, nunca desde el cuerpo de un composable. Por eso recomponer o rotar el celular no repite la petición. El ViewModel se crea dentro del destino `pokedex` del NavHost, así que se destruye al salir de la pantalla.

### Animación

La animación tiene dos partes:

1. **GIF de la API:** la PokéAPI entrega un sprite animado en `sprites.other.showdown.front_default`, que se modela con `OtrosSpritesDto` y `ShowdownDto`. Coil no decodifica GIF por defecto, así que en `MainActivity` se registra el decodificador una sola vez para toda la app (`setSingletonImageLoaderFactory`): `AnimatedImageDecoder` en Android 9 o superior y `GifDecoder` en versiones anteriores. Dependencia: `io.coil-kt.coil3:coil-gif`.
2. **Animación de Compose** (`PokemonAnimado` en `PantallaPokedex.kt`): el Pokémon entra volando desde la izquierda (`Animatable` + `tween` de 1,2 s) y después flota arriba y abajo sin parar (`rememberInfiniteTransition`). Se aplica con `graphicsLayer`, que mueve la imagen sin volver a medir el layout. `FilterQuality.None` mantiene el pixel art nítido al agrandarlo.

La entrada usa `LaunchedEffect(url)`. Es correcto porque solo mueve la imagen, no hace peticiones. Se repite únicamente cuando cambia el Pokémon buscado.

### Cómo probarla

| Prueba | Acción | Resultado esperado |
|---|---|---|
| Consulta exitosa | Activar el easter egg | Ficha #384 · rayquaza, tipos dragon y flying, 7.0 m, 206.5 kg, con imagen |
| Caché | Buscar `384` después de Rayquaza | La misma ficha aparece al instante, sin cargar |
| Normalización | Buscar `  PIKACHU ` | Ficha #25 · pikachu |
| Entrada inválida (vacía) | Borrar el texto y presionar Buscar | "Escribe un nombre o número." (no hace petición) |
| Entrada inválida (no existe) | Buscar `noexiste123` | "No encontré ese Pokémon…" (404) |
| Falla de conexión | Modo avión y buscar un Pokémon nuevo | "No pude conectar…" + Reintentar |
| Reintento | Quitar el modo avión y presionar Reintentar | Aparece la ficha y desaparece el error |
| Rotación | Girar el celular con una ficha en pantalla | Se mantiene la ficha (el ViewModel sobrevive) |

### Problemas encontrados y cómo se resolvieron

| Problema | Causa | Solución |
|---|---|---|
| `Module was compiled with an incompatible version of Kotlin… metadata 2.2.0, expected 2.0.0` | Coil 3.3.0 está compilado con Kotlin 2.2 y el proyecto usa Kotlin 2.0.21 (un compilador solo lee librerías de su versión o de la siguiente) | Usar Coil **3.0.4** (`coil` en `libs.versions.toml`) |
| La K-013 no aparecía en el catálogo | Los datos de prueba solo se cargaban con la base de datos vacía | `insertarFaltantes` con `OnConflictStrategy.IGNORE` en `ProductoDao`, llamado desde `cargarDatosIniciales()` |
| Falla `:app:kspDebugAndroidTestKotlin` al generar la APK (`concurrent-futures {strictly 1.1.0}`) | CameraX usa `concurrent-futures` 1.1.0 y los tests (espresso 3.7.0) piden 1.2.0. Los tests deben usar las mismas versiones que la app | Declarar `androidx.concurrent:concurrent-futures:1.2.0` en la app |

### Captura

<!-- Reemplazar por la captura real: guardarla en docs/easter_egg.png -->
![Pokédex secreta](docs/easter_egg.png)
