package com.example.wordie

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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

private data class MatchingCard(
    val id: String,
    val text: String,
    val wordId: String,
    val isEnglish: Boolean
)

@Composable
fun WordMatchingScreen(
    lesson: Lesson,
    isDarkMode: Boolean,
    onBackClick: () -> Unit,
    onFinishClick: () -> Unit
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))
    val orangeMain = Color(0xFFFF8A00)
    val orangeDark = Color(0xFFE56E00)
    val orangeLightBg = Color(0xFFFFF1E0)

    val bgColor = if (isDarkMode) Color(0xFF1A1A1A) else Color.White
    val cardBg = if (isDarkMode) Color(0xFF2D2D2D) else orangeLightBg
    val textColor = if (isDarkMode) Color.White else Color(0xFF333333)

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

    val words = if (lesson.words.isNotEmpty()) lesson.words else listOf(
        WordItem(id = "dog", emoji = "🐶", english = "DOG", vietnamese = "Con chó"),
        WordItem(id = "cat", emoji = "🐱", english = "CAT", vietnamese = "Con mèo"),
        WordItem(id = "apple", emoji = "🍎", english = "APPLE", vietnamese = "Quả táo"),
        WordItem(id = "bird", emoji = "🐦", english = "BIRD", vietnamese = "Con chim")
    )

    // Chọn ngẫu nhiên 4 cặp từ
    val sampleWords = remember { words.shuffled().take(4) }

    // CỘT BÊN TRÁI: Chỉ chứa thẻ Tiếng Anh
    val englishCards = remember {
        sampleWords.map { word ->
            MatchingCard(id = "en_${word.id}", text = "${word.emoji} ${word.english}", wordId = word.id, isEnglish = true)
        }.shuffled()
    }

    // CỘT BÊN PHẢI: Chỉ chứa thẻ Tiếng Việt
    val vietnameseCards = remember {
        sampleWords.map { word ->
            MatchingCard(id = "vi_${word.id}", text = word.vietnamese, wordId = word.id, isEnglish = false)
        }.shuffled()
    }

    var selectedCard1 by remember { mutableStateOf<MatchingCard?>(null) }
    var selectedCard2 by remember { mutableStateOf<MatchingCard?>(null) }
    val matchedWordIds = remember { mutableStateListOf<String>() }

    // Kiểm tra ghép đôi
    LaunchedEffect(selectedCard1, selectedCard2) {
        val c1 = selectedCard1
        val c2 = selectedCard2
        if (c1 != null && c2 != null) {
            if (c1.wordId == c2.wordId) {
                // ĐÚNG -> Hiện màu xanh 400ms rồi biến mất
                delay(400)
                matchedWordIds.add(c1.wordId)
            } else {
                // SAI -> Hiện màu đỏ nhạt 600ms rồi nhả thẻ ra (giống ảnh)
                delay(600)
            }
            selectedCard1 = null
            selectedCard2 = null
        }
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
                    text = "TRÒ CHƠI GHÉP THẺ",
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
                text = "${matchedWordIds.size}/${sampleWords.size}",
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = orangeDark
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Nối thẻ tiếng Anh (Trái) với thẻ tiếng Việt (Phải):",
            fontFamily = playfulFont,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = orangeDark,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // LƯỚI THẺ: CỘT TRÁI (TIẾNG ANH) - CỘT PHẢI (TIẾNG VIỆT)
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            for (i in 0 until sampleWords.size) {
                val enCard = englishCards.getOrNull(i)
                val viCard = vietnameseCards.getOrNull(i)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // CỘT BÊN TRÁI: TIẾNG ANH
                    Box(modifier = Modifier.weight(1f)) {
                        if (enCard != null) {
                            MatchingCardItem(
                                card = enCard,
                                isMatched = matchedWordIds.contains(enCard.wordId),
                                selectedCard1 = selectedCard1,
                                selectedCard2 = selectedCard2,
                                isDarkMode = isDarkMode,
                                cardBg = cardBg,
                                textColor = textColor,
                                orangeMain = orangeMain,
                                orangeLightBg = orangeLightBg,
                                playfulFont = playfulFont,
                                onCardClick = { card ->
                                    if (selectedCard1 == null) {
                                        selectedCard1 = card
                                    } else if (selectedCard2 == null && card != selectedCard1) {
                                        selectedCard2 = card
                                    }
                                }
                            )
                        }
                    }

                    // CỘT BÊN PHẢI: TIẾNG VIỆT
                    Box(modifier = Modifier.weight(1f)) {
                        if (viCard != null) {
                            MatchingCardItem(
                                card = viCard,
                                isMatched = matchedWordIds.contains(viCard.wordId),
                                selectedCard1 = selectedCard1,
                                selectedCard2 = selectedCard2,
                                isDarkMode = isDarkMode,
                                cardBg = cardBg,
                                textColor = textColor,
                                orangeMain = orangeMain,
                                orangeLightBg = orangeLightBg,
                                playfulFont = playfulFont,
                                onCardClick = { card ->
                                    if (selectedCard1 == null) {
                                        selectedCard1 = card
                                    } else if (selectedCard2 == null && card != selectedCard1) {
                                        selectedCard2 = card
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (matchedWordIds.size == sampleWords.size) {
            Text(
                text = "🎉 XUẤT SẮC! BẠN ĐÃ GHÉP THÀNH CÔNG TẤT CẢ CÁC THẺ!",
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFF4CAF50),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            Button(
                onClick = { onFinishClick() },
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
                    text = "HOÀN THÀNH BÀI HỌC ✓",
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun MatchingCardItem(
    card: MatchingCard,
    isMatched: Boolean,
    selectedCard1: MatchingCard?,
    selectedCard2: MatchingCard?,
    isDarkMode: Boolean,
    cardBg: Color,
    textColor: Color,
    orangeMain: Color,
    orangeLightBg: Color,
    playfulFont: FontFamily,
    onCardClick: (MatchingCard) -> Unit
) {
    if (isMatched) {
        // Đã ghép đúng ➔ Ô BIẾN MẤT (Chuyển thành khoảng trống)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
        )
    } else {
        val isSelected = selectedCard1 == card || selectedCard2 == card
        val hasTwoSelected = selectedCard1 != null && selectedCard2 != null
        val isCorrectPair = hasTwoSelected && selectedCard1.wordId == selectedCard2.wordId
        val isIncorrectPair = hasTwoSelected && selectedCard1.wordId != selectedCard2.wordId

        // MÀU NỀN & VIỀN & CHỮ THEO TRẠNG THÁI
        val cardBackgroundColor = when {
            isSelected && isCorrectPair -> Color(0xFFE8F5E9) // Xanh lá nhạt khi ghép đúng
            isSelected && isIncorrectPair -> Color(0xFFFFEBEE) // Đỏ nhạt khi ghép sai (như ảnh)
            isSelected -> if (isDarkMode) Color(0xFF3D2A1A) else orangeLightBg
            else -> cardBg
        }

        val cardBorderColor = when {
            isSelected && isCorrectPair -> Color(0xFF81C784)
            isSelected && isIncorrectPair -> Color(0xFFEF9A9A) // Viền đỏ nhạt (như ảnh)
            isSelected -> orangeMain
            else -> Color.Transparent
        }

        val cardTextColor = when {
            isSelected && isCorrectPair -> Color(0xFF2E7D32)
            isSelected && isIncorrectPair -> Color(0xFFC62828) // Chữ đỏ (như ảnh)
            isSelected -> orangeMain
            else -> textColor
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .shadow(if (isSelected) 6.dp else 2.dp, RoundedCornerShape(20.dp))
                .background(cardBackgroundColor, RoundedCornerShape(20.dp))
                .border(2.dp, cardBorderColor, RoundedCornerShape(20.dp))
                .clickable(enabled = !hasTwoSelected) {
                    onCardClick(card)
                }
                .padding(10.dp)
        ) {
            Text(
                text = card.text, // KO CÓ NÚT TICK ✓
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = cardTextColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun WordMatchingScreenPreview() {
    WordieTheme {
        WordMatchingScreen(
            lesson = Lesson(title = "ANIMALS", icon = "🐶"),
            isDarkMode = false,
            onBackClick = {},
            onFinishClick = {}
        )
    }
}
