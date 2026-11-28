package com.example.wordie.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import com.example.wordie.R
import com.example.wordie.models.Lesson

@Composable
fun LessonCard(
    lesson: Lesson,
    isLocked: Boolean,
    cardBg: Color,
    textColor: Color,
    subTextColor: Color,
    orangeMain: Color,
    onClick: () -> Unit
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .shadow(2.dp, RoundedCornerShape(20.dp))
            .background(cardBg, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(text = lesson.icon, fontSize = 32.sp)
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = lesson.title,
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    letterSpacing = 0.5.sp,
                    color = textColor
                )
                if (isLocked) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "👑", fontSize = 14.sp)
                }
            }
            Text(
                text = if (isLocked) "Premium - Bấm để mở khóa" else lesson.info,
                fontFamily = playfulFont,
                fontSize = 13.sp,
                color = if (isLocked) orangeMain else subTextColor
            )
        }
        Text(
            text = if (isLocked) "🔒" else "PLAY >",
            fontFamily = playfulFont,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = orangeMain,
            textAlign = TextAlign.Center
        )
    }
}
