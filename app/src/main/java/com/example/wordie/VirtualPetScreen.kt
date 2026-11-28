package com.example.wordie

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun VirtualPetScreen(
    playerName: String,
    stars: Int,
    isDarkMode: Boolean,
    onBackClick: () -> Unit
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))
    val orangeMain = Color(0xFFFF8A00)
    val orangeDark = Color(0xFFE56E00)
    val orangeLightBg = Color(0xFFFFF1E0)
    val orangeShadow = Color(0xFFC85A00)

    val bgColor = if (isDarkMode) Color(0xFF1A1A1A) else Color.White
    val cardBg = if (isDarkMode) Color(0xFF2D2D2D) else orangeLightBg
    val textColor = if (isDarkMode) Color.White else Color(0xFF333333)

    var happiness by remember { mutableIntStateOf(80) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(44.dp)
                    .shadow(2.dp, CircleShape)
                    .background(cardBg, CircleShape)
                    .clickable { onBackClick() }
            ) {
                Text(
                    text = "←",
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = orangeDark
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "VIRTUAL PET",
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = orangeMain,
                style = TextStyle(
                    shadow = Shadow(
                        color = orangeShadow,
                        offset = Offset(0f, 3f),
                        blurRadius = 0f
                    )
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "⭐ $stars", fontFamily = playfulFont, fontSize = 18.sp, color = orangeMain)
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(180.dp)
                .shadow(6.dp, CircleShape)
                .background(cardBg, CircleShape)
        ) {
            Text(text = if (happiness > 50) "🦊" else "🦊💤", fontSize = 90.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "$playerName's Pet",
            fontFamily = playfulFont,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = textColor
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Happiness: $happiness%",
            fontFamily = playfulFont,
            fontSize = 18.sp,
            color = orangeDark
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { if (happiness < 100) happiness += 10 },
            colors = ButtonDefaults.buttonColors(containerColor = orangeMain),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text(text = "FEED PET 🍎", fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}
