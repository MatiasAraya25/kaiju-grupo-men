package repository

import model.Movimiento
import model.Producto
import model.Rol
import model.TipoMovimiento
import model.Usuario


object DatosDePrueba {

    val usuarios = listOf(
        Usuario(1, "Matias", Rol.ADMINISTRADOR),
        Usuario(2, "Esteban", Rol.VENDEDOR),
        Usuario(3, "Natanael", Rol.ENCARGADO_INVENTARIO)
    )

    val productos = listOf(
        Producto(
            "K-001",
            "Polera Samurái Neón",
            "Estampado neón sobre algodón negro",
            "Anime",
            "Polera",
            14990,
            25,
            8,
            "Talla M · Negro"
        ),
        Producto("K-002", "Polera Ninja de Sombra", "Silueta ninja en tonos grises", "Anime", "Polera", 14990, 4, 8, "Talla L · Gris"),
        Producto("K-003", "Polera Dragón Celestial", "Dragón oriental en tinta blanca", "Anime", "Polera", 16990, 18, 6, "Talla S · Blanco"),
        Producto("K-004", "Polera Mecha Prototipo", "Robot gigante estilo plano técnico", "Anime", "Polera", 15990, 2, 5, "Talla XL · Azul"),
        Producto("K-005", "Polera Panel Manga Clásico", "Viñetas en blanco y negro", "Manga", "Polera", 13990, 30, 10, "Talla M · Blanco"),
        Producto("K-006", "Polera Onomatopeya Impacto", "Letras de impacto estilo cómic japonés", "Manga", "Polera", 13990, 9, 10, "Talla L · Negro"),
        Producto("K-007", "Polera Mangaka en Acción", "Ilustración de dibujante con tinta", "Manga", "Polera", 14490, 14, 5, "Talla M · Crema"),
        Producto("K-008", "Polera Píxel Héroe 8-bit", "Héroe de pixel art retro", "Videojuegos", "Polera", 12990, 40, 12, "Talla L · Verde"),
        Producto("K-009", "Polera Joystick Retro", "Control clásico en línea simple", "Videojuegos", "Polera", 12990, 6, 6, "Talla M · Rojo"),
        Producto("K-010", "Polera Boss Final", "Jefe final con diseño oscuro", "Videojuegos", "Polera", 15490, 11, 4, "Talla XL · Negro"),
        Producto("K-011", "Polera Nivel Secreto", "Mapa de mazmorra oculto", "Videojuegos", "Polera", 15490, 0, 3, "Talla S · Morado"),
        Producto("K-012", "Polerón Kaiju Gigante", "Monstruo gigante bordado", "Anime", "Polerón", 29990, 7, 3, "Talla L · Morado")
    )

    val movimientos = listOf(
        Movimiento(
            1,
            "K-001",
            TipoMovimiento.ENTRADA,
            30,
            "Llegada de mercadería",
            "2026-09-01 09:30",
            "Carla Núñez"
        ),
        Movimiento(2, "K-001", TipoMovimiento.SALIDA_VENTA, 5, "Venta en tienda", "2026-09-03 17:10", "Bruno Díaz"),
        Movimiento(3, "K-002", TipoMovimiento.ENTRADA, 12, "Llegada de mercadería", "2026-09-01 09:45", "Carla Núñez"),
        Movimiento(4, "K-002", TipoMovimiento.SALIDA_VENTA, 8, "Venta web", "2026-09-05 12:20", "Bruno Díaz"),
        Movimiento(5, "K-004", TipoMovimiento.ENTRADA, 6, "Llegada de mercadería", "2026-09-02 10:00", "Carla Núñez"),
        Movimiento(6, "K-004", TipoMovimiento.SALIDA_VENTA, 4, "Venta en tienda", "2026-09-06 16:40", "Bruno Díaz"),
        Movimiento(7, "K-006", TipoMovimiento.SALIDA_VENTA, 3, "Venta web", "2026-09-04 11:15", "Bruno Díaz"),
        Movimiento(8, "K-006", TipoMovimiento.AJUSTE, 1, "Conteo físico: diferencia de una unidad", "2026-09-07 18:00", "Carla Núñez"),
        Movimiento(9, "K-009", TipoMovimiento.ENTRADA, 10, "Llegada de mercadería", "2026-09-02 10:20", "Carla Núñez"),
        Movimiento(10, "K-009", TipoMovimiento.SALIDA_VENTA, 4, "Venta en tienda", "2026-09-08 15:05", "Bruno Díaz"),
        Movimiento(11, "K-011", TipoMovimiento.SALIDA_VENTA, 2, "Venta web", "2026-09-08 19:30", "Bruno Díaz"),
        Movimiento(12, "K-011", TipoMovimiento.SALIDA_CONSUMO, 1, "Polera usada como exhibición", "2026-09-09 09:10", "Ana Torres")
    )
}
