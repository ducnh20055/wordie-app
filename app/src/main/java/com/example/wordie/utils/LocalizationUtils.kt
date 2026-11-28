package com.example.wordie.utils

import java.util.Locale

fun getDeviceDefaultLanguage(): String {
    val locale = Locale.getDefault()
    val lang = locale.language.lowercase()
    val country = locale.country.uppercase()
    return when {
        lang.startsWith("vi") || country == "VN" -> "Vietnamese"
        lang.startsWith("es") -> "Spanish"
        lang.startsWith("fr") -> "French"
        lang.startsWith("de") -> "German"
        lang.startsWith("ja") || country == "JP" -> "Japanese"
        lang.startsWith("ko") || country == "KR" -> "Korean"
        lang.startsWith("zh") || country == "CN" || country == "TW" -> "Chinese"
        else -> "English"
    }
}

object LocalizedStrings {
    fun get(key: String, language: String): String {
        return when (language) {
            "Vietnamese" -> when (key) {
                "continue" -> "TIẾP TỤC  ->"
                "lessons" -> "BÀI HỌC"
                "profile" -> "HỒ SƠ"
                "level" -> "TRÌNH ĐỘ"
                "pro_unlocked" -> "👑 BẠN ĐANG LÀ PRO - ĐÃ MỞ KHÓA TẤT CẢ"
                "pro_upgrade" -> "👑 NÂNG CẤP LÊN PRO - MỞ KHÓA TẤT CẢ"
                "saved_progress" -> "Đã lưu tiến trình học thành công!"
                "need_network" -> "Không có kết nối mạng! Cần có Internet để lưu tiến trình."
                "times_up" -> "⏰ HẾT GIỜ! ⏰"
                "play_again" -> "CHƠI LẠI 🔄"
                "finish_lesson" -> "HOÀN THÀNH BÀI HỌC 🏆"
                "welcome" -> "CHÀO MỪNG ĐẾN VỚI WORDIE"
                "choose_level" -> "CHỌN TRÌNH ĐỘ CỦA BẠN!"
                "why_learning" -> "TẠI SAO BẠN HỌC TỪ VỰNG?"
                "when_progress" -> "KHI NÀO BẠN MUỐN THẤY TIẾN BỘ?"
                "native_lang_title" -> "NGÔN NGỮ MẸ ĐẺ CỦA BẠN"
                "sign_in" -> "Đăng nhập"
                "sign_up" -> "Đăng ký tài khoản"
                "log_out" -> "Đăng xuất"
                else -> key
            }
            "Spanish" -> when (key) {
                "continue" -> "CONTINUAR  ->"
                "lessons" -> "LECCIONES"
                "profile" -> "PERFIL"
                "level" -> "NIVEL"
                "pro_unlocked" -> "👑 ERES PRO - TODO DESBLOQUEADO"
                "pro_upgrade" -> "👑 ACTUALIZAR A PRO - DESBLOQUEAR TODO"
                "saved_progress" -> "¡Progreso guardado con éxito!"
                "need_network" -> "¡Sin red! Necesitas Internet para guardar el progreso."
                "times_up" -> "⏰ ¡TIEMPO TERMINADO! ⏰"
                "play_again" -> "JUGAR DE NUEVO 🔄"
                "finish_lesson" -> "TERMINAR LECCIÓN 🏆"
                "welcome" -> "BIENVENIDO A WORDIE"
                "choose_level" -> "¡ELIGE TU NIVEL!"
                "why_learning" -> "¿POR QUÉ APRENDES WORDIE?"
                "when_progress" -> "¿CUÁNDO QUIERES VER PROGRESOS?"
                "native_lang_title" -> "TU IDIOMA MATERNO"
                "sign_in" -> "Iniciar sesión"
                "sign_up" -> "Crear una cuenta"
                "log_out" -> "Cerrar sesión"
                else -> key
            }
            "French" -> when (key) {
                "continue" -> "CONTINUER  ->"
                "lessons" -> "LEÇONS"
                "profile" -> "PROFIL"
                "level" -> "NIVEAU"
                "pro_unlocked" -> "👑 VOUS ÊTES PRO - TOUT DÉBLOQUÉ"
                "pro_upgrade" -> "👑 PASSER À PRO - TOUT DÉBLOQUER"
                "saved_progress" -> "Progrès enregistré avec succès!"
                "need_network" -> "Pas de réseau! Connexion Internet requise."
                "times_up" -> "⏰ TEMPS ÉCOULÉ! ⏰"
                "play_again" -> "REJOUER 🔄"
                "finish_lesson" -> "TERMINER LA LEÇON 🏆"
                "welcome" -> "BIENVENUE SUR WORDIE"
                "choose_level" -> "CHOISISSEZ VOTRE NIVEAU!"
                "why_learning" -> "POURQUOI APPRENEZ-VOUS WORDIE?"
                "when_progress" -> "QUAND VOULEZ-VOUS DES PROGRÈS?"
                "native_lang_title" -> "VOTRE LANGUE MATERNELLE"
                "sign_in" -> "Se connecter"
                "sign_up" -> "Créer un compte"
                "log_out" -> "Se déconnecter"
                else -> key
            }
            "German" -> when (key) {
                "continue" -> "WEITER  ->"
                "lessons" -> "LEKTIONEN"
                "profile" -> "PROFIL"
                "level" -> "LEVEL"
                "pro_unlocked" -> "👑 DU BIST PRO - ALLES FREIGESCHALTET"
                "pro_upgrade" -> "👑 AUF PRO UPGRADEN - ALLES FREISCHALTEN"
                "saved_progress" -> "Fortschritt erfolgreich gespeichert!"
                "need_network" -> "Kein Netzwerk! Internet zum Speichern erforderlich."
                "times_up" -> "⏰ ZEIT ABGELAUFEN! ⏰"
                "play_again" -> "NOCHMAL SPIELEN 🔄"
                "finish_lesson" -> "LEKTION BEENDEN 🏆"
                "welcome" -> "WILLKOMMEN BEI WORDIE"
                "choose_level" -> "WÄHLE DEIN LEVEL!"
                "why_learning" -> "WARUM LERNST DU WORDIE?"
                "when_progress" -> "WANN MÖCHTEST DU FORTSCHRITTE SEHEN?"
                "native_lang_title" -> "DEINE MUTTERSPRACHE"
                "sign_in" -> "Anmelden"
                "sign_up" -> "Konto erstellen"
                "log_out" -> "Abmelden"
                else -> key
            }
            "Japanese" -> when (key) {
                "continue" -> "次へ  ->"
                "lessons" -> "レッスン"
                "profile" -> "プロフィール"
                "level" -> "レベル"
                "pro_unlocked" -> "👑 PRO会員 - すべて解放済み"
                "pro_upgrade" -> "👑 PROにアップグレード - すべて解放"
                "saved_progress" -> "進捗が保存されました！"
                "need_network" -> "ネットワークエラー！保存にはインターネットが必要です。"
                "times_up" -> "⏰ タイムアップ！ ⏰"
                "play_again" -> "もう一度プレイ 🔄"
                "finish_lesson" -> "レッスン完了 🏆"
                "welcome" -> "WORDIEへようこそ"
                "choose_level" -> "レベルを選んでください！"
                "why_learning" -> "WORDIEを学ぶ理由は？"
                "when_progress" -> "いつ成果を実感したいですか？"
                "native_lang_title" -> "あなたの母国語"
                "sign_in" -> "サインイン"
                "sign_up" -> "アカウント作成"
                "log_out" -> "サインアウト"
                else -> key
            }
            "Korean" -> when (key) {
                "continue" -> "계속하기  ->"
                "lessons" -> "레슨"
                "profile" -> "프로필"
                "level" -> "레벨"
                "pro_unlocked" -> "👑 PRO 회원 - 모든 레슨 잠금해제됨"
                "pro_upgrade" -> "👑 PRO 업그레이드 - 모든 레슨 잠금해제"
                "saved_progress" -> "학습 진도가 저장되었습니다!"
                "need_network" -> "네트워크 없음! 저장하려면 인터넷이 필요합니다."
                "times_up" -> "⏰ 시간 종료! ⏰"
                "play_again" -> "다시 하기 🔄"
                "finish_lesson" -> "레슨 완료 🏆"
                "welcome" -> "WORDIE에 오신 것을 환영합니다"
                "choose_level" -> "레벨을 선택하세요!"
                "why_learning" -> "WORDIE를 배우는 이유는?"
                "when_progress" -> "언제 성과를 보고 싶으신가요?"
                "native_lang_title" -> "당신의 모국어"
                "sign_in" -> "로그인"
                "sign_up" -> "회원가입"
                "log_out" -> "로그아웃"
                else -> key
            }
            "Chinese" -> when (key) {
                "continue" -> "继续  ->"
                "lessons" -> "课程"
                "profile" -> "个人资料"
                "level" -> "等级"
                "pro_unlocked" -> "👑 您是 PRO 会员 - 已解锁全部课程"
                "pro_upgrade" -> "👑 升级 PRO - 解锁全部课程"
                "saved_progress" -> "学习进度已成功保存！"
                "need_network" -> "无网络连接！需要网络以保存进度。"
                "times_up" -> "⏰ 时间到！ ⏰"
                "play_again" -> "再玩一次 🔄"
                "finish_lesson" -> "完成课程 🏆"
                "welcome" -> "欢迎来到 WORDIE"
                "choose_level" -> "选择您的等级！"
                "why_learning" -> "您为什么要学习 WORDIE？"
                "when_progress" -> "您希望什么时候看到进步？"
                "native_lang_title" -> "您的母语"
                "sign_in" -> "登录"
                "sign_up" -> "注册账号"
                "log_out" -> "退出登录"
                else -> key
            }
            else -> when (key) {
                "continue" -> "CONTINUE  ->"
                "lessons" -> "LESSONS"
                "profile" -> "PROFILE"
                "level" -> "LEVEL"
                "pro_unlocked" -> "👑 YOU ARE PRO - UNLOCKED ALL"
                "pro_upgrade" -> "👑 UPGRADE TO PRO - UNLOCK ALL"
                "saved_progress" -> "Progress saved successfully!"
                "need_network" -> "No network connection! Internet required to save progress."
                "times_up" -> "⏰ TIME'S UP! ⏰"
                "play_again" -> "PLAY AGAIN 🔄"
                "finish_lesson" -> "FINISH LESSON 🏆"
                "welcome" -> "WELCOME TO WORDIE"
                "choose_level" -> "CHOOSE YOUR LEVEL!"
                "why_learning" -> "WHY ARE YOU LEARNING?"
                "when_progress" -> "WHEN DO YOU WANT TO SEE PROGRESS?"
                "native_lang_title" -> "YOUR NATIVE LANGUAGE"
                "sign_in" -> "Sign In"
                "sign_up" -> "Create Account"
                "log_out" -> "Log Out"
                else -> key
            }
        }
    }
}
