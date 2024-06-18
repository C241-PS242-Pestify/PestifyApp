package com.learning.pestifyapp.data.model.ensdata

import com.learning.pestifyapp.R

data class Ensiklopedia(
    val id: String,
    val title: String,
    val description: String,
    val image: Int
)

object EnsiklopediaData {
    val ensiklopediaList = listOf(
        Ensiklopedia(
            "1",
            "Pengenalan Hama",
            "Hama adalah organisme yang merugikan tanaman. Hama dapat berupa serangga, tikus, burung, dan lain-lain.",
            R.drawable.hamaa
        ),
        Ensiklopedia(
            "2",
            "Anak Serangga",
            "Hama adalah organisme yang merugikan tanaman. Hama dapat berupa serangga, tikus, burung, dan lain-lain.",
            R.drawable.hamaa
        ),
        Ensiklopedia(
            "3",
            "Bapak Serangga",
            "Hama adalah organisme yang merugikan tanaman. Hama dapat berupa serangga, tikus, burung, dan lain-lain.",
            R.drawable.hamaa
        ),
        Ensiklopedia(
            "4",
            "Cumi Goreng",
            "Hama adalah organisme yang merugikan tanaman. Hama dapat berupa serangga, tikus, burung, dan lain-lain.",
            R.drawable.hamaa
        ),
        Ensiklopedia(
            "5",
            "Deck Serangga",
            "Hama adalah organisme yang merugikan tanaman. Hama dapat berupa serangga, tikus, burung, dan lain-lain.",
            R.drawable.hamaa
        ),
        Ensiklopedia(
            "6",
            "Egg Serangga",
            "Hama adalah organisme yang merugikan tanaman. Hama dapat berupa serangga, tikus, burung, dan lain-lain.",
            R.drawable.hamaa
        ),
        Ensiklopedia(
            "7",
            "Fusoo humi",
            "Hama adalah organisme yang merugikan tanaman. Hama dapat berupa serangga, tikus, burung, dan lain-lain.",
            R.drawable.hamaa
        ),
        Ensiklopedia(
            "8",
            "goreng ayam",
            "Hama adalah organisme yang merugikan tanaman. Hama dapat berupa serangga, tikus, burung, dan lain-lain.",
            R.drawable.hamaa
        ),
        Ensiklopedia(
            "9",
            "hantu sen",
            "Hama adalah organisme yang merugikan tanaman. Hama dapat berupa serangga, tikus, burung, dan lain-lain.",
            R.drawable.hamaa
        ),
    )
}