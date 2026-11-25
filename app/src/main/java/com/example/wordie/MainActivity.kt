package com.example.wordie

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.wordie.data.AuthRepository
import com.example.wordie.data.LessonRepository
import com.example.wordie.models.AppUser
import com.example.wordie.models.Lesson
import com.example.wordie.ui.theme.WordieTheme
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : ComponentActivity() {
    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) tts?.language = Locale.US
        }

        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            var isPremium by remember { mutableStateOf(false) }
            var showPremiumSheet by remember { mutableStateOf(false) }
            var showLessonModeSheet by remember { mutableStateOf(false) }
            var currentScreen by remember { mutableStateOf("checking") }
            var playerLevel by remember { mutableStateOf("Beginner") }
            var playerName by remember { mutableStateOf("TOMMY") }
            var selectedLesson by remember { mutableStateOf<Lesson?>(null) }
            var currentUser by remember { mutableStateOf<AppUser?>(null) }
            var lessons by remember { mutableStateOf<List<Lesson>>(emptyList()) }

            val authRepo = remember { AuthRepository() }
            val lessonRepo = remember { LessonRepository() }
            val scope = rememberCoroutineScope()

            LaunchedEffect(Unit) {
                val result = lessonRepo.uploadLessonsToFirebase()
                result.onSuccess { count ->
                    Toast.makeText(this@MainActivity, "Đã upload $count bài học lên Firebase!", Toast.LENGTH_SHORT).show()
                }.onFailure { e ->
                    android.util.Log.e("WORDIE", "Upload Firebase thất bại: ${e.message}", e)
                }
                lessons = lessonRepo.getLessons()
                val savedUser = authRepo.getCurrentUser()
                if (savedUser != null) {
                    currentUser = savedUser
                    playerName = savedUser.name.ifEmpty { "TOMMY" }
                    playerLevel = savedUser.level
                    isPremium = savedUser.isPremium
                    currentScreen = "home"
                } else {
                    currentScreen = "start"
                }
            }

            val onTabClick: (String) -> Unit = { tab ->
                when (tab) {
                    "home" -> currentScreen = "home"
                    "play" -> {
                        selectedLesson = lessons.firstOrNull()
                        showLessonModeSheet = true
                    }
                    "profile" -> currentScreen = "profile"
                }
            }

            WordieTheme(darkTheme = isDarkMode) {
                when (currentScreen) {
                    "checking" -> LoadingScreen(onLoadingFinished = {})
                    "start" -> StartScreen(onContinueClick = { currentScreen = "signin" })

                    "signin" -> SignInScreen(
                        onSignInClick = { email, password ->
                            scope.launch {
                                val result = authRepo.signIn(email, password)
                                result.onSuccess { user ->
                                    currentUser = user
                                    playerLevel = user.level
                                    isPremium = user.isPremium
                                    playerName = user.name.ifEmpty { email.substringBefore("@") }
                                    currentScreen = "home"
                                    if (!user.isPremium) showPremiumSheet = true
                                    Toast.makeText(this@MainActivity, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                                }.onFailure { e ->
                                    android.util.Log.e("WORDIE", "SignIn lỗi: ${e.message}", e)
                                    val errorMsg = parseAuthError(e)
                                    Toast.makeText(this@MainActivity, errorMsg, Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        onSignUpClick = { currentScreen = "signup" }
                    )

                    "signup" -> SignUpScreen(
                        onSignUpClick = { name, email, password ->
                            scope.launch {
                                val result = authRepo.signUp(name, email, password)
                                result.onSuccess { user ->
                                    currentUser = user
                                    playerName = name.ifEmpty { "TOMMY" }
                                    currentScreen = "input"
                                    Toast.makeText(this@MainActivity, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                                }.onFailure { e ->
                                    android.util.Log.e("WORDIE", "SignUp lỗi: ${e.message}", e)
                                    val errorMsg = parseAuthError(e)
                                    Toast.makeText(this@MainActivity, errorMsg, Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        onSignInClick = { currentScreen = "signin" }
                    )

                    "input" -> InputScreen(
                        onContinueClick = { name, age ->
                            if (name.isNotEmpty()) playerName = name
                            scope.launch {
                                currentUser?.uid?.let { uid ->
                                    authRepo.updateUserInfo(uid, name, age)
                                }
                            }
                            currentScreen = "level"
                        }
                    )

                    "level" -> LevelScreen(
                        onContinueClick = { level ->
                            playerLevel = level
                            scope.launch {
                                currentUser?.uid?.let { authRepo.updateLevel(it, level) }
                            }
                            currentScreen = "loading"
                        }
                    )

                    "loading" -> LoadingScreen(
                        onLoadingFinished = {
                            currentScreen = "home"
                            if (!isPremium) showPremiumSheet = true
                        }
                    )

                    "home" -> HomeScreen(
                        playerName = playerName,
                        playerLevel = playerLevel,
                        isDarkMode = isDarkMode,
                        isPremium = isPremium,
                        lessons = lessons,
                        lessonsDone = currentUser?.lessonsDone ?: 0,
                        stars = currentUser?.stars ?: 0,
                        onLessonClick = { lesson ->
                            selectedLesson = lesson
                            showLessonModeSheet = true
                        },
                        onPremiumClick = { showPremiumSheet = true },
                        onTabClick = onTabClick,
                        onThemeToggle = { isDarkMode = !isDarkMode },
                        onLogoutClick = {
                            authRepo.signOut()
                            currentScreen = "signin"
                        }
                    )

                    // 1. THẺ TỪ (FLASHCARD)
                    "flashcard" -> {
                        val lesson = selectedLesson
                        if (lesson != null) {
                            FlashCardScreen(
                                lesson = lesson,
                                isDarkMode = isDarkMode,
                                isPremium = isPremium,
                                onBackClick = { currentScreen = "home" },
                                onSpeakClick = { word ->
                                    tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
                                },
                                onPremiumClick = { showPremiumSheet = true },
                                onFinishClick = {
                                    scope.launch {
                                        val updated = authRepo.completeLesson(
                                            uid = currentUser?.uid ?: return@launch,
                                            currentUser = currentUser!!,
                                            starsEarned = 1
                                        )
                                        currentUser = updated
                                    }
                                    currentScreen = "home"
                                }
                            )
                        }
                    }

                    // 2. QUIZ TRẮC NGHIỆM
                    "quiz" -> {
                        val lesson = selectedLesson
                        if (lesson != null) {
                            QuizScreen(
                                lesson = lesson,
                                isDarkMode = isDarkMode,
                                isPremium = isPremium,
                                onBackClick = { currentScreen = "home" },
                                onSpeakClick = { word ->
                                    tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
                                },
                                onFinishClick = {
                                    scope.launch {
                                        val updated = authRepo.completeLesson(
                                            uid = currentUser?.uid ?: return@launch,
                                            currentUser = currentUser!!,
                                            starsEarned = 1
                                        )
                                        currentUser = updated
                                    }
                                    currentScreen = "home"
                                }
                            )
                        }
                    }

                    // 3. NGHE CHỌN TỪ
                    "listening" -> {
                        val lesson = selectedLesson
                        if (lesson != null) {
                            ListeningQuizScreen(
                                lesson = lesson,
                                isDarkMode = isDarkMode,
                                onBackClick = { currentScreen = "home" },
                                onSpeakClick = { word ->
                                    tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
                                },
                                onFinishClick = {
                                    scope.launch {
                                        val updated = authRepo.completeLesson(
                                            uid = currentUser?.uid ?: return@launch,
                                            currentUser = currentUser!!,
                                            starsEarned = 1
                                        )
                                        currentUser = updated
                                    }
                                    currentScreen = "home"
                                }
                            )
                        }
                    }

                    // 4. ĐIỀN TỪ CHÍNH TẢ
                    "fill_in_blank" -> {
                        val lesson = selectedLesson
                        if (lesson != null) {
                            FillInTheBlankScreen(
                                lesson = lesson,
                                isDarkMode = isDarkMode,
                                onBackClick = { currentScreen = "home" },
                                onSpeakClick = { word ->
                                    tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
                                },
                                onFinishClick = {
                                    scope.launch {
                                        val updated = authRepo.completeLesson(
                                            uid = currentUser?.uid ?: return@launch,
                                            currentUser = currentUser!!,
                                            starsEarned = 1
                                        )
                                        currentUser = updated
                                    }
                                    currentScreen = "home"
                                }
                            )
                        }
                    }

                    // 5. GHÉP TỪ / GHÉP THẺ
                    "word_matching" -> {
                        val lesson = selectedLesson
                        if (lesson != null) {
                            WordMatchingScreen(
                                lesson = lesson,
                                isDarkMode = isDarkMode,
                                onBackClick = { currentScreen = "home" },
                                onFinishClick = {
                                    scope.launch {
                                        val updated = authRepo.completeLesson(
                                            uid = currentUser?.uid ?: return@launch,
                                            currentUser = currentUser!!,
                                            starsEarned = 1
                                        )
                                        currentUser = updated
                                    }
                                    currentScreen = "home"
                                }
                            )
                        }
                    }

                    // 6. BẮT BÓNG TỪ VỰNG
                    "balloon_pop" -> {
                        val lesson = selectedLesson
                        if (lesson != null) {
                            BalloonPopScreen(
                                lesson = lesson,
                                isDarkMode = isDarkMode,
                                onBackClick = { currentScreen = "home" },
                                onSpeakClick = { word ->
                                    tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
                                },
                                onFinishClick = {
                                    scope.launch {
                                        val updated = authRepo.completeLesson(
                                            uid = currentUser?.uid ?: return@launch,
                                            currentUser = currentUser!!,
                                            starsEarned = 1
                                        )
                                        currentUser = updated
                                    }
                                    currentScreen = "home"
                                }
                            )
                        }
                    }

                    // 7. VƯỜN THÚ CỦA CON / NUÔI THÚ ÁO
                    "virtual_pet" -> {
                        VirtualPetScreen(
                            playerName = playerName,
                            stars = currentUser?.stars ?: 0,
                            isDarkMode = isDarkMode,
                            onBackClick = { currentScreen = "home" }
                        )
                    }

                    "profile" -> ProfileScreen(
                        playerName = playerName,
                        playerLevel = playerLevel,
                        isDarkMode = isDarkMode,
                        onBackClick = { currentScreen = "home" },
                        onTabClick = onTabClick,
                        onThemeToggle = { isDarkMode = !isDarkMode },
                        onLogoutClick = {
                            authRepo.signOut()
                            currentScreen = "signin"
                        },
                        streak = currentUser?.streak ?: 0,
                        lessonsDone = currentUser?.lessonsDone ?: 0,
                        stars = currentUser?.stars ?: 0,
                        xp = currentUser?.xp ?: 0
                    )
                }

                if (showLessonModeSheet && selectedLesson != null) {
                    LessonModeBottomSheet(
                        lesson = selectedLesson!!,
                        isDarkMode = isDarkMode,
                        onDismiss = { showLessonModeSheet = false },
                        onModeSelect = { mode ->
                            showLessonModeSheet = false
                            currentScreen = mode
                        }
                    )
                }

                if (showPremiumSheet) {
                    PremiumBottomSheet(
                        isDarkMode = isDarkMode,
                        onCloseClick = {
                            isPremium = false
                            showPremiumSheet = false
                        },
                        onBuyClick = { _ ->
                            isPremium = true
                            showPremiumSheet = false
                            scope.launch {
                                currentUser?.uid?.let { authRepo.updatePremium(it, true) }
                            }
                        }
                    )
                }
            }
        }
    }

    private fun parseAuthError(e: Throwable): String {
        val msg = e.message ?: ""
        return when {
            msg.contains("credential", ignoreCase = true) ||
            msg.contains("password", ignoreCase = true) ||
            msg.contains("user-not-found", ignoreCase = true) ||
            msg.contains("invalid-credential", ignoreCase = true) ->
                "Email hoặc Mật khẩu không chính xác!"
            msg.contains("email-already-in-use", ignoreCase = true) ->
                "Email này đã được đăng ký tài khoản!"
            msg.contains("weak-password", ignoreCase = true) ->
                "Mật khẩu quá yếu! Cần ít nhất 6 ký tự."
            msg.contains("invalid-email", ignoreCase = true) ->
                "Định dạng Email không hợp lệ!"
            msg.contains("network", ignoreCase = true) ->
                "Lỗi kết nối mạng! Vui lòng kiểm tra lại Wifi/Internet."
            msg.contains("too-many-requests", ignoreCase = true) ->
                "Bạn đã thử quá nhiều lần. Vui lòng đợi ít phút rồi thử lại!"
            msg.isNotBlank() -> msg
            else -> "Đăng nhập/Đăng ký thất bại. Vui lòng thử lại!"
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}
