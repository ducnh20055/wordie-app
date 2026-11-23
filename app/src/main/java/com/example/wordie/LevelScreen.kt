package com.example.wordie

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.wordie.ui.theme.WordieTheme

@Composable
fun LevelScreen(
    onContinueClick: (level: String) -> Unit
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))

    val orangeMain = Color(0xFFFF8A00)
    val orangeDark = Color(0xFFE56E00)
    val orangeLightBg = Color(0xFFFFF1E0)
    val orangeShadow = Color(0xFFC85A00)

    var selectedLevel by remember { mutableStateOf("Beginner") }

    val levels = listOf(
        Triple("🌱", "Beginner", "Just starting, basic words"),
        Triple("🌿", "Elementary", "Know simple words, easy talk"),
        Triple("🌳", "Intermediate", "Good at listening and reading"),
        Triple("🔥", "Advanced", "Master, even hard words are easy")
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 32.dp)
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "CHOOSE YOUR\nLEVEL!",
            fontFamily = playfulFont,
            fontWeight = FontWeight.Bold,
            fontSize = 38.sp,
            lineHeight = 44.sp,
            textAlign = TextAlign.Center,
            color = orangeMain,
            style = TextStyle(
                shadow = Shadow(
                    color = orangeShadow,
                    offset = Offset(0f, 5f),
                    blurRadius = 0f
                )
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Pick one that fits you best",
            fontFamily = playfulFont,
            fontSize = 15.sp,
            color = Color(0xFF9E9E9E),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        levels.forEach { (icon, title, desc) ->
            val isSelected = selectedLevel == title

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .shadow(if (isSelected) 6.dp else 2.dp, RoundedCornerShape(24.dp))
                    .background(
                        if (isSelected) orangeMain else orangeLightBg,
                        RoundedCornerShape(24.dp)
                    )
                    .border(
                        2.dp,
                        if (isSelected) orangeDark else orangeMain,
                        RoundedCornerShape(24.dp)
                    )
                    .clickable { selectedLevel = title }
                    .padding(horizontal = 18.dp, vertical = 16.dp)
            ) {
                Text(
                    text = icon,
                    fontSize = 32.sp
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title.uppercase(),
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp,
                        letterSpacing = 0.5.sp,
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
                        fontSize = 22.sp,
                        color = Color.White,
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.White.copy(alpha = 0.3f), CircleShape)
                            .padding(2.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onContinueClick(selectedLevel) },
            colors = ButtonDefaults.buttonColors(
                containerColor = orangeMain,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
        ) {
            Text(
                text = "CONTINUE  ->",
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                letterSpacing = 1.5.sp
            )
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LevelScreenPreview() {
    WordieTheme {
        LevelScreen(
            onContinueClick = {}
        )
    }
}