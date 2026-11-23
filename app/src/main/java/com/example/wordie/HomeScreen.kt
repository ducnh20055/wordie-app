package com.example.wordie

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordie.models.Lesson
import com.example.wordie.ui.theme.WordieTheme

@Composable
fun HomeScreen(
    playerLevel: String,
    isDarkMode: Boolean,
    isPremium: Boolean,
    lessons: List<Lesson>,
    lessonsDone: Int = 0,
    onLessonClick: (lesson: Lesson) -> Unit,
    onPremiumClick: () -> Unit,
    onTabClick: (tab: String) -> Unit,
    onThemeToggle: (Boolean) -> Unit,
    onLogoutClick: () -> Unit
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))
    val orangeMain = Color(0xFFFF8A00)
    val orangeDark = Color(0xFFE56E00)
    val orangeLightBg = Color(0xFFFFF1E0)
    val orangeShadow = Color(0xFFC85A00)
    val bgColor = if (isDarkMode) Color(0xFF1A1A1A) else Color.White
    val cardBg = if (isDarkMode) Color(0xFF2D2D2D) else orangeLightBg
    val textColor = if (isDarkMode) Color.White else Color(0xFF333333)
    val subTextColor = if (isDarkMode) Color(0xFFBDBDBD) else Color(0xFF757575)
    var selectedTab by remember { mutableStateOf("home") }
    var showMenu by remember { mutableStateOf(false) }

    val totalLessons = lessons.size
    val safeLessonsDone = if (totalLessons > 0) lessonsDone.coerceAtMost(totalLessons) else lessonsDone
    val progressFraction = if (totalLessons > 0) (safeLessonsDone.toFloat() / totalLessons.toFloat()).coerceIn(0f, 1f) else 0f
    val percentage = (progressFraction * 100).toInt()

    Column(modifier = Modifier.fillMaxSize().background(bgColor)) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
            Text(text = "LESSONS", fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 32.sp, letterSpacing = 1.sp, color = orangeMain, style = TextStyle(shadow = Shadow(color = orangeShadow, offset = Offset(0f, 4f), blurRadius = 0f)))
            Spacer(modifier = Modifier.weight(1f))
            Box {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(52.dp).shadow(4.dp, CircleShape).background(cardBg, CircleShape).clickable { showMenu = true }) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier.width(22.dp).height(3.dp).background(orangeDark, RoundedCornerShape(50.dp)))
                        Spacer(modifier = Modifier.height(5.dp))
                        Box(modifier = Modifier.width(22.dp).height(3.dp).background(orangeDark, RoundedCornerShape(50.dp)))
                        Spacer(modifier = Modifier.height(5.dp))
                        Box(modifier = Modifier.width(22.dp).height(3.dp).background(orangeDark, RoundedCornerShape(50.dp)))
                    }
                }
                MenuDropdown(expanded = showMenu, isDarkMode = isDarkMode, onDismiss = { showMenu = false }, onThemeToggle = { onThemeToggle(it) }, onLanguageClick = { showMenu = false }, onHelpClick = { showMenu = false }, onLogoutClick = { showMenu = false; onLogoutClick() })
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
            Text(text = "LEVEL: ${playerLevel.uppercase()}", fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 1.sp, color = orangeDark)
            Spacer(modifier = Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f).height(14.dp).background(cardBg, RoundedCornerShape(50.dp))) {
                    if (progressFraction > 0f) {
                        Box(modifier = Modifier.fillMaxWidth(progressFraction).height(14.dp).background(orangeMain, RoundedCornerShape(50.dp)))
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "$percentage%", fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = orangeDark)
            }
            Text(text = "$safeLessonsDone of $totalLessons lessons done", fontFamily = playfulFont, fontSize = 13.sp, color = subTextColor, modifier = Modifier.padding(top = 6.dp))
            if (!isPremium) {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 12.dp).background(Color(0xFFFFC107), RoundedCornerShape(20.dp)).clickable { onPremiumClick() }.padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                    Text(text = "👑 NÂNG CẤP LÊN PRO - MỞ KHÓA TẤT CẢ", fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                }
            } else {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 12.dp).background(Color(0xFF4CAF50), RoundedCornerShape(20.dp)).padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                    Text(text = "👑 BẠN ĐANG LÀ PRO - ĐÃ MỞ KHÓA TẤT CẢ", fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
        Column(modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 24.dp)) {
            lessons.forEach { lesson ->
                val isLocked = !isPremium && lesson.isPremium
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).shadow(2.dp, RoundedCornerShape(20.dp)).background(cardBg, RoundedCornerShape(20.dp)).clickable { if (isLocked) onPremiumClick() else onLessonClick(lesson) }.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    Text(text = lesson.icon, fontSize = 32.sp)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = lesson.title, fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 18.sp, letterSpacing = 0.5.sp, color = textColor)
                            if (isLocked) { Spacer(modifier = Modifier.width(6.dp)); Text(text = "👑", fontSize = 14.sp) }
                        }
                        Text(text = if (isLocked) "Premium - Bấm để mở khóa" else lesson.info, fontFamily = playfulFont, fontSize = 13.sp, color = if (isLocked) orangeMain else subTextColor)
                    }
                    Text(text = if (isLocked) "🔒" else "PLAY >", fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = orangeMain, textAlign = TextAlign.Center)
                }
            }
            if (!isPremium) {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 12.dp).background(Color(0xFFE0E0E0), RoundedCornerShape(16.dp)).clickable { onPremiumClick() }.padding(vertical = 14.dp), contentAlignment = Alignment.Center) {
                    Text(text = "📢 Quảng cáo - Bấm để xóa với PRO 👑", fontFamily = playfulFont, fontSize = 13.sp, color = Color(0xFF757575))
                }
            }
        }
        BottomNavBar(selectedTab = selectedTab, isDarkMode = isDarkMode, onTabClick = { tab -> selectedTab = tab; onTabClick(tab) })
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    WordieTheme {
        HomeScreen(playerLevel = "Beginner", isDarkMode = false, isPremium = false, lessons = listOf(Lesson(title = "ANIMALS", icon = "🐶", info = "20 Words • Easy")), lessonsDone = 1, onLessonClick = {}, onPremiumClick = {}, onTabClick = {}, onThemeToggle = {}, onLogoutClick = {})
    }
}
