package com.example.wordie

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.wordie.data.AuthRepository
import com.example.wordie.data.LessonRepository
import com.example.wordie.data.getSampleLessons
import com.example.wordie.models.AppUser
import com.example.wordie.models.GrammarChapter
import com.example.wordie.models.Lesson
import com.example.wordie.models.getSampleGrammarChapters
import com.example.wordie.ui.grammar.GrammarLessonScreen
import com.example.wordie.ui.grammar.GrammarResultScreen
import com.example.wordie.ui.grammar.GrammarScreen
import com.example.wordie.ui.statistics.StatisticsScreen
import com.example.wordie.ui.theme.WordieTheme
import com.example.wordie.utils.getDeviceDefaultLanguage
import com.example.wordie.utils.isNetworkAvailable
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
            var showLanguageSheet by remember { mutableStateOf(false) }
            var currentScreen by remember { mutableStateOf("checking") }
            var playerLevel by remember { mutableStateOf("Beginner") }
            var playerName by remember { mutableStateOf("TOMMY") }
            var nativeLanguage by remember { mutableStateOf(getDeviceDefaultLanguage()) }
            var selectedLesson by remember { mutableStateOf<Lesson?>(null) }
            var selectedGrammarChapter by remember { mutableStateOf<GrammarChapter?>(getSampleGrammarChapters().first()) }
            var currentUser by remember { mutableStateOf<AppUser?>(null) }
            var lessons by remember { mutableStateOf<List<Lesson>>(getSampleLessons()) }

            val authRepo = remember { AuthRepository() }
            val lessonRepo = remember { LessonRepository() }
            val scope = rememberCoroutineScope()

            LaunchedEffect(Unit) {
                val savedUser = authRepo.getCurrentUser()
                if (savedUser != null) {
                    currentUser = savedUser
                    playerName = savedUser.name.ifEmpty { "TOMMY" }
                    playerLevel = savedUser.level
                    isPremium = savedUser.isPremium
                    if (savedUser.nativeLanguage.isNotBlank()) {
                        nativeLanguage = savedUser.nativeLanguage
                    }
                }

                if (isNetworkAvailable(this@MainActivity)) {
                    val result = lessonRepo.uploadLessonsToFirebase()
                    result.onSuccess { count ->
                        if (count > 0) android.util.Log.d("WORDIE", "Đã đồng bộ $count bài học lên Firebase!")
                    }.onFailure { e ->
                        android.util.Log.e("WORDIE", "Đồng bộ Firebase thất bại: ${e.message}", e)
                    }
                    val remoteLessons = lessonRepo.getLessons()
                    if (remoteLessons.isNotEmpty()) {
                        lessons = remoteLessons
                    }
                }
            }

            val handleCompleteLesson: () -> Unit = {
                if (isNetworkAvailable(this@MainActivity)) {
                    val user = currentUser
                    if (user != null) {
                        scope.launch {
                            val updated = authRepo.completeLesson(
                                uid = user.uid,
                                currentUser = user,
                                starsEarned = 1
                            )
                            currentUser = updated
                            Toast.makeText(this@MainActivity, "Đã lưu tiến trình học thành công! (+1 ⭐)", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Chưa đăng nhập! Tiến trình chưa được lưu.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Không có kết nối mạng! Cần có Internet để lưu tiến trình bài học.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                currentScreen = "grammar"
            }

            val onTabClick: (String) -> Unit = { tab ->
                when (tab) {
                    "grammar", "home" -> currentScreen = "grammar"
                    "vocabulary" -> currentScreen = "vocabulary"
                    "statistics" -> currentScreen = "statistics"
                    "profile" -> currentScreen = "profile"
                }
            }

            WordieTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = if (isDarkMode) Color(0xFF1A1A1A) else Color.White
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        val activeLesson = selectedLesson ?: lessons.firstOrNull()

                        when (currentScreen) {
                            "checking" -> LoadingScreen(
                                onLoadingFinished = {
                                    currentScreen = if (currentUser != null || authRepo.isLoggedIn()) "grammar" else "start"
                                }
                            )
                            "start" -> StartScreen(onContinueClick = { currentScreen = "input" })

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
                                    currentScreen = "native_language"
                                }
                            )

                            "native_language" -> NativeLanguageScreen(
                                onContinueClick = { lang ->
                                    nativeLanguage = lang
                                    if (currentUser != null) {
                                        currentUser = currentUser?.copy(nativeLanguage = lang)
                                    }
                                    scope.launch {
                                        currentUser?.uid?.let { uid ->
                                            authRepo.updateOnboardingInfo(uid, nativeLanguage = lang)
                                        }
                                    }
                                    currentScreen = "reason"
                                }
                            )

                            "reason" -> ReasonScreen(
                                onContinueClick = { reason ->
                                    scope.launch {
                                        currentUser?.uid?.let { uid ->
                                            authRepo.updateOnboardingInfo(uid, learningReason = reason)
                                        }
                                    }
                                    currentScreen = "target_time"
                                }
                            )

                            "target_time" -> TargetTimeScreen(
                                onContinueClick = { target ->
                                    scope.launch {
                                        currentUser?.uid?.let { uid ->
                                            authRepo.updateOnboardingInfo(uid, targetTime = target)
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
                                    currentScreen = "loading_plan"
                                }
                            )

                            "loading_plan" -> LoadingScreen(
                                onLoadingFinished = {
                                    currentScreen = "premium_offer"
                                }
                            )

                            "premium_offer" -> PremiumScreen(
                                isDarkMode = isDarkMode,
                                onCloseClick = {
                                    currentScreen = "grammar"
                                },
                                onBuyClick = { _ ->
                                    isPremium = true
                                    currentScreen = "grammar"
                                    scope.launch {
                                        currentUser?.uid?.let { authRepo.updatePremium(it, true) }
                                    }
                                }
                            )

                            // TAB 1: GRAMMAR (HOME) - MATCHING IMAGE 3
                            "grammar" -> GrammarScreen(
                                streakDays = currentUser?.streak ?: 1,
                                currentLevel = 1,
                                isDarkMode = isDarkMode,
                                selectedTab = "grammar",
                                onTabClick = onTabClick,
                                onStartLesson = { chapter ->
                                    selectedGrammarChapter = chapter
                                    currentScreen = "grammar_lesson"
                                }
                            )

                            // GRAMMAR LESSON SCREEN - MATCHING IMAGE 2
                            "grammar_lesson" -> GrammarLessonScreen(
                                chapter = selectedGrammarChapter ?: getSampleGrammarChapters().first(),
                                onBackClick = { currentScreen = "grammar" },
                                onFinishLesson = { currentScreen = "grammar_result" }
                            )

                            // GRAMMAR RESULT SCREEN - MATCHING IMAGE 1
                            "grammar_result" -> GrammarResultScreen(
                                onContinueClick = {
                                    handleCompleteLesson()
                                    currentScreen = "grammar"
                                },
                                onCloseClick = { currentScreen = "grammar" }
                            )

                            // TAB 2: VOCABULARY (CURRENT HOMESCREEN)
                            "vocabulary", "home" -> HomeScreen(
                                playerName = playerName,
                                playerLevel = playerLevel,
                                isDarkMode = isDarkMode,
                                isPremium = isPremium,
                                lessons = lessons,
                                lessonsDone = currentUser?.lessonsDone ?: 0,
                                stars = currentUser?.stars ?: 0,
                                nativeLanguage = nativeLanguage,
                                onLessonClick = { lesson ->
                                    selectedLesson = lesson
                                    showLessonModeSheet = true
                                },
                                onPremiumClick = { showPremiumSheet = true },
                                onTabClick = onTabClick,
                                onThemeToggle = { isDarkMode = !isDarkMode },
                                onLanguageClick = { showLanguageSheet = true },
                                onLogoutClick = {
                                    authRepo.signOut()
                                    currentScreen = "signin"
                                }
                            )

                            // TAB 3: STATISTICS
                            "statistics" -> StatisticsScreen(
                                streakDays = currentUser?.streak ?: 1,
                                totalWords = 120,
                                accuracyPercent = 92,
                                studyTimeMins = 45,
                                isDarkMode = isDarkMode,
                                selectedTab = "statistics",
                                onTabClick = onTabClick
                            )

                            // 1. THẺ TỪ (FLASHCARD)
                            "flashcard" -> {
                                if (activeLesson != null) {
                                    FlashCardScreen(
                                        lesson = activeLesson,
                                        isDarkMode = isDarkMode,
                                        isPremium = isPremium,
                                        onBackClick = { currentScreen = "home" },
                                        onSpeakClick = { word ->
                                            tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
                                        },
                                        onPremiumClick = { showPremiumSheet = true },
                                        onFinishClick = handleCompleteLesson
                                    )
                                } else {
                                    currentScreen = "home"
                                }
                            }

                            // 2. QUIZ TRẮC NGHIỆM
                            "quiz" -> {
                                if (activeLesson != null) {
                                    QuizScreen(
                                        lesson = activeLesson,
                                        isDarkMode = isDarkMode,
                                        isPremium = isPremium,
                                        onBackClick = { currentScreen = "home" },
                                        onSpeakClick = { word ->
                                            tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
                                        },
                                        onFinishClick = handleCompleteLesson
                                    )
                                } else {
                                    currentScreen = "home"
                                }
                            }

                            // 3. NGHE CHỌN TỪ
                            "listening" -> {
                                if (activeLesson != null) {
                                    ListeningQuizScreen(
                                        lesson = activeLesson,
                                        isDarkMode = isDarkMode,
                                        onBackClick = { currentScreen = "home" },
                                        onSpeakClick = { word ->
                                            tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
                                        },
                                        onFinishClick = handleCompleteLesson
                                    )
                                } else {
                                    currentScreen = "home"
                                }
                            }

                            // 4. ĐIỀN TỪ CHÍNH TẢ
                            "fill_in_blank" -> {
                                if (activeLesson != null) {
                                    FillInTheBlankScreen(
                                        lesson = activeLesson,
                                        isDarkMode = isDarkMode,
                                        onBackClick = { currentScreen = "home" },
                                        onSpeakClick = { word ->
                                            tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
                                        },
                                        onFinishClick = handleCompleteLesson
                                    )
                                } else {
                                    currentScreen = "home"
                                }
                            }

                            // 5. GHÉP TỪ / GHÉP THẺ
                            "word_matching" -> {
                                if (activeLesson != null) {
                                    WordMatchingScreen(
                                        lesson = activeLesson,
                                        isDarkMode = isDarkMode,
                                        onBackClick = { currentScreen = "home" },
                                        onFinishClick = handleCompleteLesson
                                    )
                                } else {
                                    currentScreen = "home"
                                }
                            }

                            // 6. BẮT BÓNG TỪ VỰNG
                            "balloon_pop" -> {
                                if (activeLesson != null) {
                                    BalloonPopScreen(
                                        lesson = activeLesson,
                                        isDarkMode = isDarkMode,
                                        onBackClick = { currentScreen = "home" },
                                        onSpeakClick = { word ->
                                            tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
                                        },
                                        onFinishClick = handleCompleteLesson
                                    )
                                } else {
                                    currentScreen = "home"
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
                                    currentUser = null
                                    currentScreen = "signin"
                                },
                                onSignInClick = { currentScreen = "signin" },
                                onSignUpClick = { currentScreen = "signup" },
                                onLanguageClick = { showLanguageSheet = true },
                                isLoggedIn = (currentUser != null || authRepo.isLoggedIn()),
                                streak = currentUser?.streak ?: 0,
                                lessonsDone = currentUser?.lessonsDone ?: 0,
                                stars = currentUser?.stars ?: 0,
                                xp = currentUser?.xp ?: 0
                            )

                            else -> HomeScreen(
                                playerName = playerName,
                                playerLevel = playerLevel,
                                isDarkMode = isDarkMode,
                                isPremium = isPremium,
                                lessons = lessons,
                                lessonsDone = currentUser?.lessonsDone ?: 0,
                                stars = currentUser?.stars ?: 0,
                                nativeLanguage = nativeLanguage,
                                onLessonClick = { lesson ->
                                    selectedLesson = lesson
                                    showLessonModeSheet = true
                                },
                                onPremiumClick = { showPremiumSheet = true },
                                onTabClick = onTabClick,
                                onThemeToggle = { isDarkMode = !isDarkMode },
                                onLanguageClick = { showLanguageSheet = true },
                                onLogoutClick = {
                                    authRepo.signOut()
                                    currentScreen = "signin"
                                }
                            )
                        }

                        if (showLanguageSheet) {
                            LanguageSelectionBottomSheet(
                                currentLanguage = nativeLanguage,
                                isDarkMode = isDarkMode,
                                onDismiss = { showLanguageSheet = false },
                                onLanguageSelect = { lang ->
                                    nativeLanguage = lang
                                    showLanguageSheet = false
                                    if (currentUser != null) {
                                        currentUser = currentUser?.copy(nativeLanguage = lang)
                                    }
                                    scope.launch {
                                        currentUser?.uid?.let { uid ->
                                            authRepo.updateOnboardingInfo(uid, nativeLanguage = lang)
                                        }
                                    }
                                }
                            )
                        }

                        if (showLessonModeSheet && activeLesson != null) {
                            LessonModeBottomSheet(
                                lesson = activeLesson,
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
