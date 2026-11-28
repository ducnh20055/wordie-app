package com.example.wordie.ui.grammar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordie.R

import com.example.wordie.models.GrammarChapter

@Composable
fun GrammarLessonScreen(
    chapter: GrammarChapter,
    onBackClick: () -> Unit,
    onFinishLesson: () -> Unit
) {
    val playfulFont = FontFamily(Font(R.font.fredoka_bold, FontWeight.Bold))
    val darkBg = Color.Black
    val cardBg = Color(0xFF1E1F24)

    val currentQuestion = chapter.questions.firstOrNull()
        ?: com.example.wordie.models.GrammarQuestion(
            id = "q1",
            promptVietnamese = "Tôi làm việc ở đây",
            structureHint = "Chủ ngữ + Động từ + ...",
            correctWords = listOf("I", "work", "here"),
            wordOptions = listOf("I", "here", "work", "she")
        )

    var selectedWords by remember { mutableStateOf(listOf("I", "here", "work")) }
    var isAnswerCorrect by remember { mutableStateOf<Boolean?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp)
    ) {
        // TOP PROGRESS BAR
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(Color(0xFF333333))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFF4CAF50))
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // TITLE
        Text(
            text = "Tạo một cụm từ",
            fontFamily = playfulFont,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        // STRUCTURE CARD
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(20.dp))
                .background(cardBg, RoundedCornerShape(20.dp))
                .border(1.dp, Color(0xFF3A3D4A), RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Sử dụng cấu trúc sau cho câu khẳng định:",
                    fontFamily = playfulFont,
                    fontSize = 13.sp,
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF2196F3))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = "Chủ ngữ", fontFamily = playfulFont, fontSize = 12.sp, color = Color.White)
                    }
                    Text(text = "  +  ", color = Color.White, fontWeight = FontWeight.Bold)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF4CAF50))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = "Động từ", fontFamily = playfulFont, fontSize = 12.sp, color = Color.White)
                    }
                    Text(text = "  +  ", color = Color.White, fontWeight = FontWeight.Bold)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF424242))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = "...", fontFamily = playfulFont, fontSize = 12.sp, color = Color.White)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // PROMPT
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "🗣️", fontSize = 28.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = currentQuestion.promptVietnamese,
                fontFamily = playfulFont,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // WORD CHIPS ARRANGEMENT (MATCHING IMAGE 2)
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            selectedWords.forEachIndexed { index, word ->
                val chipColor = when (index) {
                    0 -> Color(0xFF2196F3)
                    selectedWords.size - 1 -> Color(0xFF4CAF50)
                    else -> Color(0xFF424242)
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .size(width = 80.dp, height = 54.dp)
                        .shadow(4.dp, RoundedCornerShape(16.dp))
                        .background(chipColor, RoundedCornerShape(16.dp))
                        .clickable {
                            // Swap word order on tap
                            val mutable = selectedWords.toMutableList()
                            if (index < mutable.size - 1) {
                                val temp = mutable[index]
                                mutable[index] = mutable[index + 1]
                                mutable[index + 1] = temp
                                selectedWords = mutable
                            }
                        }
                ) {
                    Text(
                        text = word,
                        fontFamily = playfulFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // BOTTOM ACTION ROW: EXIT BUTTON + KIỂM TRA BUTTON
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // EXIT BUTTON ⎘
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(56.dp)
                    .shadow(4.dp, CircleShape)
                    .background(Color(0xFF2A2C34), CircleShape)
                    .clickable { onBackClick() }
            ) {
                Text(
                    text = "⎘",
                    fontSize = 24.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // CHECK BUTTON
            Button(
                onClick = {
                    val isCorrect = selectedWords == currentQuestion.correctWords || selectedWords == listOf("I", "work", "here")
                    isAnswerCorrect = isCorrect
                    onFinishLesson()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A2C34)),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) {
                Text(
                    text = "Kiểm Tra",
                    fontFamily = playfulFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.LightGray
                )
            }
        }
    }
}
