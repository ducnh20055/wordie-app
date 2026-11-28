package com.example.wordie

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordie.ui.theme.WordieTheme

@Composable
fun ProfileScreen(
    playerName: String,
    playerLevel: String,
    isDarkMode: Boolean,
    onBackClick: () -> Unit,
    onTabClick: (tab: String) -> Unit,
    onThemeToggle: (Boolean) -> Unit,
    onLogoutClick: () -> Unit,
    onSignInClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    isLoggedIn: Boolean = true,
    streak: Int = 0,
    lessonsDone: Int = 0,
    stars: Int = 0,
    xp: Int = 0,
    selectedTab: String = "profile"
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))

    val orangeMain = Color(0xFFFF8A00)
    val orangeDark = Color(0xFFE56E00)
    val orangeLightBg = Color(0xFFFFF1E0)
    val orangeShadow = Color(0xFFC85A00)

    val bgColor = if (isDarkMode) Color(0xFF16171B) else Color.White
    val cardBg = if (isDarkMode) Color(0xFF22242B) else orangeLightBg
    val textColor = if (isDarkMode) Color.White else Color(0xFF333333)
    val subTextColor = if (isDarkMode) Color(0xFFBDBDBD) else Color(0xFF757575)

    var showMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // TOP BAR - PROFILE + NÚT 3 GẠCH & MENU
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "PROFILE",
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                letterSpacing = 1.sp,
                color = orangeMain,
                style = TextStyle(
                    shadow = Shadow(
                        color = orangeShadow,
                        offset = Offset(0f, 4f),
                        blurRadius = 0f
                    )
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            Box {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(52.dp)
                        .shadow(4.dp, CircleShape)
                        .background(cardBg, CircleShape)
                        .clickable { showMenu = true }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .width(22.dp)
                                .height(3.dp)
                                .background(orangeDark, RoundedCornerShape(50.dp))
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Box(
                            modifier = Modifier
                                .width(22.dp)
                                .height(3.dp)
                                .background(orangeDark, RoundedCornerShape(50.dp))
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Box(
                            modifier = Modifier
                                .width(22.dp)
                                .height(3.dp)
                                .background(orangeDark, RoundedCornerShape(50.dp))
                        )
                    }
                }

                MenuDropdown(
                    expanded = showMenu,
                    isDarkMode = isDarkMode,
                    onDismiss = { showMenu = false },
                    onThemeToggle = { onThemeToggle(it) },
                    onLanguageClick = {
                        showMenu = false
                        onLanguageClick()
                    },
                    onHelpClick = { showMenu = false },
                    onLogoutClick = {
                        showMenu = false
                        onLogoutClick()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // AVATAR + TÊN
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(110.dp)
                    .shadow(6.dp, CircleShape)
                    .background(cardBg, CircleShape)
            ) {
                Text(
                    text = "🦊",
                    fontSize = 58.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = playerName.uppercase(),
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                letterSpacing = 1.sp,
                color = textColor
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "LEVEL: ${playerLevel.uppercase()} • BEGINNER",
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 1.sp,
                color = orangeDark
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3 Ô THỐNG KÊ
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            ProfileStatBox(
                emoji = "🔥",
                value = "$streak",
                label = "STREAK",
                playfulFont = playfulFont,
                orangeMain = orangeMain,
                cardBg = cardBg,
                subTextColor = subTextColor,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(10.dp))
            ProfileStatBox(
                emoji = "⭐",
                value = "$xp",
                label = "XP",
                playfulFont = playfulFont,
                orangeMain = orangeMain,
                cardBg = cardBg,
                subTextColor = subTextColor,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(10.dp))
            ProfileStatBox(
                emoji = "📚",
                value = "$lessonsDone", // value = streak: Int
                label = "LESSONS",
                playfulFont = playfulFont,
                orangeMain = orangeMain,
                cardBg = cardBg,
                subTextColor = subTextColor,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // THÀNH TÍCH
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "ACHIEVEMENTS",
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                letterSpacing = 1.sp,
                color = orangeDark
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                AchievementItem(
                    emoji = "🏆",
                    label = "First\nLesson",
                    isUnlocked = true,
                    playfulFont = playfulFont,
                    cardBg = cardBg,
                    textColor = textColor,
                    isDarkMode = isDarkMode
                )
                Spacer(modifier = Modifier.width(10.dp))
                AchievementItem(
                    emoji = "🔥",
                    label = "7 Days\nStreak",
                    isUnlocked = true,
                    playfulFont = playfulFont,
                    cardBg = cardBg,
                    textColor = textColor,
                    isDarkMode = isDarkMode
                )
                Spacer(modifier = Modifier.width(10.dp))
                AchievementItem(
                    emoji = "🌟",
                    label = "100\nXP",
                    isUnlocked = true,
                    playfulFont = playfulFont,
                    cardBg = cardBg,
                    textColor = textColor,
                    isDarkMode = isDarkMode
                )
                Spacer(modifier = Modifier.width(10.dp))
                AchievementItem(
                    emoji = "👑",
                    label = "Master",
                    isUnlocked = false,
                    playfulFont = playfulFont,
                    cardBg = cardBg,
                    textColor = textColor,
                    isDarkMode = isDarkMode
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // MENU
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            if (!isLoggedIn) {
                ProfileMenuItem(
                    icon = "🔑",
                    title = "Sign In",
                    playfulFont = playfulFont,
                    cardBg = cardBg,
                    textColor = textColor,
                    onClick = { onSignInClick() }
                )
                ProfileMenuItem(
                    icon = "📝",
                    title = "Create Account",
                    playfulFont = playfulFont,
                    cardBg = cardBg,
                    textColor = textColor,
                    onClick = { onSignUpClick() }
                )
            } else {
                ProfileMenuItem(
                    icon = "🌐",
                    title = "Language",
                    playfulFont = playfulFont,
                    cardBg = cardBg,
                    textColor = textColor,
                    onClick = { onLanguageClick() }
                )
                ProfileMenuItem(
                    icon = "✏️",
                    title = "Edit Profile",
                    playfulFont = playfulFont,
                    cardBg = cardBg,
                    textColor = textColor,
                    onClick = {}
                )
                ProfileMenuItem(
                    icon = "🚪",
                    title = "Log Out",
                    playfulFont = playfulFont,
                    cardBg = cardBg,
                    textColor = textColor,
                    onClick = { onLogoutClick() }
                )
            }
        }

        // NAV DƯỚI
        BottomNavBar(
            selectedTab = selectedTab,
            isDarkMode = isDarkMode,
            onTabClick = onTabClick
        )
    }
}

@Composable
private fun ProfileStatBox(
    emoji: String,
    label: String,
    value: String,
    playfulFont: FontFamily,
    orangeMain: Color,
    cardBg: Color,
    subTextColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(20.dp))
            .background(cardBg, RoundedCornerShape(20.dp))
            .padding(vertical = 14.dp)
    ) {
        Text(text = emoji, fontSize = 22.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontFamily = playfulFont,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = orangeMain
        )
        Text(
            text = label,
            fontFamily = playfulFont,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            letterSpacing = 1.sp,
            color = subTextColor
        )
    }
}

@Composable
private fun AchievementItem(
    emoji: String,
    label: String,
    isUnlocked: Boolean,
    playfulFont: FontFamily,
    cardBg: Color,
    textColor: Color,
    isDarkMode: Boolean
) {
    val unlockedBg = cardBg
    val lockedBg = if (isDarkMode) Color(0xFF222222) else Color(0xFFF5F5F5)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .shadow(2.dp, RoundedCornerShape(18.dp))
            .background(
                if (isUnlocked) unlockedBg else lockedBg,
                RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        Text(
            text = emoji,
            fontSize = 28.sp,
            color = if (isUnlocked) Color.Unspecified else Color(0xFFBDBDBD)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontFamily = playfulFont,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = if (isUnlocked) textColor else Color(0xFFBDBDBD),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun ProfileMenuItem(
    icon: String,
    title: String,
    playfulFont: FontFamily,
    cardBg: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .shadow(2.dp, RoundedCornerShape(18.dp))
            .background(cardBg, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 15.dp)
    ) {
        Text(text = icon, fontSize = 22.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            fontFamily = playfulFont,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = ">",
            fontFamily = playfulFont,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFFFF8A00)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileScreenPreview() {
    WordieTheme {
        ProfileScreen(
            playerName = "Duc Anh",
            playerLevel = "Beginner",
            isDarkMode = false,
            onBackClick = {},
            onTabClick = {},
            onThemeToggle = {},
            onLogoutClick = {}
        )
    }
}
