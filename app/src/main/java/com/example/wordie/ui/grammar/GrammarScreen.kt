package com.example.wordie.ui.grammar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordie.R

import com.example.wordie.models.GrammarChapter
import com.example.wordie.models.getSampleGrammarChapters
import com.example.wordie.ui.components.BottomNavBar

@Composable
fun GrammarScreen(
    streakDays: Int = 1,
    currentLevel: Int = 1,
    isDarkMode: Boolean = false,
    selectedTab: String = "grammar",
    onTabClick: (String) -> Unit = {},
    onStartLesson: (GrammarChapter) -> Unit = {}
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))
    val darkBg = Color(0xFF16171B)
    val cardDarkBg = Color(0xFF22242B)
    val purplePrimary = Color(0xFF8C5CFF)
    val orangeMain = Color(0xFFFF8A00)

    val chapters = remember { getSampleGrammarChapters() }
    val currentChapter = chapters.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // TOP BAR
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // 1% BADGE
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF2C3E35))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "1%",
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF4CAF50)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // DAILY GOAL PROGRESS
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF2A2C34))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Mục tiêu ngày",
                        fontFamily = playfulFont,
                        fontSize = 11.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "10 phút",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // PLANET ICON
            Text(text = "🪐", fontSize = 28.sp)

            Spacer(modifier = Modifier.width(10.dp))

            // PROMO BADGE -80%
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .shadow(4.dp, RoundedCornerShape(20.dp))
                    .background(Color(0xFFFF5252), RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "-80%",
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // CIRCULAR STREAK GAUGE
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(170.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = Color(0xFF3A3D4A),
                        startAngle = 135f,
                        sweepAngle = 270f,
                        useCenter = false,
                        style = Stroke(width = 14f)
                    )
                    drawArc(
                        color = Color(0xFFFFB74D),
                        startAngle = 135f,
                        sweepAngle = 90f,
                        useCenter = false,
                        style = Stroke(width = 14f)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "💧 $streakDays", fontSize = 32.sp)
                    Text(
                        text = "CHUỖI NGÀY",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color.White)
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Cấp độ $currentLevel",
                            fontFamily = playfulFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // CIRCULAR LESSON CARD (MATCHING IMAGE 3)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .shadow(12.dp, CircleShape)
                    .background(cardDarkBg, CircleShape)
                    .border(3.dp, Color(0xFF3F4352), CircleShape)
                    .padding(32.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // BADGE - HOÀN THÀNH BÀI HỌC
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(purplePrimary)
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Hoàn thành bài học",
                            fontFamily = playfulFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Bài Học 1",
                        fontFamily = playfulFont,
                        fontSize = 13.sp,
                        color = Color.LightGray
                    )

                    Text(
                        text = currentChapter.title,
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 26.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 3 STARS
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "⭐", fontSize = 28.sp)
                        Text(text = "☆", fontSize = 32.sp, color = Color.Gray)
                        Text(text = "☆", fontSize = 28.sp, color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // BUTTON TIẾP TỤC
                    Button(
                        onClick = { onStartLesson(currentChapter) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Tiếp tục",
                            fontFamily = playfulFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // NAV BAR DƯỚI
        BottomNavBar(
            selectedTab = selectedTab,
            isDarkMode = true,
            onTabClick = onTabClick
        )
    }
}
