package com.example.kaijugrupomen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.kaijugrupomen.R
import com.example.kaijugrupomen.ui.theme.KaijuBlanco
import com.example.kaijugrupomen.ui.theme.KaijuMorado
import com.example.kaijugrupomen.ui.theme.KaijuNegro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncabezadoKaiju(
    titulo: String,
    onVolver: (() -> Unit)? = null,
    acciones: @Composable RowScope.() -> Unit = {}
) {
    Column {
        CenterAlignedTopAppBar(
            title = {
                Image(
                    painter = painterResource(id = R.drawable.logo_kaiju),
                    contentDescription = "Logo Kaiju Serigrafía",
                    modifier = Modifier.size(52.dp)
                )
            },
            navigationIcon = {
                if (onVolver != null) {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            },
            actions = acciones,
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = KaijuNegro,
                navigationIconContentColor = KaijuBlanco,
                actionIconContentColor = KaijuBlanco
            )
        )
        Text(
            text = titulo.uppercase(),
            color = KaijuBlanco,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(KaijuMorado)
                .padding(vertical = 8.dp)
        )
    }
}
