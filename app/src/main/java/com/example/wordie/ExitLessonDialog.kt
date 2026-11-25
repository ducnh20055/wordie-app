package com.example.wordie

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExitLessonDialog(
    isDarkMode: Boolean,
    onDismiss: () -> Unit,
    onConfirmExit: () -> Unit
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))
    val orangeMain = Color(0xFFFF8A00)
    val dialogBg = if (isDarkMode) Color(0xFF222222) else Color.White
    val textColor = if (isDarkMode) Color.White else Color(0xFF333333)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = dialogBg,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = "THOÁT BÀI HỌC?",
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = orangeMain,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = "Tiến trình bài học chưa hoàn thành sẽ bị mất. Bạn có chắc chắn muốn thoát không?",
                fontFamily = playfulFont,
                fontSize = 15.sp,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirmExit,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE53935),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(
                    text = "THOÁT",
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = orangeMain,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(
                    text = "TIẾP TỤC HỌC",
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}
