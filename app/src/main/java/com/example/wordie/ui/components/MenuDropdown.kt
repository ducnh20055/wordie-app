package com.example.wordie.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordie.R
import com.example.wordie.ui.theme.WordieTheme

@Composable
fun MenuDropdown(
    expanded: Boolean,
    isDarkMode: Boolean,
    onDismiss: () -> Unit,
    onThemeToggle: (Boolean) -> Unit,
    onLanguageClick: () -> Unit,
    onHelpClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))

    val orangeMain = Color(0xFFFF8A00)
    val orangeDark = Color(0xFFE56E00)

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White,
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "MENU",
            fontFamily = playfulFont,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            letterSpacing = 1.sp,
            color = orangeDark,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )

        // 1. THEME SÁNG / TỐI
        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = if (isDarkMode) "🌙" else "☀️", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Theme Sáng / Tối",
                            fontFamily = playfulFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF333333)
                        )
                        Text(
                            text = if (isDarkMode) "Đang: Tối" else "Đang: Sáng",
                            fontFamily = playfulFont,
                            fontSize = 12.sp,
                            color = Color(0xFF9E9E9E)
                        )
                    }
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = onThemeToggle,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = orangeMain,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFE0E0E0)
                        )
                    )
                }
            },
            onClick = { onThemeToggle(!isDarkMode) }
        )

        // 2. NGÔN NGỮ
        DropdownMenuItem(
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🌐", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Ngôn ngữ",
                            fontFamily = playfulFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF333333)
                        )
                        Text(
                            text = "Tiếng Việt",
                            fontFamily = playfulFont,
                            fontSize = 12.sp,
                            color = Color(0xFF9E9E9E)
                        )
                    }
                }
            },
            onClick = onLanguageClick
        )

        // 3. TRỢ GIÚP
        DropdownMenuItem(
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "❓", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Trợ giúp",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF333333)
                    )
                }
            },
            onClick = onHelpClick
        )

        // 4. ĐĂNG XUẤT
        DropdownMenuItem(
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🚪", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Đăng xuất",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFFE53935)
                    )
                }
            },
            onClick = onLogoutClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MenuDropdownPreview() {
    WordieTheme {
        MenuDropdown(
            expanded = true,
            isDarkMode = false,
            onDismiss = {},
            onThemeToggle = {},
            onLanguageClick = {},
            onHelpClick = {},
            onLogoutClick = {}
        )
    }
}
