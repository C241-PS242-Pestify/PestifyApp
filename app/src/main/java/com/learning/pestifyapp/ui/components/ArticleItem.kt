package com.learning.pestifyapp.ui.components

import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.learning.pestifyapp.R
import com.learning.pestifyapp.data.model.homeart.FakeArtData
import com.learning.pestifyapp.data.model.local.entity.ArticleEntity
import com.learning.pestifyapp.ui.common.formatDateToUSLongFormat
import com.learning.pestifyapp.ui.common.loadingFx
import com.learning.pestifyapp.ui.screen.dashboard.home.HomeViewModel
import com.learning.pestifyapp.ui.screen.navigation.Screen
import com.learning.pestifyapp.ui.theme.PestifyAppTheme
import com.learning.pestifyapp.ui.theme.iconLight

@Composable
fun ArticleItem(
    modifier: Modifier = Modifier,
    article: ArticleEntity,
    navigateToDetail: (String) -> Unit
) {
    val isClickable = remember { mutableStateOf(true) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .background(Color.White)
            .clickable(
                onClick = {
                    if (isClickable.value) {
                        isClickable.value = false
                        navigateToDetail(article.id)
                    }
                },
                interactionSource = remember { MutableInteractionSource() },
                indication = null

            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = article.picture,
                placeholder = painterResource(id = R.drawable.placeholder)
            ),
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
                text = article.tags,
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
                text = formatDateToUSLongFormat(article.createdAt),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ArticleCategory(
    modifier: Modifier = Modifier,
    articleList: List<ArticleEntity>,
    navController: NavHostController,
    viewModel: HomeViewModel,
    navigateToDetail: (String) -> Unit,
) {
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle(lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current)
    val isClickable = remember { mutableStateOf(true) }

    Column {
        SegmentedControl(FakeArtData.category, selectedCategory) {
            viewModel.filterArticles(it)
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            articleList.take(5).forEach { article ->
                ArticleItem(
                    article = article,
                    navigateToDetail = navigateToDetail,
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(bottom = 18.dp)
                .size(40.dp)
                .clickable(
                    onClick = {
                        if (isClickable.value) {
                            isClickable.value = false
                            navController.navigate(
                                Screen.DetailCategories.createRoute(
                                    selectedCategory
                                )
                            )
                        }
                    },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                )
                .background(Color.Transparent, shape = RoundedCornerShape(50.dp))
                .border(1.dp, color = iconLight, shape = RoundedCornerShape(50.dp))
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_up),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
            )
        }

    }
}

@Composable
fun SegmentedControl(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val isClickableMap = remember {
        mutableStateMapOf<String, Boolean>().apply {
            categories.forEach {
                this[it] = true
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        categories.forEach { category ->
            val isSelected = selectedCategory == category
            Box(
                modifier = Modifier
                    .clickable(
                        onClick = {
                            if (isClickableMap[category] == true) {
                                Log.d("SegmentedControl", "Clicked: $category")
                                categories.forEach { isClickableMap[it] = true }
                                isClickableMap[category] = false
                                onCategorySelected(category)
                            }
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .border(
                        1.dp,
                        color = if (isSelected) Color.Transparent else iconLight,
                        MaterialTheme.shapes.extraLarge
                    )
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = MaterialTheme.shapes.extraLarge
                    )
                    .width(80.dp)
                    .padding(vertical = 2.dp),
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

@Composable
fun ArticleCategoryLoading(
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in 0..3) {
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .height(30.dp)
                        .padding(vertical = 2.dp)
                        .loadingFx()
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            for (i in 0..5) {
                ArticleItemLoading()

            }
        }
    }
}

@Composable
fun ArticleItemLoading(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .clip(RoundedCornerShape(10.dp))
            .height(95.dp)
            .loadingFx()
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(95.dp)
                .clip(RoundedCornerShape(10.dp))
                .loadingFx(),
        )
        Spacer(modifier = Modifier.width(25.dp))
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 2.dp)
                    .height(12.dp)
                    .fillMaxWidth(0.5f)
                    .loadingFx()
            )
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .height(10.dp)
                    .fillMaxWidth(0.9f)
                    .loadingFx()
            )
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 2.dp)
                    .height(10.dp)
                    .fillMaxWidth(0.8f)
                    .loadingFx()
            )
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 5.dp)
                    .height(10.dp)
                    .fillMaxWidth(0.4f)
                    .loadingFx()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ArticleItemLoadingPreview() {
    PestifyAppTheme {
        Box(
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxWidth()
        ) {
            ArticleItemLoading(
                modifier = Modifier.padding(50.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ArticleCategoryLoadingPreview() {
    PestifyAppTheme {
        Box(
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxWidth()
        ) {
            ArticleCategoryLoading()
        }
    }
}
