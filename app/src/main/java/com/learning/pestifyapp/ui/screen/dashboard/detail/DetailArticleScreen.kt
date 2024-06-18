package com.learning.pestifyapp.ui.screen.dashboard.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.R
import com.learning.pestifyapp.data.model.homeart.Article
import com.learning.pestifyapp.data.model.homeart.FakeArtData
import com.learning.pestifyapp.data.model.local.entity.ArticleEntity
import com.learning.pestifyapp.di.factory.HomeFactory
import com.learning.pestifyapp.ui.common.UiState
import com.learning.pestifyapp.ui.components.CustomTopAppBar
import com.learning.pestifyapp.ui.screen.dashboard.home.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun DetailArticleScreen(
    modifier: Modifier = Modifier,
    articleId: String,
    navigateBack: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = HomeFactory.getInstance(MainActivity.CONTEXT))
) {

    val uiState by viewModel.uiArticle.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current,
    )

//    DetailArticleScreenContent(
//        articleData = FakeArtData.dummyArticles[0],
//        navigateBack = navigateBack,
//        modifier = Modifier
//    )

    when (uiState) {
        is UiState.Loading -> {
            LaunchedEffect(key1 = articleId) {
                viewModel.getArticleById(articleId)
            }
        }

        is UiState.Success -> {
            val articleItem = (uiState as UiState.Success<ArticleEntity>).data
            DetailArticleScreenContent(
                articleData = articleItem,
                navigateBack = navigateBack,
                modifier = modifier
            )
        }

        is UiState.Error -> {
            // Show error message
        }
    }

}

@Composable
fun DetailArticleScreenContent(
    modifier: Modifier = Modifier,
    articleData: ArticleEntity,
    navigateBack: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White),
    ) {
        Column(
            modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),

            ) {
            CustomTopAppBar(
                onBackClick = navigateBack,
                title = articleData.tags,
                modifier = Modifier.padding(top = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = articleData.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
            )
            Text(
                text = articleData.createdAt,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            )
            Spacer(modifier = Modifier.height(18.dp))
            Image(
                painter = rememberAsyncImagePainter(
                    model = articleData.picture,
                    placeholder = painterResource(id = R.drawable.placeholder)
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = articleData.description,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}