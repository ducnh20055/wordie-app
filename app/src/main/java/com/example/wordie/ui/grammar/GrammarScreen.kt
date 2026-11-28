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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordie.R
import com.example.wordie.models.GrammarChapter
import com.example.wordie.ui.components.BottomNavBar
import com.example.wordie.ui.components.MenuDropdown

@Composable
fun GrammarScreen(
    streakDays: Int = 0,
    currentLevel: Int = 1,
    chapters: List<GrammarChapter> = emptyList(),
    selectedChapterIndex: Int = 0,
    isDarkMode: Boolean = false,
    selectedTab: String = "grammar",
    onChapterSelect: (Int) -> Unit = {},
    onTabClick: (String) -> Unit = {},
    onStartLesson: (GrammarChapter) -> Unit = {},
    onThemeToggle: (Boolean) -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))

    val orangeMain = Color(0xFFFF8A00)
    val orangeDark = Color(0xFFE56E00)
    val orangeLightBg = Color(0xFFFFF1E0)

    val bgColor = if (isDarkMode) Color(0xFF1A1A1A) else Color.White
    val cardBg = if (isDarkMode) Color(0xFF2D2D2D) else orangeLightBg
    val textColor = if (isDarkMode) Color.White else Color(0xFF333333)
    val subTextColor = if (isDarkMode) Color(0xFFBDBDBD) else Color(0xFF757575)

    var showMenu by remember { mutableStateOf(false) }

    val safeChapters = if (chapters.isNotEmpty()) chapters else com.example.wordie.models.getSampleGrammarChapters()
    val activeIndex = selectedChapterIndex.coerceIn(0, safeChapters.size - 1)
    val currentChapter = safeChapters[activeIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // TOP HEADER BAR - REPLACED -80% WITH MENUDROPDOWN
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // PERCENT BADGE
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE8F5E9))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "${currentChapter.progressPercent}%",
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
                    .background(cardBg, RoundedCornerShape(20.dp))
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
                        color = subTextColor
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "10 phút",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = orangeDark
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // PLANET ICON
            Text(text = "🪐", fontSize = 28.sp)

            Spacer(modifier = Modifier.width(12.dp))

            // 3-LINE MENU DROPDOWN (REPLACED -80%)
            Box {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .shadow(2.dp, CircleShape)
                        .background(cardBg, CircleShape)
                        .clickable { showMenu = true }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier.width(18.dp).height(2.5.dp).background(orangeDark, RoundedCornerShape(50.dp)))
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(modifier = Modifier.width(18.dp).height(2.5.dp).background(orangeDark, RoundedCornerShape(50.dp)))
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(modifier = Modifier.width(18.dp).height(2.5.dp).background(orangeDark, RoundedCornerShape(50.dp)))
                    }
                }

                MenuDropdown(
                    expanded = showMenu,
                    isDarkMode = isDarkMode,
                    onDismiss = { showMenu = false },
                    onThemeToggle = { onThemeToggle(it) },
                    onLanguageClick = { showMenu = false; onLanguageClick() },
                    onHelpClick = { showMenu = false },
                    onLogoutClick = { showMenu = false; onLogoutClick() }
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
            Spacer(modifier = Modifier.height(12.dp))

            // CIRCULAR STREAK GAUGE
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(160.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = Color(0xFFE0E0E0),
                        startAngle = 135f,
                        sweepAngle = 270f,
                        useCenter = false,
                        style = Stroke(width = 14f)
                    )
                    if (streakDays > 0) {
                        drawArc(
                            color = orangeMain,
                            startAngle = 135f,
                            sweepAngle = (270f * (streakDays / 7f).coerceAtMost(1f)),
                            useCenter = false,
                            style = Stroke(width = 14f)
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "💧 $streakDays", fontSize = 32.sp)
                    Text(
                        text = "CHUỖI NGÀY",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        color = subTextColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(orangeMain)
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Cấp độ $currentLevel",
                            fontFamily = playfulFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // CHAPTER SWITCHER NAVIGATION (PREV / NEXT)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(38.dp)
                        .shadow(2.dp, CircleShape)
                        .background(if (activeIndex > 0) cardBg else Color.Transparent, CircleShape)
                        .clickable(enabled = activeIndex > 0) {
                            onChapterSelect(activeIndex - 1)
                        }
                ) {
                    Text(text = "◀", fontSize = 16.sp, color = if (activeIndex > 0) orangeDark else Color.Transparent)
                }

                Text(
                    text = "Bài Học ${currentChapter.id} / ${safeChapters.size}",
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = subTextColor
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(38.dp)
                        .shadow(2.dp, CircleShape)
                        .background(if (activeIndex < safeChapters.size - 1) cardBg else Color.Transparent, CircleShape)
                        .clickable(enabled = activeIndex < safeChapters.size - 1) {
                            onChapterSelect(activeIndex + 1)
                        }
                ) {
                    Text(text = "▶", fontSize = 16.sp, color = if (activeIndex < safeChapters.size - 1) orangeDark else Color.Transparent)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // CIRCULAR LESSON CARD
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .shadow(12.dp, CircleShape)
                    .background(cardBg, CircleShape)
                    .border(3.dp, orangeMain.copy(alpha = 0.4f), CircleShape)
                    .padding(28.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // BADGE
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(orangeMain)
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (currentChapter.starsEarned > 0) "Đã học" else "Bài học mới",
                            fontFamily = playfulFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Bài Học ${currentChapter.id}",
                        fontFamily = playfulFont,
                        fontSize = 12.sp,
                        color = subTextColor
                    )

                    Text(
                        text = currentChapter.title,
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp,
                        color = textColor,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // STARS
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(3) { starIndex ->
                            val isFilled = starIndex < currentChapter.starsEarned
                            Text(
                                text = if (isFilled) "⭐" else "☆",
                                fontSize = if (starIndex == 1) 32.sp else 28.sp,
                                color = if (isFilled) Color.Unspecified else Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // BUTTON BẮT ĐẦU HỌC / TIẾP TỤC HỌC / HỌC LẠI
                    val buttonText = when {
                        currentChapter.starsEarned > 0 && currentChapter.starsEarned < currentChapter.totalStars -> "Tiếp tục học"
                        currentChapter.starsEarned >= currentChapter.totalStars -> "Học lại"
                        else -> "Bắt đầu học"
                    }

                    Button(
                        onClick = { onStartLesson(currentChapter) },
                        colors = ButtonDefaults.buttonColors(containerColor = orangeMain, contentColor = Color.White),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.78f)
                            .height(46.dp)
                    ) {
                        Text(
                            text = buttonText,
                            fontFamily = playfulFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
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
