package com.example.wordie

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.wordie.models.WordItem
import com.example.wordie.ui.theme.WordieTheme

@Composable
fun FlashCardScreen(
    lesson: Lesson,
    isDarkMode: Boolean,
    isPremium: Boolean,
    onBackClick: () -> Unit,
    onSpeakClick: (word: String) -> Unit,
    onPremiumClick: () -> Unit,
    onFinishClick: () -> Unit
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

    var showExitDialog by remember { mutableStateOf(false) }

    // Xử lý phím Back hệ thống
    BackHandler(enabled = true) {
        showExitDialog = true
    }

    if (showExitDialog) {
        ExitLessonDialog(
            isDarkMode = isDarkMode,
            onDismiss = { showExitDialog = false },
            onConfirmExit = {
                showExitDialog = false
                onBackClick()
            }
        )
    }

    // Nếu Firestore chưa có words thì dùng data mẫu
    val words = if (lesson.words.isNotEmpty()) lesson.words else listOf(
        WordItem(emoji = "🐶", english = "DOG", vietnamese = "Con chó", example = "The dog is happy."),
        WordItem(emoji = "🐱", english = "CAT", vietnamese = "Con mèo", example = "The cat is cute."),
        WordItem(emoji = "🍎", english = "APPLE", vietnamese = "Quả táo", example = "I eat an apple.")
    )

    var currentIndex by remember { mutableIntStateOf(0) }
    val currentWord = words[currentIndex]

    Column(modifier = Modifier.fillMaxSize().background(bgColor).padding(horizontal = 24.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(48.dp).shadow(3.dp, CircleShape).background(cardBg, CircleShape).clickable { showExitDialog = true }) {
                Text(text = "<", fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = orangeDark)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = lesson.title.uppercase(), fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 22.sp, letterSpacing = 1.sp, color = orangeMain)
            Spacer(modifier = Modifier.weight(1f))
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(48.dp).shadow(3.dp, CircleShape).background(if (isPremium) cardBg else Color(0xFFE0E0E0), CircleShape).clickable { if (!isPremium) onPremiumClick() }) {
                Text(text = if (isPremium) "📥" else "🔒", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "${currentIndex + 1}/${words.size}", fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = orangeDark)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            words.forEachIndexed { index, _ ->
                Box(modifier = Modifier.weight(1f).height(8.dp).background(if (index <= currentIndex) orangeMain else cardBg, RoundedCornerShape(50.dp)))
                if (index != words.lastIndex) Spacer(modifier = Modifier.width(6.dp))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth().weight(1f).shadow(6.dp, RoundedCornerShape(28.dp)).background(cardBg, RoundedCornerShape(28.dp)).padding(24.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = currentWord.emoji, fontSize = 110.sp, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = currentWord.english, fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 44.sp, letterSpacing = 2.sp, color = orangeMain, textAlign = TextAlign.Center, style = TextStyle(shadow = Shadow(color = orangeShadow, offset = Offset(0f, 4f), blurRadius = 0f)))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = currentWord.vietnamese, fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = textColor, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "\"${currentWord.example}\"", fontFamily = playfulFont, fontSize = 15.sp, color = subTextColor, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(24.dp))
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(72.dp).shadow(6.dp, CircleShape).background(if (isPremium) orangeMain else Color(0xFFBDBDBD), CircleShape).clickable { if (isPremium) onSpeakClick(currentWord.english) else onPremiumClick() }) {
                    Text(text = if (isPremium) "🔊" else "🔒", fontSize = 32.sp)
                }
                Text(text = if (isPremium) "TAP TO LISTEN" else "PHÁT ÂM AI - CẦN PRO 👑", fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 11.sp, letterSpacing = 1.sp, color = if (isPremium) orangeDark else orangeMain, modifier = Modifier.padding(top = 8.dp))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { if (currentIndex > 0) currentIndex-- else showExitDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = cardBg, contentColor = orangeDark), shape = RoundedCornerShape(50.dp), modifier = Modifier.weight(1f).height(58.dp)) {
                Text(text = "< BACK", fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Button(onClick = { if (currentIndex < words.size - 1) currentIndex++ else onFinishClick() }, colors = ButtonDefaults.buttonColors(containerColor = orangeMain, contentColor = Color.White), shape = RoundedCornerShape(50.dp), modifier = Modifier.weight(1f).height(58.dp).shadow(5.dp, RoundedCornerShape(50.dp))) {
                Text(text = if (currentIndex == words.size - 1) "FINISH ✓" else "NEXT >", fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FlashCardScreenPreview() {
    WordieTheme {
        FlashCardScreen(lesson = Lesson(title = "ANIMALS", icon = "🐶"), isDarkMode = false, isPremium = false, onBackClick = {}, onSpeakClick = {}, onPremiumClick = {}, onFinishClick = {})
    }
}
