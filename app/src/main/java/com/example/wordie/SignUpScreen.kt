package com.example.wordie

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordie.ui.theme.WordieTheme

@Composable
fun SignUpScreen(
    onSignUpClick: (name: String, email: String, password: String) -> Unit,
    onSignInClick: () -> Unit
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))

    val orangeMain = Color(0xFFFF8A00)
    val orangeDark = Color(0xFFE56E00)
    val orangeLightBg = Color(0xFFFFF1E0)
    val orangeShadow = Color(0xFFC85A00)

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        Text(
            text = "CREATE\nACCOUNT",
            fontFamily = playfulFont,
            fontWeight = FontWeight.Bold,
            fontSize = 42.sp,
            lineHeight = 46.sp,
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

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Join Wordie and start learning!",
            fontFamily = playfulFont,
            fontSize = 15.sp,
            color = Color(0xFF9E9E9E)
        )

        Spacer(modifier = Modifier.height(36.dp))

        // NAME
        Text(
            text = "NAME",
            fontFamily = playfulFont,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            letterSpacing = 1.sp,
            color = orangeDark,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = {
                Text(
                    text = "Enter your name",
                    fontFamily = playfulFont,
                    fontSize = 15.sp,
                    color = Color(0xFFBDBDBD)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = orangeMain,
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedContainerColor = orangeLightBg,
                unfocusedContainerColor = orangeLightBg
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // EMAIL
        Text(
            text = "EMAIL",
            fontFamily = playfulFont,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            letterSpacing = 1.sp,
            color = orangeDark,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = {
                Text(
                    text = "Enter your email",
                    fontFamily = playfulFont,
                    fontSize = 15.sp,
                    color = Color(0xFFBDBDBD)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = orangeMain,
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedContainerColor = orangeLightBg,
                unfocusedContainerColor = orangeLightBg
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // PASSWORD
        Text(
            text = "PASSWORD",
            fontFamily = playfulFont,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            letterSpacing = 1.sp,
            color = orangeDark,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = {
                Text(
                    text = "Create a password",
                    fontFamily = playfulFont,
                    fontSize = 15.sp,
                    color = Color(0xFFBDBDBD)
                )
            },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = orangeMain,
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedContainerColor = orangeLightBg,
                unfocusedContainerColor = orangeLightBg
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = { onSignUpClick(name, email, password) },
            colors = ButtonDefaults.buttonColors(
                containerColor = orangeMain,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .shadow(6.dp, RoundedCornerShape(50.dp))
        ) {
            Text(
                text = "SIGN UP",
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                letterSpacing = 1.5.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Already have an account? SIGN IN",
            fontFamily = playfulFont,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color(0xFF757575),
            modifier = Modifier.clickable { onSignInClick() }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SignUpScreenPreview() {
    WordieTheme {
        SignUpScreen(
            onSignUpClick = { _, _, _ -> },
            onSignInClick = {}
        )
    }
}