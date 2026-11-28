package com.example.wordie

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionBottomSheet(
    currentLanguage: String,
    isDarkMode: Boolean,
    onDismiss: () -> Unit,
    onLanguageSelect: (language: String) -> Unit
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))
    val orangeMain = Color(0xFFFF8A00)
    val orangeDark = Color(0xFFE56E00)
    val orangeLightBg = Color(0xFFFFF1E0)

    val bgColor = if (isDarkMode) Color(0xFF1A1A1A) else Color.White
    val cardBg = if (isDarkMode) Color(0xFF2D2D2D) else orangeLightBg
    val textColor = if (isDarkMode) Color.White else Color(0xFF333333)

    val languages = listOf(
        Triple("🇻🇳", "Vietnamese", "Tiếng Việt"),
        Triple("🇺🇸", "English", "English"),
        Triple("🇪🇸", "Spanish", "Español"),
        Triple("🇫🇷", "French", "Français"),
        Triple("🇩🇪", "German", "Deutsch"),
        Triple("🇯🇵", "Japanese", "日本語"),
        Triple("🇰🇷", "Korean", "한국어"),
        Triple("🇨🇳", "Chinese", "中文")
    )

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = bgColor,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .size(width = 44.dp, height = 5.dp)
                    .background(Color(0xFFBDBDBD), RoundedCornerShape(50.dp))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 28.dp)
        ) {
            // HEADER BAR
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "🌐 CHỌN NGÔN NGỮ",
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = orangeMain
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(36.dp)
                        .shadow(2.dp, CircleShape)
                        .background(cardBg, CircleShape)
                        .clickable { onDismiss() }
                ) {
                    Text(
                        text = "✕",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = textColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // SCROLLABLE COMPACT LANGUAGE LIST
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 360.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                languages.forEach { (flag, code, name) ->
                    val isSelected = currentLanguage.equals(code, ignoreCase = true)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .shadow(if (isSelected) 3.dp else 1.dp, RoundedCornerShape(18.dp))
                            .background(
                                if (isSelected) orangeMain else cardBg,
                                RoundedCornerShape(18.dp)
                            )
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = if (isSelected) orangeDark else Color.Transparent,
                                shape = RoundedCornerShape(18.dp)
                            )
                            .clickable {
                                onLanguageSelect(code)
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Text(text = flag, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = name,
                            fontFamily = playfulFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = if (isSelected) Color.White else textColor,
                            modifier = Modifier.weight(1f)
                        )
                        if (isSelected) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color.White.copy(alpha = 0.3f), CircleShape)
                            ) {
                                Text(
                                    text = "✓",
                                    fontFamily = playfulFont,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
