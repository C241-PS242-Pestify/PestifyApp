package com.learning.pestifyapp.ui.screen.dashboard.history

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.learning.pestifyapp.R
import com.learning.pestifyapp.data.model.historydata.HistoryData
import com.learning.pestifyapp.data.model.local.entity.HistoryImageEntity
import com.learning.pestifyapp.ui.common.RememberScrollDirection
import com.learning.pestifyapp.ui.common.converterStringToBitmap
import com.learning.pestifyapp.ui.common.loadingFx
import com.learning.pestifyapp.ui.components.BottomBarState
import com.learning.pestifyapp.ui.screen.dashboard.ensiklopedia.ScrollToTopButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun HistoryScreen(
    bottomBarState: BottomBarState,
    navController: NavHostController = rememberNavController(),
    viewModel: HistoryViewModel,
) {
    val history by viewModel.history.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isBottomBarVisible = bottomBarState.bottomAppBarState.collectAsState()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyGridState()
    val showButton: Boolean by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 1 }
    }
    RememberScrollDirection(listState, bottomBarState, scope)

    LaunchedEffect(Unit) {
        viewModel.refreshHistory()
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            TopSection()
            if (isLoading) {
//                HistoryScreenLoading()
            } else {
                MainSection(
                    viewModel = viewModel,
                    history = history,
                    onItemClick = { historyData ->
                        navController.navigate("detail/${historyData.id}")
                    },
                    bottomBarState = bottomBarState,
                    showButton = showButton,
                    listState = listState,
                    isBottomBarVisible = isBottomBarVisible,
                    scope = scope,
                    )

            }
        }
    }
}

@Composable
fun HistoryScreenLoading(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize()
        ) {
            items(8) {
                HistoryItemLoading()
            }
        }
    }
}

@Composable
fun HistoryItemLoading(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .size(150.dp)
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(10.dp))
            .loadingFx()
    )
}

@Composable
fun TopSection(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.menu_bookmark),
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp, start = 16.dp, top = 16.dp)
        )
    }
}

@Composable
fun MainSection(
    viewModel: HistoryViewModel,
    history: List<HistoryData>,
    modifier: Modifier = Modifier,
    bottomBarState: BottomBarState,
    showButton: Boolean,
    listState: LazyGridState,
    onItemClick: (HistoryData) -> Unit,
    isBottomBarVisible: State<Boolean>,
    scope: CoroutineScope,

    ) {
    LaunchedEffect(true) {
        scope.launch {
            bottomBarState.setBottomAppBarState(true)
            listState.scrollToItem(0)
        }
    }
    Box(modifier = modifier.fillMaxSize()
        .fillMaxSize()
        .background(Color.White)
    ) {
        LazyVerticalGrid(
            state = listState,
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()

        ) {
            items(history) { history ->
                Item(
                    viewModel = viewModel,
                    historyData = history,
                    onItemClick = onItemClick
                )
            }
        }
        AnimatedVisibility(
            visible = showButton && !isBottomBarVisible.value,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
            modifier = Modifier
                .padding(bottom = 30.dp)
                .align(Alignment.BottomCenter)
        ) {
            ScrollToTopButton(
                onClick = {
                    scope.launch {
                        listState.scrollToItem(index = 0)
                    }
                }
            )
        }
    }
}

@Composable
fun Item(
    viewModel: HistoryViewModel,
    modifier: Modifier = Modifier,
    historyData: HistoryData,
    onItemClick: (HistoryData) -> Unit,
) {
    var historyImage by remember {
        mutableStateOf<HistoryImageEntity?>(
            null
        )
    }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val name = historyData.pest?.pestName


    LaunchedEffect(historyData.pest?.id) {
        historyData.pest?.id?.let { historyId ->
            viewModel.getHistoryImage(historyId) { image ->
                image?.let {
                    isLoading = true
                    CoroutineScope(Dispatchers.IO).launch {
                        val degrees = 90f
                        val processedBitmap = converterStringToBitmap(it.image, degrees)
                        withContext(Dispatchers.Main) {
                            bitmap = processedBitmap
                            isLoading = false
                        }
                    }
                } ?: run {
                    isLoading = false
                }
            }
        }
    }

    Box(
        modifier = modifier
            .size(150.dp)
            .background(Color.White)
            .padding(16.dp)
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(10.dp))
            .clickable(onClick = { onItemClick(historyData) })
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .loadingFx()
            )
        } else if (bitmap != null) {
            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = "Item Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .size(150.dp)
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black
                                ),
                                startY = size.height / 2,
                                endY = size.height
                            ),
                            size = size
                        )
                    }
            )
        } else {
            val imageResource = if (name == "Mealybugs") {
                R.drawable.mealybugs_
            } else if (name == "Aphids") {
                R.drawable.aphids_
            } else if (name == "Whiteflies") {
                R.drawable.whiteflies_
            } else {
                R.drawable.placeholder
            }
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = "Item Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .size(150.dp)
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black
                                ),
                                startY = size.height / 2,
                                endY = size.height
                            ),
                            size = size
                        )
                    }
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp)
        ) {
            Text(
                text = historyData.pest?.pestName ?: "Unknown",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Icon(
                painter = painterResource(id = R.drawable.arrow_icon),
                contentDescription = "Arrow Icon",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(14.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}
