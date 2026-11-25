package com.example.wordie

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
fun QuizScreen(
    lesson: Lesson,
    isDarkMode: Boolean,
    isPremium: Boolean,
    onBackClick: () -> Unit,
    onSpeakClick: (word: String) -> Unit,
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

    val words = if (lesson.words.isNotEmpty()) lesson.words else listOf(
        WordItem(emoji = "🐶", english = "DOG", vietnamese = "Con chó", example = "The dog is happy."),
        WordItem(emoji = "🐱", english = "CAT", vietnamese = "Con mèo", example = "The cat is cute."),
        WordItem(emoji = "🍎", english = "APPLE", vietnamese = "Quả táo", example = "I eat an apple.")
    )

    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var isAnswered by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }

    val currentWord = words[currentIndex]

    val options = remember(currentIndex) {
        val wrongOptions = words.filter { it.id != currentWord.id }
            .map { it.vietnamese }
            .shuffled()
            .take(3)
        (wrongOptions + currentWord.vietnamese).shuffled()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // TOP BAR
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .shadow(3.dp, CircleShape)
                    .background(cardBg, CircleShape)
                    .clickable { onBackClick() }
            ) {
                Text(
                    text = "<",
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = orangeDark
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "QUIZ TRẮC NGHIỆM",
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    letterSpacing = 0.5.sp,
                    color = orangeMain
                )
                Text(
                    text = lesson.title.uppercase(),
                    fontFamily = playfulFont,
                    fontSize = 12.sp,
                    color = orangeDark
                )
            }

            Text(
                text = "${currentIndex + 1}/${words.size}",
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = orangeDark
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // CARD HIỂN THỊ TỪ CẦN ĐOÁN
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .shadow(6.dp, RoundedCornerShape(28.dp))
                .background(cardBg, RoundedCornerShape(28.dp))
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = currentWord.emoji, fontSize = 60.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = currentWord.english,
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 36.sp,
                        color = orangeMain,
                        style = TextStyle(
                            shadow = Shadow(
                                color = orangeShadow,
                                offset = Offset(0f, 3f),
                                blurRadius = 0f
                            )
                        )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "🔊",
                        fontSize = 22.sp,
                        modifier = Modifier.clickable { onSpeakClick(currentWord.english) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Chọn nghĩa tiếng Việt chính xác:",
            fontFamily = playfulFont,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = orangeDark,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // 4 ĐÁP ÁN
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            options.forEach { option ->
                val isCorrect = option == currentWord.vietnamese
                val isSelected = option == selectedOption

                val btnColor = when {
                    !isAnswered -> cardBg
                    isCorrect -> Color(0xFF4CAF50)
                    isSelected -> Color(0xFFE53935)
                    else -> cardBg
                }

                val optionTextColor = when {
                    isAnswered && (isCorrect || isSelected) -> Color.White
                    else -> textColor
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(3.dp, RoundedCornerShape(20.dp))
                        .background(btnColor, RoundedCornerShape(20.dp))
                        .clickable(enabled = !isAnswered) {
                            selectedOption = option
                            isAnswered = true
                            if (isCorrect) score += 10
                        }
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = option,
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = optionTextColor,
                        modifier = Modifier.weight(1f)
                    )

                    if (isAnswered) {
                        Text(
                            text = when {
                                isCorrect -> "✓"
                                isSelected -> "✗"
                                else -> ""
                            },
                            fontFamily = playfulFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (isAnswered) {
            Button(
                onClick = {
                    if (currentIndex < words.size - 1) {
                        currentIndex++
                        selectedOption = null
                        isAnswered = false
                    } else {
                        onFinishClick()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = orangeMain,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .shadow(5.dp, RoundedCornerShape(50.dp))
            ) {
                Text(
                    text = if (currentIndex == words.size - 1) "HOÀN THÀNH 🎉" else "CÂU TIẾP TỤC ->",
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun QuizScreenPreview() {
    WordieTheme {
        QuizScreen(
            lesson = Lesson(title = "ANIMALS", icon = "🐶"),
            isDarkMode = false,
            isPremium = false,
            onBackClick = {},
            onSpeakClick = {},
            onFinishClick = {}
        )
    }
}
