package com.learning.pestifyapp.ui.screen.dashboard.detail

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.data.model.homeart.Article
import com.learning.pestifyapp.data.model.local.entity.ArticleEntity
import com.learning.pestifyapp.di.factory.HomeFactory
import com.learning.pestifyapp.ui.common.rememberScrollOffset
import com.learning.pestifyapp.ui.common.ScrollDirection
import com.learning.pestifyapp.ui.common.UiState
import com.learning.pestifyapp.ui.components.ArticleItem
import com.learning.pestifyapp.ui.components.CustomTopAppBar
import com.learning.pestifyapp.ui.screen.dashboard.ensiklopedia.ScrollToTopButton
import com.learning.pestifyapp.ui.screen.dashboard.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DetailCategoriesScreen(
    modifier: Modifier = Modifier,
    context: Context,
    selectedCategory: String,
    navigateBack: () -> Unit,
    navigateToDetail: (String) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = HomeFactory.getInstance(context))
) {
    val uiListArticleState by
    viewModel.uiListArticleState.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current,
        initialValue = UiState.Loading
    )

    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val showButton: Boolean by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 1 }
    }
    val scrollDirection = rememberScrollOffset(listState, scope)



    when (uiListArticleState) {
        is UiState.Loading -> {
            LaunchedEffect(key1 = uiListArticleState) {
                viewModel.getAllArticles()
            }
        }

        is UiState.Success -> {
            LaunchedEffect(key1 = selectedCategory) {
                viewModel.filterArticles(selectedCategory)
            }
            val articleList: List<ArticleEntity> =
                (uiListArticleState as UiState.Success<List<ArticleEntity>>).data
            DetalCategoryContent(
                selectedCategory = selectedCategory,
                articleList = articleList,
                navigateBack = navigateBack,
                listState = listState,
                showButton = showButton,
                scrollDirection = scrollDirection,
                scope = scope,
                navigateToDetail = navigateToDetail
            )

        }

        is UiState.Error -> {
            // Show error message
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetalCategoryContent(
    modifier: Modifier = Modifier,
    selectedCategory: String,
    articleList: List<ArticleEntity>,
    navigateBack: () -> Unit,
    listState: LazyListState,
    showButton: Boolean,
    scrollDirection: State<ScrollDirection>,
    scope: CoroutineScope,
    navigateToDetail: (String) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(horizontal = 16.dp),
    ) {
        LazyColumn(
            state = listState
        ) {

            stickyHeader {
                CustomTopAppBar(
                    onBackClick = navigateBack,
                    title = selectedCategory,
                    modifier = Modifier
                )
                Divider()
            }
            item {
                Spacer(modifier = Modifier.height(25.dp))
            }
            items(articleList, key = { it.id }) { article ->
                ArticleItem(
                    article = article,
                    navigateToDetail = {
                        navigateToDetail(it)
                    }
                )
            }
        }

        AnimatedVisibility(
            visible = showButton && (scrollDirection.value == ScrollDirection.DOWN),
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
            modifier = Modifier
                .padding(bottom = 30.dp)
                .align(Alignment.BottomCenter)
        ) {
            ScrollToTopButton(
                onClick = {
                    scope.launch {
                        listState.scrollToItem(0)
                    }
                }
            )
        }
    }
}