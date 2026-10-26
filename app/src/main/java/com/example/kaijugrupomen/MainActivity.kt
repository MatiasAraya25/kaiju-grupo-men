package com.example.kaijugrupomen

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
            KaijugrupomenTheme {
                AppNavHost()
            }
        }
    }
}