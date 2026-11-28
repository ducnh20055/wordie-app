package com.example.wordie.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordie.R

@Composable
fun BottomNavBar(
    selectedTab: String,
    isDarkMode: Boolean,
    onTabClick: (tab: String) -> Unit
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))
    val orangeMain = Color(0xFFFF8A00)
    
    // Light theme style matching Image 1 across all screens
    val cardBg = if (isDarkMode) Color(0xFF2D2D2D) else Color(0xFFFFF1E0)
    val activeBg = if (isDarkMode) Color(0xFF3D3D3D) else Color.White

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(50.dp))
                .background(cardBg, RoundedCornerShape(50.dp))
                .padding(vertical = 8.dp, horizontal = 10.dp)
        ) {
            // 1. GRAMMAR (HOME)
            val isGrammar = selectedTab == "grammar" || selectedTab == "home"
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(52.dp)
                    .then(
                        if (isGrammar) Modifier
                            .shadow(4.dp, RoundedCornerShape(50.dp))
                            .background(activeBg, RoundedCornerShape(50.dp))
                            .padding(horizontal = 16.dp)
                        else Modifier.padding(horizontal = 8.dp)
                    )
                    .clickable { onTabClick("grammar") }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🏠", fontSize = 22.sp)
                    if (isGrammar) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "GRAMMAR",
                            fontFamily = playfulFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 0.5.sp,
                            color = orangeMain
                        )
                    }
                }
            }

            // 2. VOCABULARY
            val isVocab = selectedTab == "vocabulary"
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(52.dp)
                    .then(
                        if (isVocab) Modifier
                            .shadow(4.dp, RoundedCornerShape(50.dp))
                            .background(activeBg, RoundedCornerShape(50.dp))
                            .padding(horizontal = 16.dp)
                        else Modifier.padding(horizontal = 8.dp)
                    )
                    .clickable { onTabClick("vocabulary") }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "📚", fontSize = 22.sp)
                    if (isVocab) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "VOCAB",
                            fontFamily = playfulFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 0.5.sp,
                            color = orangeMain
                        )
                    }
                }
            }

            // 3. STATISTICS
            val isStats = selectedTab == "statistics"
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(52.dp)
                    .then(
                        if (isStats) Modifier
                            .shadow(4.dp, RoundedCornerShape(50.dp))
                            .background(activeBg, RoundedCornerShape(50.dp))
                            .padding(horizontal = 16.dp)
                        else Modifier.padding(horizontal = 8.dp)
                    )
                    .clickable { onTabClick("statistics") }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "📊", fontSize = 22.sp)
                    if (isStats) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "STATS",
                            fontFamily = playfulFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 0.5.sp,
                            color = orangeMain
                        )
                    }
                }
            }

            // 4. PROFILE
            val isProfile = selectedTab == "profile"
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(52.dp)
                    .then(
                        if (isProfile) Modifier
                            .shadow(4.dp, RoundedCornerShape(50.dp))
                            .background(activeBg, RoundedCornerShape(50.dp))
                            .padding(horizontal = 16.dp)
                        else Modifier.padding(horizontal = 8.dp)
                    )
                    .clickable { onTabClick("profile") }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "👤", fontSize = 22.sp)
                    if (isProfile) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "PROFILE",
                            fontFamily = playfulFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 0.5.sp,
                            color = orangeMain
                        )
                    }
                }
            }
        }
    }
}
