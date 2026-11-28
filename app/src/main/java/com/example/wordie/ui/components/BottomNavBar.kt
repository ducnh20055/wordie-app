package com.example.wordie.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
    val cardBg = if (isDarkMode) Color(0xFF2D2D2D) else Color(0xFFFFF1E0)
    val textColor = if (isDarkMode) Color.White else Color(0xFF333333)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(50.dp))
                .background(cardBg, RoundedCornerShape(50.dp))
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            // 1. HOME TAB
            val isHome = selectedTab == "home"
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(52.dp)
                    .then(
                        if (isHome) Modifier
                            .shadow(4.dp, RoundedCornerShape(50.dp))
                            .background(Color.White, RoundedCornerShape(50.dp))
                            .padding(horizontal = 20.dp)
                        else Modifier.padding(horizontal = 12.dp)
                    )
                    .clickable { onTabClick("home") }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🏠", fontSize = 24.sp)
                    if (isHome) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "HOME",
                            fontFamily = playfulFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            letterSpacing = 1.sp,
                            color = orangeMain
                        )
                    }
                }
            }

            // 2. PLAY TAB (NÚT NỔI GIỮA)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(56.dp)
                    .shadow(6.dp, CircleShape)
                    .background(orangeMain, CircleShape)
                    .clickable { onTabClick("play") }
            ) {
                Text(
                    text = "▶",
                    fontSize = 22.sp,
                    color = Color.White
                )
            }

            // 3. PROFILE TAB
            val isProfile = selectedTab == "profile"
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(52.dp)
                    .then(
                        if (isProfile) Modifier
                            .shadow(4.dp, RoundedCornerShape(50.dp))
                            .background(Color.White, RoundedCornerShape(50.dp))
                            .padding(horizontal = 20.dp)
                        else Modifier.padding(horizontal = 12.dp)
                    )
                    .clickable { onTabClick("profile") }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "👤", fontSize = 22.sp)
                    if (isProfile) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PROFILE",
                            fontFamily = playfulFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            letterSpacing = 1.sp,
                            color = orangeMain
                        )
                    }
                }
            }
        }
    }
}
