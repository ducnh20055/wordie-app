package com.example.wordie

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import kotlinx.coroutines.delay

@Composable
fun BalloonPopScreen(
    lesson: Lesson,
    isDarkMode: Boolean,
    onBackClick: () -> Unit,
    onSpeakClick: (word: String) -> Unit,
    onFinishClick: () -> Unit
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))
    val orangeMain = Color(0xFFFF8A00)
    val orangeDark = Color(0xFFE56E00)

    val kidBg = if (isDarkMode) Color(0xFF1A1A1A) else Color(0xFFFFF8E1)
    val cardBg = if (isDarkMode) Color(0xFF2D2D2D) else Color(0xFFFFF1E0)
    val textColor = if (isDarkMode) Color.White else Color(0xFF333333)

    val balloonColors = listOf(
        Color(0xFFFF8A80), // Hồng kẹo
        Color(0xFFFFD54F), // Vàng kẹo
        Color(0xFF69F0AE), // Xanh lá kẹo
        Color(0xFF40C4FF)  // Xanh dương kẹo
    )

    var showExitDialog by remember { mutableStateOf(false) }

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

    val words = if (lesson.words.isNotEmpty()) lesson.words else listOf(
        WordItem(emoji = "🐶", english = "DOG", vietnamese = "Con chó"),
        WordItem(emoji = "🐱", english = "CAT", vietnamese = "Con mèo"),
        WordItem(emoji = "🍎", english = "APPLE", vietnamese = "Quả táo"),
        WordItem(emoji = "🐘", english = "ELEPHANT", vietnamese = "Con voi")
    )

    var currentIndex by remember { mutableIntStateOf(0) }
    var poppedIndex by remember { mutableStateOf<Int?>(null) }
    var isPoppedCorrect by remember { mutableStateOf<Boolean?>(null) }
    var starsEarned by remember { mutableIntStateOf(0) }

    val currentWord = words[currentIndex]

    LaunchedEffect(currentIndex) {
        onSpeakClick(currentWord.english)
    }

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
            .background(kidBg)
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
                    .clickable { showExitDialog = true }
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
                    text = "BẮT BÓNG TỪ VỰNG 🎈",
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
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
                text = "⭐ $starsEarned",
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFFFFB300)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // HEADER LINH VẬT WORDIE
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(24.dp))
                .background(cardBg, RoundedCornerShape(24.dp))
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(
                    id = when (isPoppedCorrect) {
                        true -> R.drawable.wordie_excited
                        false -> R.drawable.wordie_sad
                        else -> R.drawable.wordie_happy
                    }
                ),
                contentDescription = "Mascot",
                modifier = Modifier.size(72.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = when (isPoppedCorrect) {
                        true -> "XUẤT SẮC! BẮT ĐÚNG RỒI! 🎉"
                        false -> "ỐI! THỬ BẮT BÓNG KHÁC NÀO! 💪"
                        else -> "BẮT BÓNG CÓ NGHĨA CỦA:"
                    },
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = orangeDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${currentWord.emoji} ${currentWord.english}",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = orangeMain
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "🔊",
                        fontSize = 22.sp,
                        modifier = Modifier.clickable { onSpeakClick(currentWord.english) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // 4 QUẢ BÓNG BAY MÀU SẮC
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            options.forEachIndexed { index, option ->
                val isCorrect = option == currentWord.vietnamese
                val isPopped = poppedIndex == index
                val color = balloonColors[index % balloonColors.size]

                val scale by animateFloatAsState(
                    targetValue = if (isPopped && isPoppedCorrect == true) 1.1f else 1.0f,
                    animationSpec = tween(300),
                    label = "balloonScale"
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .scale(scale)
                        .shadow(6.dp, RoundedCornerShape(36.dp))
                        .background(
                            if (isPopped && isPoppedCorrect == false) Color(0xFFE53935) else color,
                            RoundedCornerShape(36.dp)
                        )
                        .clickable {
                            poppedIndex = index
                            if (isCorrect) {
                                isPoppedCorrect = true
                                starsEarned += 1
                                onSpeakClick(currentWord.english)
                            } else {
                                isPoppedCorrect = false
                            }
                        }
                        .padding(horizontal = 24.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "🎈",
                            fontSize = 24.sp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = option,
                            fontFamily = playfulFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isPoppedCorrect == true) {
            Button(
                onClick = {
                    if (currentIndex < words.size - 1) {
                        currentIndex++
                        poppedIndex = null
                        isPoppedCorrect = null
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
                    text = if (currentIndex == words.size - 1) "HOÀN THÀNH 🎉 (+1 ⭐)" else "CÂU TIẾP TỤC 🎈 ->",
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
private fun BalloonPopScreenPreview() {
    WordieTheme {
        BalloonPopScreen(
            lesson = Lesson(title = "ANIMALS", icon = "🐶"),
            isDarkMode = false,
            onBackClick = {},
            onSpeakClick = {},
            onFinishClick = {}
        )
    }
}
