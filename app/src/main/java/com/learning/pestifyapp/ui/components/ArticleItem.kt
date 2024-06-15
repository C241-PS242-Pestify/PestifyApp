package com.learning.pestifyapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.learning.pestifyapp.data.model.homeart.Article
import com.learning.pestifyapp.data.model.homeart.FakeArtData
import com.learning.pestifyapp.ui.screen.dashboard.home.HomeViewModel
import com.learning.pestifyapp.ui.theme.iconLight

@Composable
fun ArticleItem(article: Article) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .background(Color.White)
            .clickable(
                onClick = {},
                interactionSource = remember { MutableInteractionSource() },
                indication = null

            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(article.image),
            contentDescription = null,
            modifier = Modifier
                .size(95.dp)
                .background(MaterialTheme.colorScheme.surface)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(25.dp))
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(
                text = article.category,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Text(
                text = article.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = article.date,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ArticleCategory(
    articleList: List<Article>,
    viewModel: HomeViewModel,
    navigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedCategory = viewModel.selectedCategory.collectAsStateWithLifecycle(lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current).value

    Column {
        SegmentedControl(FakeArtData.category, selectedCategory) {
            viewModel.filterArticles(it)
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            articleList.take(5).forEach { article ->
                ArticleItem(article)
            }
        }

    }
}

@Composable
fun SegmentedControl(categories: List<String>, selectedCategory: String, onCategorySelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        categories.forEach { category ->
            val isSelected = selectedCategory == category
            Box(
                modifier = Modifier
                    .clickable(
                            onClick = {
                                onCategorySelected(category)
                            },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                    )
                    .border(1.dp, color = if (isSelected) Color.Transparent else iconLight, MaterialTheme.shapes.extraLarge)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = MaterialTheme.shapes.extraLarge
                    )
                    .width(80.dp)
                    .padding(vertical = 2.dp)
                    ,
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category,
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}