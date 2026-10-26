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

`model/` · `repository/` (Room: DAO + Database + Repository) · `viewmodel/` · `navigation/` · `ui/screen`, `ui/components`, `ui/theme`
