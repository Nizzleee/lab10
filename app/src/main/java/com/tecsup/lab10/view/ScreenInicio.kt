package com.tecsup.lab10.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScreenInicio() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryColor),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "🎬", fontSize = 64.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Series App",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccentColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tu colección favorita",
                fontSize = 16.sp,
                color = TextSecondary
            )
        }
    }
}