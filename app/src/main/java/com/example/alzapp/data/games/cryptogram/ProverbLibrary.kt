package com.example.alzapp.data.games.cryptogram

import com.example.alzapp.data.games.cryptogram.local.ProverbEntity

// all proverbs available in Cryptogram game
object ProverbLibrary {
    fun getInitialProverbs(): List<ProverbEntity> {
        return listOf(
            ProverbEntity(content = "Nie chwal dnia przed zachodem słońca", category = "Mądrości"),
            ProverbEntity(content = "Gdzie kucharek sześć tam nie ma co jeść", category = "Kuchnia"),
            ProverbEntity(content = "Bez pracy nie ma kołaczy", category = "Praca"),
            ProverbEntity(content = "Kto rano wstaje temu Pan Bóg daje", category = "Dzień"),
            ProverbEntity(content = "Darowanemu koniowi w zęby się nie zagląda", category = "Zwierzęta"),
            ProverbEntity(content = "Lepszy wróbel w garści niż gołąb na dachu", category = "Zwierzęta"),
            ProverbEntity(content = "Apetyt rośnie w miarę jedzenia", category = "Jedzenie"),
            ProverbEntity(content = "Co nagle to po diable", category = "Mądrości")
        )
    }
}