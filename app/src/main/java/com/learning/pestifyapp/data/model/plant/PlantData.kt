package com.learning.pestifyapp.data.model.plant

import com.learning.pestifyapp.R

data class PlantData(
    val id: Long,
    val image: Int,
    val title: String,
    val description: String
)

object FakePlantData {
    val dummyPlants = listOf(
        PlantData(1, R.drawable.monstera, "Monstera", "a popular ornamental plant native to the tropical rainforests of Central and South America"),
        PlantData(2, R.drawable.aloe_vera, "Aloe Vera", " tanaman sukulen yang dikenal karena manfaat kesehatannya dan kemampuannya untuk tumbuh dalam berbagai kondisi."),
        PlantData(3, R.drawable.snake_plant, "Snake Plant", "have the ability to survive in a variety of environmental conditions"),
        PlantData(4, R.drawable.basil, "Basil", " tanaman herbal populer yang sering digunakan dalam masakan"),
        PlantData(5, R.drawable.bok_choy, "Bok Choy", "a type of Chinese cabbage that doesn't form heads"),
        PlantData(6, R.drawable.lettuce, "Lettuce", "a leafy vegetable that is often used in salads"),
        PlantData(7, R.drawable.spinach, "Spinach", "a leafy green vegetable that is generally cooked or eaten raw in salads"),
        PlantData(8, R.drawable.water_spinach, "Water Spinach", "a semiaquatic, tropical plant grown as a vegetable for its tender shoots")
    )
}