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
fun TargetTimeScreen(
    onContinueClick: (targetTime: String) -> Unit
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))
    val orangeMain = Color(0xFFFF8A00)
    val orangeDark = Color(0xFFE56E00)
    val orangeLightBg = Color(0xFFFFF1E0)
    val orangeShadow = Color(0xFFC85A00)

    var selectedTarget by remember { mutableStateOf("1 Month") }

    val targets = listOf(
        Triple("⚡", "1 Week", "Casual • 5 mins / day"),
        Triple("🚀", "1 Month", "Recommended • 10 mins / day"),
        Triple("🔥", "3 Months", "Serious • 15 mins / day"),
        Triple("🏆", "6 Months", "Mastery • 20 mins / day")
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
            text = "WHEN DO YOU WANT\nTO SEE PROGRESS?",
            fontFamily = playfulFont,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 38.sp,
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
            text = "Select a daily learning commitment",
            fontFamily = playfulFont,
            fontSize = 14.sp,
            color = Color(0xFF9E9E9E),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            targets.forEach { (icon, title, desc) ->
                val isSelected = selectedTarget == title

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
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
                        .clickable { selectedTarget = title }
                        .padding(horizontal = 18.dp, vertical = 16.dp)
                ) {
                    Text(text = icon, fontSize = 32.sp)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title.uppercase(),
                            fontFamily = playfulFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = if (isSelected) Color.White else orangeDark
                        )
                        Text(
                            text = desc,
                            fontFamily = playfulFont,
                            fontSize = 13.sp,
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
            onClick = { onContinueClick(selectedTarget) },
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
