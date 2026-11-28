package com.example.wordie

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordie.ui.theme.WordieTheme
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(
    onLoadingFinished: () -> Unit
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))

    val orangeMain = Color(0xFFFF8A00)
    val orangeDark = Color(0xFFE56E00)
    val orangeLightBg = Color(0xFFFFF1E0)
    val orangeShadow = Color(0xFFC85A00)

    var progress by remember { mutableStateOf(0f) }
    var loadingText by remember { mutableStateOf("PREPARING YOUR PROGRESS...") }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = "progress"
    )

    LaunchedEffect(Unit) {
        val texts = listOf(
            "PREPARING YOUR PROGRESS...",
            "PICKING FUN WORDS...",
            "GETTING READY...",
            "LET'S GO!"
        )
        for (i in 1..100) {
            progress = i / 100f
            loadingText = when {
                i < 35 -> texts[0]
                i < 70 -> texts[1]
                i < 95 -> texts[2]
                else -> texts[3]
            }
            delay(25)
        }
        delay(300)
        onLoadingFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 24.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Wordie Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(180.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = loadingText,
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                letterSpacing = 0.5.sp,
                textAlign = TextAlign.Center,
                color = orangeMain,
                style = TextStyle(
                    shadow = Shadow(
                        color = orangeShadow,
                        offset = Offset(0f, 3f),
                        blurRadius = 0f
                    )
                )
            )

            Spacer(modifier = Modifier.height(28.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(orangeLightBg)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(22.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(orangeMain)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "${(animatedProgress * 100).toInt()}%",
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = orangeDark,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoadingScreenPreview() {
    WordieTheme {
        LoadingScreen(
            onLoadingFinished = {}
        )
    }
}