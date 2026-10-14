package model
data class Producto(
    val codigo: String,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val tipo: String,
    val precio: Int,
    val stockActual: Int,
    val stockMinimo: Int,
    val detalle: String = ""
)


object Categorias {
    val lista = listOf("Anime", "Manga", "Videojuegos")
}
