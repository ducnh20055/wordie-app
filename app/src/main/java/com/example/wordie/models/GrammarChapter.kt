package com.example.wordie.models

data class GrammarQuestion(
    val id: String,
    val promptVietnamese: String,
    val structureHint: String,
    val correctWords: List<String>,
    val wordOptions: List<String>
)

data class GrammarChapter(
    val id: Int,
    val title: String,
    val subtitle: String,
    val currentLevel: Int = 1,
    val streakDays: Int = 0,
    val progressPercent: Int = 0,
    val starsEarned: Int = 0,
    val totalStars: Int = 3,
    val questions: List<GrammarQuestion> = emptyList()
)

fun getSampleGrammarChapters(): List<GrammarChapter> {
    return listOf(
        GrammarChapter(
            id = 1,
            title = "Thì hiện tại: câu khẳng định",
            subtitle = "Sử dụng cấu trúc: [Chủ ngữ] + [Động từ] + [...]",
            currentLevel = 1,
            streakDays = 0,
            progressPercent = 0,
            starsEarned = 0,
            totalStars = 3,
            questions = listOf(
                GrammarQuestion(
                    id = "q1",
                    promptVietnamese = "Tôi làm việc ở đây",
                    structureHint = "Chủ ngữ + Động từ + ...",
                    correctWords = listOf("I", "work", "here"),
                    wordOptions = listOf("I", "here", "work")
                ),
                GrammarQuestion(
                    id = "q2",
                    promptVietnamese = "Họ sống ở thành phố này",
                    structureHint = "Chủ ngữ + Động từ + ...",
                    correctWords = listOf("They", "live", "in", "this", "city"),
                    wordOptions = listOf("They", "live", "in", "this", "city")
                )
            )
        ),
        GrammarChapter(
            id = 2,
            title = "Thì hiện tại: câu phủ định",
            subtitle = "Sử dụng cấu trúc: [Chủ ngữ] + don't/doesn't + [Động từ]",
            currentLevel = 2,
            streakDays = 0,
            progressPercent = 0,
            starsEarned = 0,
            totalStars = 3,
            questions = listOf(
                GrammarQuestion(
                    id = "q3",
                    promptVietnamese = "Tôi không thích cà phê",
                    structureHint = "Chủ ngữ + don't + Động từ",
                    correctWords = listOf("I", "don't", "like", "coffee"),
                    wordOptions = listOf("I", "don't", "like", "coffee")
                ),
                GrammarQuestion(
                    id = "q4",
                    promptVietnamese = "Anh ấy không xem tivi",
                    structureHint = "Chủ ngữ + doesn't + Động từ",
                    correctWords = listOf("He", "doesn't", "watch", "TV"),
                    wordOptions = listOf("He", "doesn't", "watch", "TV")
                )
            )
        ),
        GrammarChapter(
            id = 3,
            title = "Thì quá khứ đơn",
            subtitle = "Sử dụng cấu trúc: [Chủ ngữ] + [Động từ V2/ed]",
            currentLevel = 3,
            streakDays = 0,
            progressPercent = 0,
            starsEarned = 0,
            totalStars = 3,
            questions = listOf(
                GrammarQuestion(
                    id = "q5",
                    promptVietnamese = "Hôm qua tôi đã đi học",
                    structureHint = "Chủ ngữ + Động từ V2 + ...",
                    correctWords = listOf("I", "went", "to", "school", "yesterday"),
                    wordOptions = listOf("I", "went", "to", "school", "yesterday")
                )
            )
        ),
        GrammarChapter(
            id = 4,
            title = "Thì tương lai đơn",
            subtitle = "Sử dụng cấu trúc: [Chủ ngữ] + will + [Động từ]",
            currentLevel = 4,
            streakDays = 0,
            progressPercent = 0,
            starsEarned = 0,
            totalStars = 3,
            questions = listOf(
                GrammarQuestion(
                    id = "q6",
                    promptVietnamese = "Tôi sẽ giúp bạn ngày mai",
                    structureHint = "Chủ ngữ + will + Động từ",
                    correctWords = listOf("I", "will", "help", "you", "tomorrow"),
                    wordOptions = listOf("I", "will", "help", "you", "tomorrow")
                )
            )
        )
    )
}
