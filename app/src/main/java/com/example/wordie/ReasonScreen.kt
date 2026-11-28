package com.example.wordie

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReasonScreen(
    onContinueClick: (reason: String) -> Unit
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))
    val orangeMain = Color(0xFFFF8A00)
    val orangeDark = Color(0xFFE56E00)
    val orangeLightBg = Color(0xFFFFF1E0)
    val orangeShadow = Color(0xFFC85A00)

    var selectedReason by remember { mutableStateOf("Travel & Culture") }

    val reasons = listOf(
        Triple("✈️", "Travel & Culture", "Going abroad, exploring new countries"),
        Triple("📚", "School & Exams", "Get high scores, study abroad"),
        Triple("💼", "Career & Work", "Promote job, business communication"),
        Triple("🧠", "Brain Exercise", "Improve memory & smart thinking"),
        Triple("👶", "Kids & Family", "Teach children, family fun time"),
        Triple("🗣️", "Connect Friends", "Talk with foreign friends easily")
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "WHY ARE YOU\nLEARNING?",
            fontFamily = playfulFont,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            lineHeight = 42.sp,
            textAlign = TextAlign.Center,
            color = orangeMain,
            style = TextStyle(
                shadow = Shadow(
                    color = orangeShadow,
                    offset = Offset(0f, 4f),
                    blurRadius = 0f
                )
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "We will tailor your daily vocabulary topics",
            fontFamily = playfulFont,
            fontSize = 14.sp,
            color = Color(0xFF9E9E9E),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            reasons.forEach { (icon, title, desc) ->
                val isSelected = selectedReason == title

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .shadow(if (isSelected) 4.dp else 1.dp, RoundedCornerShape(22.dp))
                        .background(
                            if (isSelected) orangeMain else orangeLightBg,
                            RoundedCornerShape(22.dp)
                        )
                        .border(
                            2.dp,
                            if (isSelected) orangeDark else orangeMain,
                            RoundedCornerShape(22.dp)
                        )
                        .clickable { selectedReason = title }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Text(text = icon, fontSize = 30.sp)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            fontFamily = playfulFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = if (isSelected) Color.White else orangeDark
                        )
                        Text(
                            text = desc,
                            fontFamily = playfulFont,
                            fontSize = 12.sp,
                            color = if (isSelected) Color.White else Color(0xFF757575)
                        )
                    }
                    if (isSelected) {
                        Text(
                            text = "✓",
                            fontFamily = playfulFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White,
                            modifier = Modifier
                                .size(28.dp)
                                .background(Color.White.copy(alpha = 0.3f), CircleShape)
                                .padding(2.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onContinueClick(selectedReason) },
            colors = ButtonDefaults.buttonColors(containerColor = orangeMain, contentColor = Color.White),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "CONTINUE  ->",
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                letterSpacing = 1.5.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
