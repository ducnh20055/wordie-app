package com.example.wordie.data

import android.util.Log
import com.example.wordie.models.AppUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private fun getTodayAndYesterday(): Pair<String, String> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val cal = Calendar.getInstance()
        val today = sdf.format(cal.time)
        cal.add(Calendar.DAY_OF_YEAR, -1)
        val yesterday = sdf.format(cal.time)
        return Pair(today, yesterday)
    }

    private fun getEffectiveStreak(storedStreak: Int, lastActiveDate: String): Int {
        if (storedStreak <= 0 || lastActiveDate.isBlank()) return 0
        val (today, yesterday) = getTodayAndYesterday()
        return when (lastActiveDate) {
            today, yesterday -> storedStreak
            else -> 0 // Bị đứt chuỗi do bỏ lỡ quá 1 ngày
        }
    }

    suspend fun signUp(name: String, email: String, password: String): Result<AppUser> {
        return try {
            val cleanEmail = email.trim()
            val cleanPassword = password.trim()
            val cleanName = name.trim()

            if (cleanEmail.isBlank()) throw Exception("Vui lòng nhập Email!")
            if (cleanPassword.length < 6) throw Exception("Mật khẩu phải chứa ít nhất 6 ký tự!")
            if (cleanName.isBlank()) throw Exception("Vui lòng nhập Tên của bạn!")

            // 1. Tạo tài khoản trên Firebase Authentication
            val result = auth.createUserWithEmailAndPassword(cleanEmail, cleanPassword).await()
            val uid = result.user?.uid ?: throw Exception("Không lấy được UID từ Firebase")

            // 2. Tạo document thông tin user trên Firestore
            val userMap = mapOf(
                "uid" to uid,
                "name" to cleanName,
                "email" to cleanEmail,
                "age" to 0,
                "level" to "Beginner",
                "isPremium" to false,
                "xp" to 0,
                "streak" to 0,
                "lessonsDone" to 0,
                "lastActiveDate" to "",
                "createdAt" to System.currentTimeMillis()
            )

            db.collection("users").document(uid).set(userMap, SetOptions.merge()).await()

            val user = AppUser(
                uid = uid,
                name = cleanName,
                email = cleanEmail,
                age = 0,
                level = "Beginner",
                isPremium = false,
                xp = 0,
                streak = 0,
                lessonsDone = 0,
                lastActiveDate = ""
            )
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<AppUser> {
        return try {
            val cleanEmail = email.trim()
            val cleanPassword = password.trim()

            if (cleanEmail.isBlank() || cleanPassword.isBlank()) {
                throw Exception("Vui lòng nhập đầy đủ Email và Mật khẩu!")
            }

            // 1. Kiểm tra Firebase Authentication
            val result = auth.signInWithEmailAndPassword(cleanEmail, cleanPassword).await()
            val uid = result.user?.uid ?: throw Exception("Không lấy được UID")

            // 2. Kiểm tra dữ liệu trên Firestore
            val docRef = db.collection("users").document(uid)
            val snap = docRef.get().await()

            val user: AppUser
            if (snap.exists()) {
                val rawUser = snap.toObject(AppUser::class.java)?.copy(uid = uid)
                    ?: AppUser(
                        uid = uid,
                        email = cleanEmail,
                        name = snap.getString("name") ?: (result.user?.displayName ?: cleanEmail.substringBefore("@")),
                        level = snap.getString("level") ?: "Beginner",
                        isPremium = snap.getBoolean("isPremium") ?: false,
                        xp = (snap.getLong("xp") ?: 0L).toInt(),
                        streak = (snap.getLong("streak") ?: 0L).toInt(),
                        lessonsDone = (snap.getLong("lessonsDone") ?: 0L).toInt(),
                        lastActiveDate = snap.getString("lastActiveDate") ?: ""
                    )
                val effectiveStreak = getEffectiveStreak(rawUser.streak, rawUser.lastActiveDate)
                user = rawUser.copy(streak = effectiveStreak)
            } else {
                val defaultName = result.user?.displayName ?: cleanEmail.substringBefore("@")
                val userMap = mapOf(
                    "uid" to uid,
                    "name" to defaultName,
                    "email" to cleanEmail,
                    "age" to 0,
                    "level" to "Beginner",
                    "isPremium" to false,
                    "xp" to 0,
                    "streak" to 0,
                    "lessonsDone" to 0,
                    "lastActiveDate" to "",
                    "createdAt" to System.currentTimeMillis()
                )
                docRef.set(userMap, SetOptions.merge()).await()
                user = AppUser(uid = uid, name = defaultName, email = cleanEmail)
            }

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): AppUser? {
        val firebaseUser = auth.currentUser ?: return null
        val uid = firebaseUser.uid
        return try {
            val docRef = db.collection("users").document(uid)
            val snap = docRef.get().await()

            if (snap.exists()) {
                val parsed = try { snap.toObject(AppUser::class.java) } catch (e: Exception) { null }
                val rawUser = parsed?.copy(uid = uid) ?: AppUser(
                    uid = uid,
                    name = snap.getString("name") ?: (firebaseUser.displayName ?: firebaseUser.email?.substringBefore("@") ?: "User"),
                    email = snap.getString("email") ?: (firebaseUser.email ?: ""),
                    age = (snap.getLong("age") ?: 0L).toInt(),
                    level = snap.getString("level") ?: "Beginner",
                    isPremium = snap.getBoolean("isPremium") ?: false,
                    xp = (snap.getLong("xp") ?: 0L).toInt(),
                    streak = (snap.getLong("streak") ?: 0L).toInt(),
                    lessonsDone = (snap.getLong("lessonsDone") ?: 0L).toInt(),
                    lastActiveDate = snap.getString("lastActiveDate") ?: "",
                    createdAt = snap.getLong("createdAt") ?: System.currentTimeMillis()
                )
                val effectiveStreak = getEffectiveStreak(rawUser.streak, rawUser.lastActiveDate)
                rawUser.copy(streak = effectiveStreak)
            } else {
                val defaultName = firebaseUser.displayName ?: firebaseUser.email?.substringBefore("@") ?: "User"
                val defaultEmail = firebaseUser.email ?: ""
                val userMap = mapOf(
                    "uid" to uid,
                    "name" to defaultName,
                    "email" to defaultEmail,
                    "age" to 0,
                    "level" to "Beginner",
                    "isPremium" to false,
                    "xp" to 0,
                    "streak" to 0,
                    "lessonsDone" to 0,
                    "lastActiveDate" to "",
                    "createdAt" to System.currentTimeMillis()
                )
                docRef.set(userMap, SetOptions.merge()).await()
                AppUser(uid = uid, name = defaultName, email = defaultEmail)
            }
        } catch (e: Exception) {
            Log.e("AUTH", "Lỗi getCurrentUser từ Firestore: ${e.message}", e)
            AppUser(
                uid = uid,
                email = firebaseUser.email ?: "",
                name = firebaseUser.displayName ?: firebaseUser.email?.substringBefore("@") ?: "User"
            )
        }
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun updateUserInfo(uid: String, name: String, age: Int) {
        try {
            val updates = mutableMapOf<String, Any>()
            if (name.isNotBlank()) updates["name"] = name.trim()
            if (age > 0) updates["age"] = age
            if (updates.isNotEmpty()) {
                db.collection("users").document(uid).set(updates, SetOptions.merge()).await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updatePremium(uid: String, isPremium: Boolean) {
        try {
            db.collection("users").document(uid).set(mapOf("isPremium" to isPremium), SetOptions.merge()).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updateLevel(uid: String, level: String) {
        try {
            db.collection("users").document(uid).set(mapOf("level" to level), SetOptions.merge()).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updateProgress(uid: String, lessonsDone: Int, xp: Int, streak: Int) {
        try {
            db.collection("users").document(uid).set(
                mapOf(
                    "lessonsDone" to lessonsDone,
                    "xp" to xp,
                    "streak" to streak
                ),
                SetOptions.merge()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Hàm được gọi khi học xong 1 bài - Tự tính streak chuẩn theo ngày
    suspend fun completeLesson(uid: String, currentUser: AppUser, xpEarned: Int = 20): AppUser {
        return try {
            val (today, yesterday) = getTodayAndYesterday()
            val effectiveStreak = getEffectiveStreak(currentUser.streak, currentUser.lastActiveDate)

            val newStreak = when (currentUser.lastActiveDate) {
                today -> if (effectiveStreak > 0) effectiveStreak else 1 // Học thêm bài trong hôm nay -> giữ nguyên streak
                yesterday -> effectiveStreak + 1 // Học tiếp vào ngày hôm sau -> +1 streak
                else -> 1 // Chưa từng học hoặc bị đứt chuỗi -> bắt đầu chuỗi 1 ngày
            }

            val newXp = currentUser.xp + xpEarned
            val newLessonsDone = currentUser.lessonsDone + 1

            val updateData = mapOf(
                "xp" to newXp,
                "lessonsDone" to newLessonsDone,
                "streak" to newStreak,
                "lastActiveDate" to today
            )

            db.collection("users").document(uid).set(updateData, SetOptions.merge()).await()

            currentUser.copy(
                xp = newXp,
                lessonsDone = newLessonsDone,
                streak = newStreak,
                lastActiveDate = today
            )
        } catch (e: Exception) {
            e.printStackTrace()
            currentUser
        }
    }
}
