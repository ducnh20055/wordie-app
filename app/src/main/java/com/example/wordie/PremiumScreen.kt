package com.example.wordie

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordie.ui.theme.WordieTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumBottomSheet(
    isDarkMode: Boolean,
    onCloseClick: () -> Unit, // Bấm X / Back / Vuốt xuống -> FALSE
    onBuyClick: (packageName: String) -> Unit // Bấm Mua -> TRUE
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))
    val orangeMain = Color(0xFFFF8A00)
    val goldColor = Color(0xFFFFC107)

    val bgColor = if (isDarkMode) Color(0xFF1A1A1A) else Color.White
    val cardBg = if (isDarkMode) Color(0xFF2D2D2D) else Color(0xFFFFF1E0)
    val textColor = if (isDarkMode) Color.White else Color(0xFF333333)
    val subTextColor = if (isDarkMode) Color(0xFFBDBDBD) else Color(0xFF757575)

    var selectedPackage by remember { mutableStateOf("YEARLY") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = {
            // VUỐT XUỐNG HOẶC BẤM BACK -> FALSE
            onCloseClick()
        },
        sheetState = sheetState,
        containerColor = bgColor,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .size(width = 50.dp, height = 5.dp)
                    .background(Color(0xFFBDBDBD), RoundedCornerShape(50.dp))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
        ) {
            // TOP BAR - NÚT X
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(2.dp, CircleShape)
                        .background(cardBg, CircleShape)
                        .clickable { onCloseClick() }
                ) {
                    Text(
                        text = "✕",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = textColor
                    )
                }
            }

            // LOGO + TIÊU ĐỀ
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(80.dp)
                        .shadow(6.dp, CircleShape)
                        .background(goldColor, CircleShape)
                ) {
                    Text(text = "👑", fontSize = 40.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "WORDIE PRO",
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = orangeMain
                )
                Text(
                    text = "Học không giới hạn, không quảng cáo",
                    fontFamily = playfulFont,
                    fontSize = 14.sp,
                    color = subTextColor,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4 LỢI ÍCH - TRONG ẢNH CỦA M
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(cardBg, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Text(text = "✨ Mở khóa tất cả bài học", fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = textColor)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "🚫 Không quảng cáo", fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = textColor)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "🔊 Phát âm chuẩn AI + Luyện nói", fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = textColor)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "📥 Tải bài học học offline", fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = textColor)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3 GÓI
            PremiumPackageCard(title = "1 THÁNG", price = "49.000đ", subPrice = "49.000đ / tháng", isSelected = selectedPackage == "MONTHLY", isBest = false, isDarkMode = isDarkMode, onClick = { selectedPackage = "MONTHLY" })
            Spacer(modifier = Modifier.height(12.dp))
            PremiumPackageCard(title = "1 NĂM", price = "299.000đ", subPrice = "Chỉ 25.000đ / tháng - Tiết kiệm 49%", isSelected = selectedPackage == "YEARLY", isBest = true, isDarkMode = isDarkMode, onClick = { selectedPackage = "YEARLY" })
            Spacer(modifier = Modifier.height(12.dp))
            PremiumPackageCard(title = "TRỌN ĐỜI", price = "999.000đ", subPrice = "Trả 1 lần - Dùng mãi mãi", isSelected = selectedPackage == "LIFETIME", isBest = false, isDarkMode = isDarkMode, onClick = { selectedPackage = "LIFETIME" })

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onBuyClick(selectedPackage) },
                colors = ButtonDefaults.buttonColors(containerColor = orangeMain, contentColor = Color.White),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.fillMaxWidth().height(58.dp)
            ) {
                Text(text = "MUA NGAY - $selectedPackage", fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Hủy bất cứ lúc nào. Không bị trừ tiền nếu hủy trước 7 ngày.",
                fontFamily = playfulFont,
                fontSize = 12.sp,
                color = subTextColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PremiumPackageCard(
    title: String,
    price: String,
    subPrice: String,
    isSelected: Boolean,
    isBest: Boolean,
    isDarkMode: Boolean,
    onClick: () -> Unit
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))
    val orangeMain = Color(0xFFFF8A00)
    val cardBg = if (isDarkMode) Color(0xFF2D2D2D) else Color(0xFFFFF1E0)
    val textColor = if (isDarkMode) Color.White else Color(0xFF333333)
    val subTextColor = if (isDarkMode) Color(0xFFBDBDBD) else Color(0xFF757575)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelected) Color.White else cardBg, RoundedCornerShape(20.dp))
            .border(width = if (isSelected) 2.dp else 0.dp, color = if (isSelected) orangeMain else Color.Transparent, shape = RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = title, fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = if (isSelected) Color(0xFF333333) else textColor)
                    if (isBest) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(modifier = Modifier.background(orangeMain, RoundedCornerShape(50.dp)).padding(horizontal = 8.dp, vertical = 2.dp)) {
                            Text(text = "BEST", fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color.White)
                        }
                    }
                }
                Text(text = subPrice, fontFamily = playfulFont, fontSize = 12.sp, color = if (isSelected) Color(0xFF757575) else subTextColor)
            }
            Text(text = price, fontFamily = playfulFont, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = orangeMain)
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(22.dp)
                    .background(if (isSelected) orangeMain else Color.Transparent, CircleShape)
                    .border(width = 2.dp, color = if (isSelected) orangeMain else Color(0xFFBDBDBD), shape = CircleShape)
            ) {
                if (isSelected) Text(text = "✓", fontSize = 12.sp, color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PremiumBottomSheetPreview() {
    WordieTheme {
        PremiumBottomSheet(isDarkMode = false, onCloseClick = {}, onBuyClick = {})
    }
}