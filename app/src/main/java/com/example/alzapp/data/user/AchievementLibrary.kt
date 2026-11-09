package com.example.alzapp.data.user

import com.example.alzapp.R
import com.example.alzapp.data.user.local.AchievementEntity

// achievements to collect; some of them only serve a decorative purpose
object AchievementLibrary {
    fun getInitialAchievements(): List<AchievementEntity> {
        return listOf(
            // streaks
            AchievementEntity(
                id = "streak_1",
                title = "Pierwszy Kiełek",
                description = "Za 1 dzień aktywności - rozpoczęcie przygody.",
                iconRes = R.drawable.icon_streak,
                category = AchievementCategory.STREAK
            ),
            AchievementEntity(
                id = "streak_7",
                title = "Tydzień podlewania",
                description = "Za aktywność 7 dni z rzędu.",
                iconRes = R.drawable.icon_streak,
                category = AchievementCategory.STREAK
            ),
            AchievementEntity(
                id = "streak_30",
                title = "Rozkwitający Ogród",
                description = "Za miesiąc aktywności.",
                iconRes = R.drawable.icon_streak,
                category = AchievementCategory.STREAK
            ),
            AchievementEntity(
                id = "streak_90",
                title = "Złoty Ogrodnik",
                description = "Za 3 miesiące aktywności.",
                iconRes = R.drawable.icon_streak,
                category = AchievementCategory.STREAK
            ),
            AchievementEntity(
                id = "streak_180",
                title = "Strażnik Tradycji",
                description = "Za 6 miesięcy aktywności.",
                iconRes = R.drawable.icon_streak,
                category = AchievementCategory.STREAK
            ),
            AchievementEntity(
                id = "streak_365",
                title = "Dąb Pamięci",
                description = "Za rok aktywności.",
                iconRes = R.drawable.icon_streak,
                category = AchievementCategory.STREAK
            ),

            // LOGIC: sudoku
            AchievementEntity(
                id = "sudoku_1",
                title = "Uważny Obserwator",
                description = "Za rozwiązanie pierwszego Sudoku.",
                iconRes = R.drawable.icon_achievement,
                category = AchievementCategory.SUDOKU
            ),
            AchievementEntity(
                id = "sudoku_10",
                title = "Gimnastyka Umysłu",
                description = "Za rozwiązanie 10 plansz.",
                iconRes = R.drawable.icon_achievement,
                category = AchievementCategory.SUDOKU
            ),
            // MEMORY: postman
            AchievementEntity(
                id = "postman_1",
                title = "Dobry Sąsiad",
                description = "Za bezbłędne dostarczenie pierwszego listu.",
                iconRes = R.drawable.icon_delivery_truck,
                category = AchievementCategory.POSTMAN
            ),
            AchievementEntity(
                id = "postman_perfect",
                title = "Zawsze na Czas",
                description = "Za ukończenie poziomu bez pomyłki w adresie.",
                iconRes = R.drawable.icon_delivery_truck,
                category = AchievementCategory.POSTMAN
            ),
            // LANGUAGE: proverbs (cryptogram)
            AchievementEntity(
                id = "proverbs_1",
                title = "Skarbnica Mądrości",
                description = "Za odgadnięcie pierwszego przysłowia.",
                iconRes = R.drawable.icon_achievement,
                category = AchievementCategory.PROVERBS
            ),
            AchievementEntity(
                id = "proverbs_3_streak",
                title = "Złote Usta",
                description = "Za odgadnięcie 3 przysłów pod rząd bez podpowiedzi.",
                iconRes = R.drawable.icon_achievement,
                category = AchievementCategory.PROVERBS
            ),
            // bonus achievements
            AchievementEntity(
                id = "bonus_morning",
                title = "Ranny Ptaszek",
                description = "Za rozwiązanie gry rano.",
                iconRes = R.drawable.icon_morning,
                category = AchievementCategory.BONUS
            ),
            AchievementEntity(
                id = "bonus_evening",
                title = "Wieczorny Spokój",
                description = "Za odhaczenie wszystkich leków i zadań przed końcem dnia",
                iconRes = R.drawable.icon_night,
                category = AchievementCategory.BONUS
            ),
            AchievementEntity(
                id = "bonus_meds",
                title = "Dzielny Pacjent",
                description = "Za wzięcie wszystkich leków w danym dniu.",
                iconRes = R.drawable.icon_medications,
                category = AchievementCategory.BONUS
            )
        )
    }
}