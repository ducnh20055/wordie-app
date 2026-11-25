package com.example.wordie

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordie.models.Lesson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonModeBottomSheet(
    lesson: Lesson,
    isDarkMode: Boolean,
    onDismiss: () -> Unit,
    onModeSelect: (mode: String) -> Unit
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))
    val orangeMain = Color(0xFFFF8A00)
    val orangeDark = Color(0xFFE56E00)
    val orangeLightBg = Color(0xFFFFF1E0)

    val sheetBg = if (isDarkMode) Color(0xFF222222) else Color(0xFFFFF8E1)
    val cardBg = if (isDarkMode) Color(0xFF2D2D2D) else orangeLightBg
    val textColor = if (isDarkMode) Color.White else Color(0xFF333333)
    val subTextColor = if (isDarkMode) Color(0xFFBDBDBD) else Color(0xFF757575)

    val modes = listOf(
        Triple("flashcard", "🎴 Thẻ từ (Flashcard)", "Học từ vựng cơ bản với hình ảnh & ví dụ"),
        Triple("quiz", "❓ Quiz trắc nghiệm", "Thử thách chọn nghĩa tiếng Việt chính xác"),
        Triple("listening", "🎧 Nghe chọn từ", "Luyện kỹ năng nghe phát âm chuẩn AI"),
        Triple("fill_in_blank", "✏️ Điền từ chính tả", "Luyện gõ từ tiếng Anh theo hình ảnh"),
        Triple("word_matching", "🧩 Ghép từ / Ghép thẻ", "Trò chơi ghép thẻ tiếng Anh & tiếng Việt"),
        Triple("balloon_pop", "🎈 Bắt bóng từ vựng", "Trò chơi bắt bóng màu sắc vui nhộn"),
        Triple("virtual_pet", "🦁 Vườn thú & Thú cưng", "Nuôi & chăm sóc thú cưng bằng Ngôi sao ⭐")
    )

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = sheetBg,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            // TIÊU ĐỀ
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = lesson.icon, fontSize = 38.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = lesson.title.uppercase(),
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = orangeMain
                    )
                    Text(
                        text = "CHỌN CHẾ ĐỘ HỌC DÀNH CHO BẠN",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 0.5.sp,
                        color = orangeDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // DANH SÁCH CHẾ ĐỘ HỌC & GAME
            modes.forEach { (modeId, modeTitle, modeDesc) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .shadow(2.dp, RoundedCornerShape(20.dp))
                        .background(cardBg, RoundedCornerShape(20.dp))
                        .clickable {
                            onModeSelect(modeId)
                        }
                        .padding(horizontal = 18.dp, vertical = 14.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = modeTitle,
                            fontFamily = playfulFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = textColor
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = modeDesc,
                            fontFamily = playfulFont,
                            fontSize = 12.sp,
                            color = subTextColor
                        )
                    }

                    Text(
                        text = ">",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = orangeMain,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
