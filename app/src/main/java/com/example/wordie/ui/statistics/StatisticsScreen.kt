package com.example.wordie.ui.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordie.R

import com.example.wordie.ui.components.BottomNavBar

@Composable
fun StatisticsScreen(
    streakDays: Int = 1,
    totalWords: Int = 120,
    accuracyPercent: Int = 92,
    studyTimeMins: Int = 45,
    isDarkMode: Boolean = false,
    selectedTab: String = "statistics",
    onTabClick: (String) -> Unit = {}
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))
    val orangeMain = Color(0xFFFF8A00)
    val orangeDark = Color(0xFFE56E00)
    val orangeLightBg = Color(0xFFFFF1E0)
    val orangeShadow = Color(0xFFC85A00)

    val bgColor = if (isDarkMode) Color(0xFF1A1A1A) else Color.White
    val cardBg = if (isDarkMode) Color(0xFF2D2D2D) else orangeLightBg
    val textColor = if (isDarkMode) Color.White else Color(0xFF333333)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // HEADER
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "STATISTICS",
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                letterSpacing = 1.sp,
                color = orangeMain,
                style = TextStyle(
                    shadow = Shadow(
                        color = orangeShadow,
                        offset = Offset(0f, 4f),
                        blurRadius = 0f
                    )
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            // STATS GRID (2x2)
            Row(modifier = Modifier.fillMaxWidth()) {
                StatCard(
                    emoji = "🔥",
                    value = "$streakDays Days",
                    label = "STREAK",
                    playfulFont = playfulFont,
                    orangeMain = orangeMain,
                    cardBg = cardBg,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                StatCard(
                    emoji = "📚",
                    value = "$totalWords Words",
                    label = "LEARNED",
                    playfulFont = playfulFont,
                    orangeMain = orangeMain,
                    cardBg = cardBg,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                StatCard(
                    emoji = "🎯",
                    value = "$accuracyPercent%",
                    label = "ACCURACY",
                    playfulFont = playfulFont,
                    orangeMain = orangeMain,
                    cardBg = cardBg,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                StatCard(
                    emoji = "⏱️",
                    value = "$studyTimeMins Mins",
                    label = "STUDY TIME",
                    playfulFont = playfulFont,
                    orangeMain = orangeMain,
                    cardBg = cardBg,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // WEEKLY PROGRESS CHART CARD
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(24.dp))
                    .background(cardBg, RoundedCornerShape(24.dp))
                    .padding(20.dp)
            ) {
                Text(
                    text = "WEEKLY ACTIVITY",
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = orangeDark
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                ) {
                    val days = listOf("Mon" to 0.4f, "Tue" to 0.7f, "Wed" to 0.9f, "Thu" to 0.5f, "Fri" to 0.8f, "Sat" to 1.0f, "Sun" to 0.3f)
                    days.forEach { (day, fraction) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(36.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height((80 * fraction).dp)
                                    .background(orangeMain, RoundedCornerShape(8.dp))
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = day,
                                fontFamily = playfulFont,
                                fontSize = 11.sp,
                                color = textColor
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // NAV BAR DƯỚI
        BottomNavBar(
            selectedTab = selectedTab,
            isDarkMode = isDarkMode,
            onTabClick = onTabClick
        )
    }
}

@Composable
private fun StatCard(
    emoji: String,
    value: String,
    label: String,
    playfulFont: FontFamily,
    orangeMain: Color,
    cardBg: Color,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(20.dp))
            .background(cardBg, RoundedCornerShape(20.dp))
            .padding(vertical = 16.dp)
    ) {
        Text(text = emoji, fontSize = 28.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            fontFamily = playfulFont,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = orangeMain
        )
        Text(
            text = label,
            fontFamily = playfulFont,
            fontSize = 11.sp,
            color = Color.Gray
        )
    }
}
