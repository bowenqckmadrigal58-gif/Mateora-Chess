package com.mateora.chess

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mateora.chess.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MateoraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MateoraChessGame()
                }
            }
        }
    }
}

@Composable
fun MateoraChessGame() {
    // وضعیت تخته شطرنج به صورت لیست قابل تغییر (Mutable) برای اعمال انیمیشن و تغییرات
    val boardState = remember {
        mutableStateListOf(
            mutableStateListOf("r", "n", "b", "q", "k", "b", "n", "r"),
            mutableStateListOf("p", "p", "p", "p", "p", "p", "p", "p"),
            mutableStateListOf("", "", "", "", "", "", "", ""),
            mutableStateListOf("", "", "", "", "", "", "", ""),
            mutableStateListOf("", "", "", "", "", "", "", ""),
            mutableStateListOf("", "", "", "", "", "", "", ""),
            mutableStateListOf("P", "P", "P", "P", "P", "P", "P", "P"),
            mutableStateListOf("R", "N", "B", "Q", "K", "B", "N", "R")
        )
    }

    // نگهداری خانه انتخاب شده برای انیمیشن کلیک و حرکت
    var selectedSquare by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var gameMessage by remember { mutableStateOf("Mateora Tactical Engine Ready") }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = SurfaceDark) {
                NavigationBarItem(
                    icon = { Text("♙", color = TextWhite, fontSize = 22.sp) },
                    label = { Text("Board", color = TextWhite) },
                    selected = true,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Text("⚡", color = TextWhite, fontSize = 22.sp) },
                    label = { Text("Analysis", color = TextWhite) },
                    selected = false,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Text("⚙", color = TextWhite, fontSize = 22.sp) },
                    label = { Text("Settings", color = TextWhite) },
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
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // هدر برنامه
            Column(
                modifier = Modifier.padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "MATEORA",
                    color = TextWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp
                )
                Text(
                    text = gameMessage,
                    color = SubtleRed,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp
                )
            }

            // تخته شطرنج با قابلیت انیمیشن کلیک و انتخاب خانه
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, DarkCrimson, RoundedCornerShape(8.dp))
                    .background(BoardLight)
            ) {
                Column(Modifier.fillMaxSize()) {
                    for (row in 0 until 8) {
                        Row(Modifier.weight(1f)) {
                            for (col in 0 until 8) {
                                val isDark = (row + col) % 2 != 0
                                val piece = boardState[row][col]
                                val symbol = getChessSymbol(piece)
                                val isSelected = selectedSquare == Pair(row, col)

                                // رنگ پس‌زمینه خانه (اگر انتخاب شده باشد رنگ متفاوتی می‌گیرد)
                                val squareColor = when {
                                    isSelected -> DarkCrimson.copy(alpha = 0.7f)
                                    isDark -> BoardDark
                                    else -> BoardLight
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .background(squareColor)
                                        .clickable {
                                            if (selectedSquare == null) {
                                                if (piece.isNotEmpty()) {
                                                    selectedSquare = Pair(row, col)
                                                    gameMessage = "Selected: Row $row, Col $col"
                                                }
                                            } else {
                                                // جابجایی مهره با انیمیشن ساده
                                                val (fromRow, fromCol) = selectedSquare!!
                                                boardState[row][col] = boardState[fromRow][fromCol]
                                                boardState[fromRow][fromCol] = ""
                                                selectedSquare = null
                                                gameMessage = "Move completed!"
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    // انیمیشن نرم برای ظاهر شدن مهره‌ها
                                    AnimatedVisibility(
                                        visible = symbol.isNotEmpty(),
                                        enter = fadeIn(animationSpec = tween(300)),
                                        exit = fadeOut(animationSpec = tween(300))
                                    ) {
                                        Text(
                                            text = symbol,
                                            fontSize = 32.sp,
                                            color = if (piece[0].isUpperCase()) Color(0xFFFFFFFF) else Color(0xFF111111)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // دکمه ریست بازی
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        // ریست کردن تخته
                        val initial = listOf(
                            listOf("r", "n", "b", "q", "k", "b", "n", "r"),
                            listOf("p", "p", "p", "p", "p", "p", "p", "p"),
                            listOf("", "", "", "", "", "", "", ""),
                            listOf("", "", "", "", "", "", "", ""),
                            listOf("", "", "", "", "", "", "", ""),
                            listOf("", "", "", "", "", "", "", ""),
                            listOf("P", "P", "P", "P", "P", "P", "P", "P"),
                            listOf("R", "N", "B", "Q", "K", "B", "N", "R")
                        )
                        for (r in 0..7) {
                            for (c in 0..7) {
                                boardState[r][c] = initial[r][c]
                            }
                        }
                        selectedSquare = null
                        gameMessage = "New Game Started"
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkCrimson),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(50.dp)
                ) {
                    Text("Reset Board", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                }
            }
        }
    }
}

fun getChessSymbol(piece: String): String {
    return when (piece) {
        "K" -> "♔"; "Q" -> "♕"; "R" -> "♖"; "B" -> "♗"; "N" -> "♘"; "P" -> "♙"
        "k" -> "♚"; "q" -> "♛"; "r" -> "♜"; "b" -> "♝"; "n" -> "♞"; "p" -> "♟"
        else -> ""
    }
}
