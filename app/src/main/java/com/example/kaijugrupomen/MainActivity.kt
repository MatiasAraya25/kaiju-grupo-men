package com.example.kaijugrupomen

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import com.example.kaijugrupomen.navigation.AppNavHost
import com.example.kaijugrupomen.ui.components.Notificaciones
import com.example.kaijugrupomen.ui.theme.KaijugrupomenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        Notificaciones.crearCanal(this)
        setContent {
            setSingletonImageLoaderFactory { contexto ->
                ImageLoader.Builder(contexto)
                    .components {
                        if (Build.VERSION.SDK_INT >= 28) add(AnimatedImageDecoder.Factory())
                        else add(GifDecoder.Factory())
                    }
                    .build()
            }
            KaijugrupomenTheme {
                AppNavHost()
            }
        }
    }
}