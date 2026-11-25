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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordie.models.Lesson
import com.example.wordie.models.WordItem
import com.example.wordie.ui.theme.WordieTheme

@Composable
fun FillInTheBlankScreen(
    lesson: Lesson,
    isDarkMode: Boolean,
    onBackClick: () -> Unit,
    onSpeakClick: (word: String) -> Unit,
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
        WordItem(emoji = "🐶", english = "DOG", vietnamese = "Con chó", example = "The dog is happy."),
        WordItem(emoji = "🐱", english = "CAT", vietnamese = "Con mèo", example = "The cat is cute."),
        WordItem(emoji = "🍎", english = "APPLE", vietnamese = "Quả táo", example = "I eat an apple.")
    )

    var currentIndex by remember { mutableIntStateOf(0) }
    var inputText by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    val currentWord = words[currentIndex]

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
                    text = "LUYỆN ĐIỀN TỪ",
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

        // CARD GỢI Ý HÌNH + NGHĨA
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .shadow(6.dp, RoundedCornerShape(28.dp))
                .background(cardBg, RoundedCornerShape(28.dp))
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = currentWord.emoji, fontSize = 60.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = currentWord.vietnamese.uppercase(),
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = textColor
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Gõ từ tiếng Anh tương ứng:",
            fontFamily = playfulFont,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = orangeDark,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Ô NHẬP TỪ TIẾNG ANH
        OutlinedTextField(
            value = inputText,
            onValueChange = { newValue ->
                inputText = newValue
                if (isChecked && !isCorrect) {
                    isChecked = false // Cho phép gõ/xóa lại thoải mái khi vừa gõ sai
                }
            },
            placeholder = {
                if (!isFocused && inputText.isEmpty()) {
                    Text(
                        text = "Gõ từ tiếng Anh ở đây...",
                        fontFamily = playfulFont,
                        fontSize = 16.sp,
                        color = Color(0xFFBDBDBD)
                    )
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
            textStyle = TextStyle(
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                letterSpacing = 2.sp,
                color = textColor,
                textAlign = TextAlign.Start
            ),
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isChecked) (if (isCorrect) Color(0xFF4CAF50) else Color(0xFFE53935)) else orangeMain,
                unfocusedBorderColor = if (isChecked) (if (isCorrect) Color(0xFF4CAF50) else Color(0xFFE53935)) else Color(0xFFE0E0E0),
                focusedContainerColor = cardBg,
                unfocusedContainerColor = cardBg
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .onFocusChanged { isFocused = it.isFocused }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isChecked) {
            Text(
                text = if (isCorrect) "✓ CHÍNH XÁC!" else "✗ CHƯA ĐÚNG! HÃY THỬ LẠI.",
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFE53935),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // NÚT CHECK / THỬ LẠI / CÂU TIẾP TỤC
        Button(
            onClick = {
                if (!isChecked) {
                    if (inputText.trim().equals(currentWord.english.trim(), ignoreCase = true)) {
                        isCorrect = true
                        isChecked = true
                        onSpeakClick(currentWord.english)
                    } else {
                        isCorrect = false
                        isChecked = true
                    }
                } else {
                    if (isCorrect) {
                        if (currentIndex < words.size - 1) {
                            currentIndex++
                            inputText = ""
                            isChecked = false
                            isCorrect = false
                        } else {
                            onFinishClick()
                        }
                    } else {
                        // Nhấn nút THỬ LẠI -> Xóa text cũ để người dùng nhập mới
                        inputText = ""
                        isChecked = false
                    }
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
                text = when {
                    !isChecked -> "KIỂM TRA 🔍"
                    !isCorrect -> "THỬ LẠI 🔄"
                    currentIndex == words.size - 1 -> "HOÀN THÀNH 🎉"
                    else -> "CÂU TIẾP TỤC ->"
                },
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FillInTheBlankScreenPreview() {
    WordieTheme {
        FillInTheBlankScreen(
            lesson = Lesson(title = "ANIMALS", icon = "🐶"),
            isDarkMode = false,
            onBackClick = {},
            onSpeakClick = {},
            onFinishClick = {}
        )
    }
}
