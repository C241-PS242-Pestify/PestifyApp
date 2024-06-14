package com.learning.pestifyapp.data.model.homeart

data class Article(
    val id: Int,
    val title: String,
    val category: String,
    val date: String,
    val content: String
)

object FakeArtData {
    val dummyArticles = listOf(
        Article(1, "Putri: Aquaponics Pioneer Changing Farming", "Story", "May 14, 2024", "Content 1"),
        Article(2, "Boost Your Harvest with These Expert Tricks!", "Tips", "May 15, 2024", "Content 2"),
        Article(3, "Cultivating Wellness Through Sustainable Living", "Lifestyle", "May 15, 2024", "Content 3")
    )
}