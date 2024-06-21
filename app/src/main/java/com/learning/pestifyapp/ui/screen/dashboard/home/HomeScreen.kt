package com.learning.pestifyapp.ui.screen.dashboard.home

import android.app.Activity
import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.learning.pestifyapp.R
import com.learning.pestifyapp.data.model.local.entity.ArticleEntity
import com.learning.pestifyapp.data.model.local.entity.PlantEntity
import com.learning.pestifyapp.data.model.user.UserData
import com.learning.pestifyapp.ui.common.TopWithFooter
import com.learning.pestifyapp.ui.common.UiState
import com.learning.pestifyapp.ui.components.AnimatedStatusBarColorOnScroll
import com.learning.pestifyapp.ui.components.ArticleCategory
import com.learning.pestifyapp.ui.components.ArticleCategoryLoading
import com.learning.pestifyapp.ui.components.BottomBarState
import com.learning.pestifyapp.ui.components.ItemSection
import com.learning.pestifyapp.ui.components.PlantCategory
import com.learning.pestifyapp.ui.components.PlantCategoryLoading
import com.learning.pestifyapp.ui.screen.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    context: Context,
    viewModel: HomeViewModel,
    bottomBarState: BottomBarState,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    val uiListPlantState by
    viewModel.uiListPlantState.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current,
    )

    val uiListArticleState by
    viewModel.uiListArticleState.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current,
    )

    HomeContent(
        modifier = modifier,
        context = context,
        uiListPlantState = uiListPlantState,
        uiListArticleState = uiListArticleState,
        navController = navController,
        viewModel = viewModel,
        bottomBarState = bottomBarState,
    )
}

@Composable
fun HomeContent(
    modifier: Modifier,
    context: Context,
    uiListPlantState: UiState<List<PlantEntity>>,
    uiListArticleState: UiState<List<ArticleEntity>>,
    viewModel: HomeViewModel,
    bottomBarState: BottomBarState,
    navController: NavHostController,
) {
    val userData by viewModel.user.collectAsState()
    val defaultStatusBarColor = MaterialTheme.colorScheme.primary
    val scrolledStatusBarColor = MaterialTheme.colorScheme.primary

    LaunchedEffect(true) {
        viewModel.fetchUser()
    }
    AnimatedStatusBarColorOnScroll(
        context = context,
        defaultStatusBarColor = defaultStatusBarColor,
        scrolledStatusBarColor = scrolledStatusBarColor,
        isDefaultStatusBarIconsDark = true,
        isScrolledStatusBarIconsDark = true,
        bottomBarState = bottomBarState,
    ) { listState ->
        val scope = rememberCoroutineScope()

        LaunchedEffect(true) {
            scope.launch {
                bottomBarState.setBottomAppBarState(true)
                listState.scrollToItem(0)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                state = listState,
                verticalArrangement = TopWithFooter,
            ) {

                item {
                    TopSection(context = context, userData)
                }
                item {
                    when (uiListPlantState) {
                        is UiState.Loading -> {
                            PlantCategoryLoading()
                            LaunchedEffect(true) {
                                viewModel.getAllPlants()
                            }
                        }

                        is UiState.Success -> {
                            val plantList = uiListPlantState.data
                            ItemSection(
                                title = context.getString(R.string.plants),
                                content = {
                                    PlantCategory(
                                        plantList,
                                        navigateToDetail = { plantId ->
                                            navController.navigate(
                                                Screen.DetailPlant.createRoute(
                                                    plantId
                                                )
                                            )
                                        }
                                    )
                                }
                            )
                        }

                        is UiState.Error -> {
                            // Show error message
                        }

                        UiState.Empty -> TODO()
                    }
                }

                item {
                    when (uiListArticleState) {
                        is UiState.Loading -> {
                            ArticleCategoryLoading()
                            LaunchedEffect(true) {
                                viewModel.getAllArticles()
                            }
                        }

                        is UiState.Success -> {
                            val articleList = uiListArticleState.data
                            ArticleCategory(
                                articleList = articleList,
                                viewModel = viewModel,
                                navigateToDetail = { articleId ->
                                    navController.navigate(
                                        Screen.DetailArticle.createRoute(
                                            articleId
                                        )
                                    )
                                },
                                navController = navController,
                            )
                        }

                        is UiState.Error -> {
                            // Show error message
                        }

                        UiState.Empty -> TODO()
                    }
                }
            }
        }
    }
}

@Composable
fun TopSection(
    context: Context,
    userData: UserData?,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(310.dp)
            .padding(top = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context)
                            .data(userData?.profilePhoto)
                            .apply {
                                crossfade(true)
                                diskCachePolicy(CachePolicy.ENABLED) // Enable disk caching to reduce load times
                                memoryCachePolicy(CachePolicy.ENABLED) // Enable memory caching to reduce load times
                                fallback(R.drawable.sherlock_profile)
                                placeholder(R.drawable.placeholder)
                            }.build()
                    ),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(65.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Welcome,",
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                    if (userData != null) {
                        userData.username?.let {
                            Text(
                                text = it,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF006d5b)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context)
                            .data(R.drawable.aquaponics)
                            .apply {
                                crossfade(true)
                                placeholder(R.drawable.placeholder) // Placeholder image while loading
                                error(R.drawable.placeholder) // Error image if loading fails
                                diskCachePolicy(CachePolicy.ENABLED)
                                memoryCachePolicy(CachePolicy.ENABLED)
                            }
                            .build()
                    ),
                    contentDescription = "Aquaponics",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .align(Alignment.CenterStart)
                        .width(250.dp)
                        .background(
                            Brush.horizontalGradient(
                                startX = 1f,
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.9f),
                                    Color.Black.copy(alpha = 0.8f),
                                    Color.Transparent,
                                )
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .width(200.dp)
                            .align(Alignment.CenterStart),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = context.getString(R.string.image_desc),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(
                                start = 12.dp,
                                bottom = 4.dp,
                                top = 12.dp,
                                end = 12.dp
                            )
                        )

                        Button(
                            onClick = { /* TODO */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0XFFFFC107)
                            ),
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .align(Alignment.Start),

                            ) {
                            Text(
                                text = "Read More",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(0.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
