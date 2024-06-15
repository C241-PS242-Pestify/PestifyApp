package com.learning.pestifyapp.data.model.homeart

import com.learning.pestifyapp.R

data class Article(
    val id: Int,
    val title: String,
    val category: String,
    val date: String,
    val image: Int
)

object FakeArtData {
    val dummyArticles = listOf(
        Article(1, "Putri: Aquaponics Pioneer Changing Farming", "Story", "May 14, 2024", R.drawable.article),
        Article(2, "Boost Your Harvest with These Expert Tricks!", "Tips", "May 15, 2024", R.drawable.article),
        Article(3, "Putri: Aquaponics Pioneer Changing Farming", "Story", "May 14, 2024", R.drawable.article),
        Article(4, "Cultivating Wellness Through Sustainable Living", "Lifestyle", "May 15, 2024", R.drawable.article),
        Article(5, "Putri: Aquaponics Pioneer Changing Farming", "Story", "May 14, 2024", R.drawable.article),
        Article(6, "Putri: Aquaponics Pioneer Changing Farming", "Story", "May 14, 2024", R.drawable.article),
        Article(7, "Boost Your Harvest with These Expert Tricks!", "Tips", "May 15, 2024", R.drawable.article),
        Article(8, "Putri: Aquaponics Pioneer Changing Farming", "Story", "May 14, 2024", R.drawable.article),
        Article(9, "Boost Your Harvest with These Expert Tricks!", "Tips", "May 15, 2024", R.drawable.article),
        Article(10, "Boost Your Harvest with These Expert Tricks!", "Tips", "May 15, 2024", R.drawable.article),
        Article(11, "Boost Your Harvest with These Expert Tricks!", "Tips", "May 15, 2024", R.drawable.article),
        Article(12, "Cultivating Wellness Through Sustainable Living", "Lifestyle", "May 15, 2024", R.drawable.article),
        Article(13, "Cultivating Wellness Through Sustainable Living", "Lifestyle", "May 15, 2024", R.drawable.article),
        Article(14, "Cultivating Wellness Through Sustainable Living", "Lifestyle", "May 15, 2024", R.drawable.article),
        Article(15, "Cultivating Wellness Through Sustainable Living", "Lifestyle", "May 15, 2024", R.drawable.article),
    )

    val category = listOf(
        "All", "Story", "Tips", "Lifestyle"
    )
}