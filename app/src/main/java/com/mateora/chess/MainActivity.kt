package com.mateora.chess

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mateora.chess.ui.theme.*
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MateoraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MateoraApp()
                }
            }
        }
    }
}

@Composable
fun MateoraApp() {
    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2500)
        showSplash = false
    }

    if (showSplash) {
        SplashScreen()
    } else {
        MainScreen()
    }
}

@Composable
fun SplashScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlack),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "MATEORA",
            color = TextWhite,
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 8.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.brand_tagline),
            color = SubtleRed,
            fontSize = 14.sp,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun MainScreen() {
    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = SurfaceDark) {
                NavigationBarItem(
                    icon = { Text("♙", color = TextWhite, fontSize = 24.sp) },
                    label = { Text("Home", color = TextWhite) },
                    selected = true,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Text("♖", color = TextWhite, fontSize = 24.sp) },
                    label = { Text("Play", color = TextWhite) },
                    selected = false,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Text("♘", color = TextWhite, fontSize = 24.sp) },
                    label = { Text("Repertoire", color = TextWhite) },
                    selected = false,
                    onClick = { }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DeepBlack),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(16.dp)
                    .background(BoardLight)
            ) {
                Column(Modifier.fillMaxSize()) {
                    for (row in 0 until 8) {
                        Row(Modifier.weight(1f)) {
                            for (col in 0 until 8) {
                                val isDark = (row + col) % 2 != 0
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .background(if (isDark) BoardDark else Color.Transparent)
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = DarkCrimson),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(56.dp)
            ) {
                Text("Start Analysis", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
