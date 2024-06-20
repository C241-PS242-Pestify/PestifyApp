package com.learning.pestifyapp.ui.screen.dashboard.history

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.learning.pestifyapp.R
import com.learning.pestifyapp.data.model.HistoryData
import com.learning.pestifyapp.data.model.local.entity.HistoryImageEntity
import com.learning.pestifyapp.ui.common.converterStringToBitmap

@Composable
fun HistoryScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: HistoryViewModel,
) {
    val history by viewModel.history.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.refreshHistory()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            TopSection()
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                MainSection(
                    viewModel = viewModel,
                    history = history,
                    onItemClick = { historyData ->
                        navController.navigate("detail/${historyData.id}")
                    })
            }
        }
    }
}


@Composable
fun TopSection(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 18.dp, top = 12.dp)
    ) {
        Text(
            text = "Bookmarks",
            modifier = Modifier.align(Alignment.CenterStart),
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun MainSection(
    viewModel: HistoryViewModel,
    history: List<HistoryData>,
    modifier: Modifier = Modifier,
    onItemClick: (HistoryData) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        items(history.size) { index ->
            val prediction = history[index]
            Item(
                viewModel = viewModel,
                historyData = prediction,
                onItemClick = onItemClick
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
        historyData.pest?.id.let { historyId ->
            if (historyId != null) {
                viewModel.getHistoryImage(historyId) { image ->
                    historyImage = image
                    image?.let {
                        val degrees = 90f
                        bitmap = converterStringToBitmap(it.image, degrees)
                    }
                    isLoading = false
                }
            }
        }
    }

    Box(
        modifier = modifier
            .size(150.dp)
            .padding(18.dp)
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(10.dp))
            .background(Color.White)
            .clickable(onClick = { onItemClick(historyData) })
    ) {
        if (bitmap != null) {
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
            val imageResource = when (name) {
                "Mealybugs" -> {
                    R.drawable.aloe_vera
                }

                "Aphids" -> {
                    R.drawable.snake_plant
                }

                "Whiteflies" -> {
                    R.drawable.basil
                }

                else -> {
                    R.drawable.sherlock_profile
                }
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