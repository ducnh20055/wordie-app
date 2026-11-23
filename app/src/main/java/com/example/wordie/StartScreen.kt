package com.example.wordie

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StartScreen(
    onContinueClick: () -> Unit
) {
    // Font vui nhộn giống logo
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))

    val orangeMain = Color(0xFFFF8A00)
    val orangeDark = Color(0xFFE56E00)
    val orangeShadow = Color(0xFFC85A00)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // LOGO Ở GIỮA
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Wordie Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(240.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // CHỮ WELCOME TO WORDIE - MÀU CAM - NỔI 3D
            Text(
                text = "WELCOME TO\nWORDIE",
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 42.sp,
                lineHeight = 48.sp,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                color = orangeMain,
                style = TextStyle(
                    shadow = Shadow(
                        color = orangeShadow,
                        offset = Offset(0f, 6f),
                        blurRadius = 0f
                    )
                )
            )

            // Thêm lớp shadow thứ 2 để nổi 3D hơn
            // Nếu muốn đậm hơn, tăng offset lên 8f

            Spacer(modifier = Modifier.weight(1f))

            // NÚT CONTINUE
            Button(
                onClick = onContinueClick,
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
}