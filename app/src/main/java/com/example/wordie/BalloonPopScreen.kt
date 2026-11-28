package com.example.wordie

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordie.models.Lesson
import com.example.wordie.ui.balloonpop.BalloonPopViewModel

@Composable
fun BalloonPopScreen(
    lesson: Lesson,
    isDarkMode: Boolean,
    onBackClick: () -> Unit,
    onSpeakClick: (word: String) -> Unit,
    onFinishClick: () -> Unit,
    viewModel: BalloonPopViewModel = remember { BalloonPopViewModel() }
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))
    val orangeMain = Color(0xFFFF8A00)
    val orangeDark = Color(0xFFE56E00)
    val orangeLightBg = Color(0xFFFFF1E0)
    val orangeShadow = Color(0xFFC85A00)

    val bgColor = if (isDarkMode) Color(0xFF1A1A1A) else Color(0xFFF9F9FB)
    val cardBg = if (isDarkMode) Color(0xFF2D2D2D) else orangeLightBg

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(lesson) {
        viewModel.initGame(lesson)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // TOP BAR - BACK + TIMER + SCORE
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .shadow(2.dp, CircleShape)
                        .background(cardBg, CircleShape)
                        .clickable { onBackClick() }
                ) {
                    Text(
                        text = "←",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = orangeDark
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // TIMER BADGE
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .shadow(2.dp, RoundedCornerShape(50.dp))
                        .background(if (uiState.timeLeft <= 5) Color(0xFFFF5252) else orangeLightBg, RoundedCornerShape(50.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "⏱️ ${uiState.timeLeft}s",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = if (uiState.timeLeft <= 5) Color.White else orangeDark
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // SCORE BADGE
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .shadow(2.dp, RoundedCornerShape(50.dp))
                        .background(orangeLightBg, RoundedCornerShape(50.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "⭐ ${uiState.score}",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = orangeMain
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // TARGET PROMPT CARD
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(24.dp))
                    .background(cardBg, RoundedCornerShape(24.dp))
                    .border(2.dp, orangeMain, RoundedCornerShape(24.dp))
                    .padding(vertical = 14.dp, horizontal = 20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "POP THE BALLOON FOR:",
                        fontFamily = playfulFont,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp,
                        color = orangeDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${uiState.targetWord.emoji} ${uiState.targetWord.vietnamese.uppercase()}",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = orangeMain,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            shadow = Shadow(
                                color = orangeShadow,
                                offset = Offset(0f, 2f),
                                blurRadius = 0f
                            )
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // FLOATING CANVAS AREA
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(28.dp))
                    .background(if (isDarkMode) Color(0xFF222222) else Color(0xFFEBF8FF))
                    .border(2.dp, Color(0xFFB3E5FC), RoundedCornerShape(28.dp))
            ) {
                val canvasWidth = maxWidth
                val canvasHeight = maxHeight

                // Clouds decoration
                Text(text = "☁️", fontSize = 36.sp, modifier = Modifier.offset(x = 20.dp, y = 30.dp))
                Text(text = "☁️", fontSize = 48.sp, modifier = Modifier.offset(x = canvasWidth - 80.dp, y = 80.dp))
                Text(text = "☁️", fontSize = 30.sp, modifier = Modifier.offset(x = canvasWidth / 2 - 30.dp, y = 180.dp))

                if (!uiState.isGameOver) {
                    val currentY = canvasHeight * uiState.floatProgress

                    uiState.balloons.forEach { balloon ->
                        if (!balloon.isPopped) {
                            val balloonX = (canvasWidth * balloon.xFraction) - 50.dp

                            Box(
                                contentAlignment = Alignment.TopCenter,
                                modifier = Modifier
                                    .offset(x = balloonX, y = currentY)
                                    .size(width = 100.dp, height = 135.dp)
                                    .clickable {
                                        viewModel.popBalloon(balloon, onSpeakClick)
                                    }
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(width = 90.dp, height = 100.dp)
                                            .shadow(6.dp, CircleShape)
                                            .background(balloon.color, CircleShape)
                                            .padding(8.dp)
                                    ) {
                                        Text(
                                            text = balloon.word.english,
                                            fontFamily = playfulFont,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = Color.White,
                                            textAlign = TextAlign.Center,
                                            style = TextStyle(
                                                shadow = Shadow(
                                                    color = Color.Black.copy(alpha = 0.3f),
                                                    offset = Offset(0f, 2f),
                                                    blurRadius = 0f
                                                )
                                            )
                                        )
                                    }

                                    Canvas(modifier = Modifier.size(width = 20.dp, height = 25.dp)) {
                                        val path = Path().apply {
                                            moveTo(size.width / 2, 0f)
                                            quadraticTo(
                                                size.width / 2 + 8f, size.height / 2,
                                                size.width / 2, size.height
                                            )
                                        }
                                        drawPath(
                                            path = path,
                                            color = Color(0xFF757575),
                                            style = Stroke(width = 3f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // GAME OVER MODAL DIALOG
        if (uiState.isGameOver) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.65f))
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(32.dp))
                        .background(if (isDarkMode) Color(0xFF222222) else Color.White, RoundedCornerShape(32.dp))
                        .padding(28.dp)
                ) {
                    Text(
                        text = "⏰ TIME'S UP! ⏰",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = orangeMain,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (uiState.score >= 50) "🏆 EXCELLENT JOB!" else "🎉 GOOD TRY!",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = orangeDark
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // SCORE CARD
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(orangeLightBg, RoundedCornerShape(20.dp))
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "⭐ SCORE", fontFamily = playfulFont, fontSize = 12.sp, color = orangeDark)
                            Text(text = "${uiState.score}", fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 28.sp, color = orangeMain)
                        }
                        Box(modifier = Modifier.width(2.dp).height(36.dp).background(orangeMain.copy(alpha = 0.3f)))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "🎈 POPPED", fontFamily = playfulFont, fontSize = 12.sp, color = orangeDark)
                            Text(text = "${uiState.poppedCount}", fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 28.sp, color = orangeMain)
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // REPLAY BUTTON
                    Button(
                        onClick = {
                            viewModel.resetGame()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = orangeMain),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        Text(text = "PLAY AGAIN 🔄", fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // FINISH BUTTON
                    Button(
                        onClick = onFinishClick,
                        colors = ButtonDefaults.buttonColors(containerColor = orangeLightBg),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        Text(text = "FINISH LESSON 🏆", fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = orangeDark)
                    }
                }
            }
        }
    }
}
