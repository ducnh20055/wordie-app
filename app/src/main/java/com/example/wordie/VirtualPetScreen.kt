package com.example.wordie

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordie.ui.theme.WordieTheme

data class PetItem(
    val id: String,
    val name: String,
    val emoji: String,
    val starsToUnlock: Int,
    val imageRes: Int
)

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

    val kidBg = if (isDarkMode) Color(0xFF1A1A1A) else Color(0xFFFFF8E1)
    val cardBg = if (isDarkMode) Color(0xFF2D2D2D) else Color(0xFFFFF1E0)
    val textColor = if (isDarkMode) Color.White else Color(0xFF333333)

    var currentStars by remember { mutableIntStateOf(if (stars <= 0) 12 else stars) }
    var petHappiness by remember { mutableIntStateOf(80) } // 0 - 100
    var petMessage by remember { mutableStateOf("Hi $playerName! Tớ đang rất vui, cho tớ ăn nhé! ❤️") }

    val petZooList = listOf(
        PetItem("fox", "Cáo Wordie", "🦊", 0, R.drawable.wordie_happy),
        PetItem("dog", "Cún Căng Cọt", "🐶", 3, R.drawable.wordie_excited),
        PetItem("cat", "Mèo Béo Tồn", "🐱", 6, R.drawable.wordie_surprised),
        PetItem("bear", "Gấu Tròn Xoay", "🐻", 10, R.drawable.wordie_sleeping)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(kidBg)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // TOP BAR
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .shadow(3.dp, CircleShape)
                    .background(cardBg, CircleShape)
                    .clickable { onBackClick() }
            ) {
                Text(
                    text = "<",
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = orangeDark
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "VƯỜN THÚ CỦA CON 🦁",
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = orangeMain
                )
                Text(
                    text = "NUÔI THÚ BẰNG NGÔI SAO",
                    fontFamily = playfulFont,
                    fontSize = 11.sp,
                    color = orangeDark
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .shadow(3.dp, RoundedCornerShape(20.dp))
                    .background(Color(0xFFFFF9C4), RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "⭐ $currentStars SAO",
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFFF57F17)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // KHU VỰC THÚ CƯNG DỄ THƯƠNG
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(28.dp))
                .background(cardBg, RoundedCornerShape(28.dp))
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // LỜI NÓI CỦA THÚ CƯNG
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(18.dp))
                        .background(Color.White, RoundedCornerShape(18.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = petMessage,
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFFE56E00),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // HÌNH THÚ CƯNG
                Image(
                    painter = painterResource(
                        id = if (petHappiness >= 80) R.drawable.wordie_excited else if (petHappiness >= 40) R.drawable.wordie_happy else R.drawable.wordie_sad
                    ),
                    contentDescription = "Pet Mascot",
                    modifier = Modifier.size(140.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // THANH ĐỘ NO / VUI VẺ
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "ĐỘ NO: ",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = orangeDark
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(12.dp)
                            .background(Color(0xFFE0E0E0), RoundedCornerShape(50.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(petHappiness / 100f)
                                .height(12.dp)
                                .background(Color(0xFF69F0AE), RoundedCornerShape(50.dp))
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$petHappiness%",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = orangeDark
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // NÚT CHO THÚ ĂN
                Button(
                    onClick = {
                        if (currentStars >= 1) {
                            currentStars -= 1
                            petHappiness = (petHappiness + 20).coerceAtMost(100)
                            petMessage = "NGON QUÁ! CẢM ƠN BẠN $playerName NHIỀU NGHEN! 🍎❤️"
                        } else {
                            petMessage = "HẾT SAO RỒI! HÃY HỌC THÊM BÀI HỌC ĐỂ LẤY ⭐ NHÉ! 📚"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = orangeMain,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .shadow(4.dp, RoundedCornerShape(50.dp))
                ) {
                    Text(
                        text = "🍎 CHO THÚ CƯNG ĂN (-1 ⭐)",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "BỘ SƯU TẬP THÚ CƯNG 🦁",
            fontFamily = playfulFont,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = orangeDark
        )

        Spacer(modifier = Modifier.height(12.dp))

        // LƯỚI BỘ SƯU TẬP THÚ CƯNG
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(petZooList) { pet ->
                val isUnlocked = currentStars >= pet.starsToUnlock

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .shadow(2.dp, RoundedCornerShape(20.dp))
                        .background(if (isUnlocked) cardBg else Color(0xFFE0E0E0), RoundedCornerShape(20.dp))
                        .padding(14.dp)
                ) {
                    Image(
                        painter = painterResource(id = pet.imageRes),
                        contentDescription = pet.name,
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${pet.emoji} ${pet.name}",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = if (isUnlocked) textColor else Color(0xFF757575)
                    )

                    Text(
                        text = if (isUnlocked) "Đã mở khóa ✓" else "Cần ${pet.starsToUnlock} ⭐",
                        fontFamily = playfulFont,
                        fontSize = 12.sp,
                        color = if (isUnlocked) Color(0xFF4CAF50) else Color(0xFFE53935)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun VirtualPetScreenPreview() {
    WordieTheme {
        VirtualPetScreen(
            playerName = "TOMMY",
            stars = 12,
            isDarkMode = false,
            onBackClick = {}
        )
    }
}
