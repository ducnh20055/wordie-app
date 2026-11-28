package com.example.wordie.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.wordie.R

@Composable
fun ExitLessonDialog(
    isDarkMode: Boolean,
    onDismiss: () -> Unit,
    onConfirmExit: () -> Unit
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))

    val orangeMain = Color(0xFFFF8A00)
    val bgColor = if (isDarkMode) Color(0xFF2D2D2D) else Color.White
    val textColor = if (isDarkMode) Color.White else Color(0xFF333333)
    val subTextColor = if (isDarkMode) Color(0xFFBDBDBD) else Color(0xFF757575)

    Dialog(onDismissRequest = onDismiss) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(28.dp))
                .background(bgColor, RoundedCornerShape(28.dp))
                .padding(24.dp)
        ) {
            Text(
                text = "THOÁT BÀI HỌC?",
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = orangeMain,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Bạn có chắc muốn thoát? Tiến trình bài học này sẽ không được lưu.",
                fontFamily = playfulFont,
                fontSize = 14.sp,
                color = subTextColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = textColor),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text(
                        text = "HỌC TIẾP",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = onConfirmExit,
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF3333),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text(
                        text = "THOÁT",
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
