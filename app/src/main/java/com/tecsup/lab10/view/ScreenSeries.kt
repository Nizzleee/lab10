package com.tecsup.lab10.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tecsup.lab10.data.SerieApiService
import com.tecsup.lab10.data.SerieModel
import kotlinx.coroutines.delay

@Composable
fun ContenidoSeriesListado(navController: NavHostController, servicio: SerieApiService) {
    var listaSeries: SnapshotStateList<SerieModel> = remember { mutableStateListOf() }
    LaunchedEffect(Unit) {
        val respuesta = servicio.selectSeries()
        respuesta.series.forEach { listaSeries.add(it) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryColor)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Mis Series",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccentColor,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }
        items(listaSeries) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(AccentColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${item.id}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "⭐ ${item.rating} • ${item.category}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                    IconButton(onClick = { navController.navigate("serieVer/${item.id}") }) {
                        Icon(Icons.Outlined.Edit, contentDescription = "Editar", tint = Color(0xFF4FC3F7))
                    }
                    IconButton(onClick = { navController.navigate("serieDel/${item.id}") }) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Eliminar", tint = AccentColor)
                    }
                }
            }
        }
    }
}

@Composable
fun ContenidoSerieEditar(navController: NavHostController, servicio: SerieApiService, pid: Int = 0) {
    var id by remember { mutableStateOf(pid) }
    var name by remember { mutableStateOf("") }
    var release_date by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var grabar by remember { mutableStateOf(false) }

    if (id != 0) {
        LaunchedEffect(Unit) {
            val objSerie = servicio.selectSerie(id.toString())
            delay(100)
            name = objSerie.body()?.name ?: ""
            release_date = objSerie.body()?.release_date ?: ""
            rating = objSerie.body()?.rating.toString()
            category = objSerie.body()?.category ?: ""
        }
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = AccentColor,
        unfocusedBorderColor = TextSecondary,
        focusedLabelColor = AccentColor,
        unfocusedLabelColor = TextSecondary,
        focusedTextColor = TextPrimary,
        unfocusedTextColor = TextPrimary,
        cursorColor = AccentColor
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryColor)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (id == 0) "Nueva Serie" else "Editar Serie",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AccentColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(value = id.toString(), onValueChange = {}, label = { Text("ID") }, readOnly = true, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
        OutlinedTextField(value = release_date, onValueChange = { release_date = it }, label = { Text("Fecha de estreno") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
        OutlinedTextField(value = rating, onValueChange = { rating = it }, label = { Text("Puntuación") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
        OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Categoría") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)

        Button(
            onClick = { grabar = true },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentColor)
        ) {
            Text("Guardar", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }

    if (grabar) {
        val objSerie = SerieModel(id, name, release_date, rating.toInt(), category)
        LaunchedEffect(Unit) {
            if (id == 0) servicio.insertSerie(objSerie)
            else servicio.updateSerie(id.toString(), objSerie)
        }
        grabar = false
        navController.navigate("series")
    }
}

@Composable
fun ContenidoSerieEliminar(navController: NavHostController, servicio: SerieApiService, id: Int) {
    var showDialog by remember { mutableStateOf(true) }
    var borrar by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            containerColor = SurfaceColor,
            shape = RoundedCornerShape(20.dp),
            title = { Text("Eliminar Serie", color = AccentColor, fontWeight = FontWeight.Bold) },
            text = { Text("¿Estás seguro de que deseas eliminar esta serie?", color = TextSecondary) },
            confirmButton = {
                Button(
                    onClick = { showDialog = false; borrar = true },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentColor),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Eliminar", color = Color.White) }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDialog = false; navController.navigate("series") },
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Cancelar", color = TextPrimary) }
            }
        )
    }

    if (borrar) {
        LaunchedEffect(Unit) {
            servicio.deleteSerie(id.toString())
            borrar = false
            navController.navigate("series")
        }
    }
}