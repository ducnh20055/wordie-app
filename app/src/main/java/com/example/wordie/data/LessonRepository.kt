package com.example.wordie.data

import android.util.Log
import com.example.wordie.models.Lesson
import com.example.wordie.models.WordItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LessonRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getLessons(): List<Lesson> {
        return try {
            val snapshot = db.collection("lessons").orderBy("order").get().await()
            if (snapshot.isEmpty()) return getSampleLessons()

            val lessons = mutableListOf<Lesson>()
            for (doc in snapshot.documents) {
                val lesson = doc.toObject(Lesson::class.java)?.copy(id = doc.id) ?: continue
                val wordsSnap = db.collection("lessons").document(doc.id).collection("words").get().await()
                val words = wordsSnap.documents.mapNotNull { it.toObject(WordItem::class.java)?.copy(id = it.id) }
                lessons.add(lesson.copy(words = words))
            }
            if (lessons.isEmpty()) getSampleLessons() else lessons
        } catch (e: Exception) {
            Log.e("LESSON", "Lỗi lấy lessons từ Firebase: ${e.message}")
            getSampleLessons()
        }
    }

    suspend fun uploadLessonsToFirebase(): Result<Int> {
        return try {
            val checkSnap = db.collection("lessons").limit(1).get().await()
            if (!checkSnap.isEmpty) {
                Log.d("LESSON", "Firebase đã có dữ liệu bài học, không cần upload lại.")
                return Result.success(0)
            }
            val lessons = getSampleLessons()
            for (lesson in lessons) {
                val lessonData = mapOf(
                    "title" to lesson.title,
                    "icon" to lesson.icon,
                    "info" to lesson.info,
                    "level" to lesson.level,
                    "order" to lesson.order,
                    "isPremium" to lesson.isPremium
                )
                db.collection("lessons").document(lesson.id).set(lessonData).await()
                for (word in lesson.words) {
                    val wordData = mapOf(
                        "id" to word.id,
                        "emoji" to word.emoji,
                        "english" to word.english,
                        "vietnamese" to word.vietnamese,
                        "example" to word.example
                    )
                    db.collection("lessons").document(lesson.id)
                        .collection("words").document(word.id)
                        .set(wordData).await()
                }
            }
            Log.d("LESSON", "UP XONG ${lessons.size} BÀI LÊN FIREBASE!")
            Result.success(lessons.size)
        } catch (e: Exception) {
            Log.e("LESSON", "Up lỗi: ${e.message}", e)
            Result.failure(e)
        }
    }
}
