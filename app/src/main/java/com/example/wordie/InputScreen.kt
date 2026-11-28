package com.example.wordie

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordie.ui.theme.WordieTheme

@Composable
fun InputScreen(
    onContinueClick: (name: String, age: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))

    val orangeMain = Color(0xFFFF8A00)
    val orangeDark = Color(0xFFE56E00)
    val orangeLightBg = Color(0xFFFFF1E0)
    val orangeShadow = Color(0xFFC85A00)
    val errorColor = Color(0xFFFF3333)

    var name by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf<String?>(null) }

    var ageText by remember { mutableStateOf("10") }
    var ageError by remember { mutableStateOf<String?>(null) }

    fun validateAge(input: String): String? {
        val parsed = input.toIntOrNull()
        return when {
            input.isBlank() -> "Vui lòng nhập số tuổi!"
            parsed == null -> "Số tuổi không hợp lệ!"
            parsed < 6 -> "Tuổi tối thiểu là 6 tuổi!"
            parsed > 100 -> "Tuổi tối đa là 100 tuổi!"
            else -> null
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            // TIÊU ĐỀ
            Text(
                text = "LET'S GET\nSTARTED!",
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 38.sp,
                lineHeight = 44.sp,
                textAlign = TextAlign.Center,
                color = orangeMain,
                style = TextStyle(
                    shadow = Shadow(
                        color = orangeShadow,
                        offset = Offset(0f, 5f),
                        blurRadius = 0f
                    )
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // LABEL - YOUR NAME
            Text(
                text = "YOUR NAME",
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                letterSpacing = 1.sp,
                color = if (nameError != null) errorColor else orangeDark,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Ô INPUT TÊN
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    if (it.trim().isNotEmpty()) {
                        nameError = null
                    }
                },
                placeholder = {
                    Text(
                        text = "ENTER YOUR NAME",
                        fontFamily = playfulFont,
                        fontSize = 16.sp,
                        letterSpacing = 1.sp,
                        color = Color(0xFFBDBDBD)
                    )
                },
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF333333)
                ),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (nameError != null) errorColor else orangeMain,
                    unfocusedBorderColor = if (nameError != null) errorColor else orangeMain,
                    focusedContainerColor = if (nameError != null) Color(0xFFFFEBEE) else orangeLightBg,
                    unfocusedContainerColor = if (nameError != null) Color(0xFFFFEBEE) else orangeLightBg,
                    cursorColor = orangeMain
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .shadow(4.dp, RoundedCornerShape(24.dp))
            )

            if (nameError != null) {
                Text(
                    text = "⚠️ ${nameError!!}",
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = errorColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // LABEL - YOUR AGE
            Text(
                text = "YOUR AGE (6 - 100)",
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                letterSpacing = 1.sp,
                color = if (ageError != null) errorColor else orangeDark,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // TUỔI TYPE HOẶC BẤM NÚT TĂNG GIẢM
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Ô NHẬP TUỔI TYPE
                OutlinedTextField(
                    value = ageText,
                    onValueChange = { input ->
                        if (input.length <= 3 && input.all { it.isDigit() }) {
                            ageText = input
                            ageError = validateAge(input)
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 44.sp,
                        color = if (ageError != null) errorColor else orangeMain,
                        textAlign = TextAlign.Center,
                        shadow = Shadow(
                            color = if (ageError != null) Color.Red.copy(alpha = 0.3f) else orangeShadow,
                            offset = Offset(0f, 3f),
                            blurRadius = 0f
                        )
                    ),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (ageError != null) errorColor else orangeMain,
                        unfocusedBorderColor = if (ageError != null) errorColor else orangeMain,
                        focusedContainerColor = if (ageError != null) Color(0xFFFFEBEE) else orangeLightBg,
                        unfocusedContainerColor = if (ageError != null) Color(0xFFFFEBEE) else orangeLightBg,
                        cursorColor = orangeMain
                    ),
                    modifier = Modifier
                        .width(140.dp)
                        .height(100.dp)
                        .shadow(4.dp, RoundedCornerShape(24.dp))
                )

                Spacer(modifier = Modifier.width(20.dp))

                // 2 NÚT MŨI TÊN LÊN / XUỐNG
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // MŨI TÊN LÊN - TĂNG TUỔI
                    IconButton(
                        onClick = {
                            val curr = ageText.toIntOrNull() ?: 10
                            if (curr < 100) {
                                val next = curr + 1
                                ageText = next.toString()
                                ageError = validateAge(next.toString())
                            }
                        },
                        modifier = Modifier
                            .size(46.dp)
                            .shadow(4.dp, CircleShape)
                            .background(orangeMain, CircleShape)
                    ) {
                        Text(
                            text = "▲",
                            fontSize = 18.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // MŨI TÊN XUỐNG - GIẢM TUỔI
                    IconButton(
                        onClick = {
                            val curr = ageText.toIntOrNull() ?: 10
                            if (curr > 1) {
                                val next = curr - 1
                                ageText = next.toString()
                                ageError = validateAge(next.toString())
                            }
                        },
                        modifier = Modifier
                            .size(46.dp)
                            .shadow(4.dp, CircleShape)
                            .background(orangeMain, CircleShape)
                    ) {
                        Text(
                            text = "▼",
                            fontSize = 18.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // HIỂN THỊ THÔNG BÁO LỖI ĐỎ NẾU NHẬP SAI TUỔI
            if (ageError != null) {
                Text(
                    text = "⚠️ ${ageError!!}",
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = errorColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp)
                )
            } else {
                Text(
                    text = "Type age or tap ▲ ▼ to change",
                    fontFamily = playfulFont,
                    fontSize = 13.sp,
                    color = Color(0xFF9E9E9E),
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // NÚT CONTINUE
            Button(
                onClick = {
                    val cleanName = name.trim()
                    val nameErr = if (cleanName.isEmpty()) "Vui lòng nhập tên của bạn!" else null
                    val ageErr = validateAge(ageText)

                    nameError = nameErr
                    ageError = ageErr

                    if (nameErr == null && ageErr == null) {
                        val validAge = ageText.toInt()
                        onContinueClick(cleanName, validAge)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (nameError != null || ageError != null) Color(0xFFBDBDBD) else orangeMain,
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun InputScreenPreview() {
    WordieTheme {
        InputScreen(
            onContinueClick = { _, _ -> }
        )
    }
}
