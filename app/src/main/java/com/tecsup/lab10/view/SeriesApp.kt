package com.tecsup.lab10.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.lab10.data.SerieApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val PrimaryColor = Color(0xFF1A1A2E)
val AccentColor = Color(0xFFE94560)
val SurfaceColor = Color(0xFF16213E)
val CardColor = Color(0xFF0F3460)
val TextPrimary = Color(0xFFEEEEEE)
val TextSecondary = Color(0xFFAAAAAA)

@Composable
fun SeriesApp() {
    val urlBase = "http://10.0.2.2:8000/"
    val retrofit = Retrofit.Builder()
        .baseUrl(urlBase)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val servicio = retrofit.create(SerieApiService::class.java)
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.background(PrimaryColor),
        topBar = { BarraSuperior() },
        bottomBar = { BarraInferior(navController) },
        floatingActionButton = { BotonFAB(navController, servicio) },
        containerColor = PrimaryColor,
        content = { paddingValues -> Contenido(paddingValues, navController, servicio) }
    )
}

@Composable
fun BotonFAB(navController: NavHostController, servicio: SerieApiService) {
    val cbeState by navController.currentBackStackEntryAsState()
    val rutaActual = cbeState?.destination?.route
    if (rutaActual == "series") {
        FloatingActionButton(
            containerColor = AccentColor,
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            onClick = { navController.navigate("serieNuevo") }
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(PrimaryColor, CardColor)
                )
            )
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "🎬 SERIES APP",
                    color = TextPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    letterSpacing = 2.sp
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Transparent
            )
        )
    }
}

@Composable
fun BarraInferior(navController: NavHostController) {
    NavigationBar(
        containerColor = SurfaceColor,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Home, contentDescription = "Inicio", tint = TextPrimary) },
            label = { Text("Inicio", color = TextSecondary, fontSize = 12.sp) },
            selected = navController.currentDestination?.route == "inicio",
            onClick = { navController.navigate("inicio") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = AccentColor
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Favorite, contentDescription = "Series", tint = TextPrimary) },
            label = { Text("Series", color = TextSecondary, fontSize = 12.sp) },
            selected = navController.currentDestination?.route == "series",
            onClick = { navController.navigate("series") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = AccentColor
            )
        )
    }
}

@Composable
fun Contenido(pv: PaddingValues, navController: NavHostController, servicio: SerieApiService) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryColor)
            .padding(pv)
    ) {
        NavHost(navController = navController, startDestination = "inicio") {
            composable("inicio") { ScreenInicio() }
            composable("series") { ContenidoSeriesListado(navController, servicio) }
            composable("serieNuevo") { ContenidoSerieEditar(navController, servicio, 0) }
            composable("serieVer/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) {
                ContenidoSerieEditar(navController, servicio, it.arguments!!.getInt("id"))
            }
            composable("serieDel/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) {
                ContenidoSerieEliminar(navController, servicio, it.arguments!!.getInt("id"))
            }
        }
    }
}