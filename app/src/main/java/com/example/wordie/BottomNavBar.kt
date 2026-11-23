package com.example.wordie

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordie.ui.theme.WordieTheme

@Composable
fun BottomNavBar(
    selectedTab: String,
    isDarkMode: Boolean = false,
    onTabClick: (tab: String) -> Unit
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))

    val orangeMain = Color(0xFFFF8A00)
    val orangeDark = Color(0xFFE56E00)

    // MÀU THEO THEME - ĐÂY LÀ CHỖ SỬA
    val navBg = if (isDarkMode) Color(0xFF2D2D2D) else Color(0xFFFFF1E0)
    val unselectedColor = if (isDarkMode) Color(0xFFBDBDBD) else Color(0xFF9E9E9E)
    val selectedBg = if (isDarkMode) Color(0xFF3D3D3D) else Color.White

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .shadow(4.dp, RoundedCornerShape(50.dp))
            .background(navBg, RoundedCornerShape(50.dp))
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        // HOME
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable { onTabClick("home") }
                .background(
                    if (selectedTab == "home") selectedBg else Color.Transparent,
                    CircleShape
                )
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(text = "🏠", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "HOME",
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                color = if (selectedTab == "home") orangeMain else unselectedColor
            )
        }

        // PLAY
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(56.dp)
                .shadow(4.dp, CircleShape)
                .background(orangeMain, CircleShape)
                .clickable { onTabClick("play") }
        ) {
            Text(text = "▶", fontSize = 22.sp, color = Color.White)
        }

        // PROFILE
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable { onTabClick("profile") }
                .background(
                    if (selectedTab == "profile") selectedBg else Color.Transparent,
                    CircleShape
                )
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(text = "👤", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "PROFILE",
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                color = if (selectedTab == "profile") orangeMain else unselectedColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomNavBarLightPreview() {
    WordieTheme {
        BottomNavBar(
            selectedTab = "home",
            isDarkMode = false,
            onTabClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomNavBarDarkPreview() {
    WordieTheme {
        BottomNavBar(
            selectedTab = "home",
            isDarkMode = true,
            onTabClick = {}
        )
    }
}