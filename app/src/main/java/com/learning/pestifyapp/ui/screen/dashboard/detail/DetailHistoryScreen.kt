package com.learning.pestifyapp.ui.screen.dashboard.detail

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.learning.pestifyapp.R
import com.learning.pestifyapp.data.model.historydata.HistoryData
import com.learning.pestifyapp.data.model.local.entity.HistoryImageEntity
import com.learning.pestifyapp.ui.common.converterStringToBitmap
import com.learning.pestifyapp.ui.common.loadingFx
import com.learning.pestifyapp.ui.components.CustomAlertDialog
import com.learning.pestifyapp.ui.components.CustomTab
import com.learning.pestifyapp.ui.screen.dashboard.history.HistoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DetailHistoryScreen(
    historyId: String,
    viewModel: HistoryViewModel,
    navController: NavHostController,
    context: Context
) {
    val historyData by viewModel.selectedHistory.collectAsState()

    LaunchedEffect(historyId) {
        viewModel.fetchHistoryById(historyId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            item {
                historyData?.let { data ->
                    TopSection(data, viewModel, navController)
                    ImageSection(data, viewModel, context = context)
                    ButtonSection(data)
                }
            }
        }
    }
}

@Composable
fun TopSection(data: HistoryData, viewModel: HistoryViewModel, navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        CustomAlertDialog(
            title = "Delete Confirmation",
            message = "Are you sure you want to delete this history?",
            onYesClick = {
                data.id?.let { viewModel.deleteHistoryById(it) }
                navController.popBackStack()
                showDialog = false
            },
            onNoClick = { showDialog = false },
            onDismiss = { showDialog = false }
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp)
    ) {
        IconButton(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .clip(CircleShape)
                .border(0.5.dp, Color.Gray, CircleShape)
                .size(34.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = "Navigate Back"
            )
        }
        Text(
            text = stringResource(R.string.detail),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,

            )
        IconButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .clip(CircleShape)
                .border(0.5.dp, Color.Gray, CircleShape)
                .size(34.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.bookmarks_fill),
                contentDescription = "Navigate Back",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun ImageSection(
    historyData: HistoryData,
    viewModel: HistoryViewModel,
    modifier: Modifier = Modifier,
    context : Context
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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .loadingFx()
                )            } else if (bitmap != null) {
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
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context)
                            .data(imageResource)
                            .apply {
                                crossfade(true)
                                diskCachePolicy(CachePolicy.ENABLED)
                                memoryCachePolicy(CachePolicy.ENABLED)
                            }
                            .build()
                    ),
                    contentDescription = "Aquaponics",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        historyData.pest.let { data ->
            Text(
                text = data?.pestName ?: "N/A",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Text(
                text = String.format("%.2f", data?.confidenceScore),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ButtonSection(historyData: HistoryData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp),
    ) {
        var selectedTab by remember { mutableStateOf("about") }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomTab(
                text = "About",
                isSelected = selectedTab == "about",
                onTabSelected = { selectedTab = "about" }
            )
            CustomTab(
                text = "Effect",
                isSelected = selectedTab == "effect",
                onTabSelected = { selectedTab = "effect" }
            )
            CustomTab(
                text = "Care",
                isSelected = selectedTab == "care",
                onTabSelected = { selectedTab = "care" }
            )
        }
        when (selectedTab) {
            "about" -> Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(vertical = 30.dp)
            ) {
                historyData.pest?.let { pest ->
                    Column {
                        Text(
                            text = pest.pestDescription ?: "N/A",
                            textAlign = TextAlign.Justify,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                        )
                    }
                } ?: run {
                    DetailHistoryLoading()

                }
            }

            "effect" -> Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(vertical = 30.dp)
            ) {
                historyData.pest?.let { pest ->
                    Column {
                        Text(
                            text = pest.pestEffect ?: "N/A",
                            textAlign = TextAlign.Justify,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                        )
                    }
                } ?: run {
                    DetailHistoryLoading()
                }
            }

            "care" -> Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(vertical = 30.dp)
            ) {
                historyData.pest?.let { pest ->
                    Column {
                        Text(
                            text = pest.solution ?: "N/A",
                            textAlign = TextAlign.Justify,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                        )
                    }
                } ?: run {
                    DetailHistoryLoading()
                }
            }
        }
    }
}
@Composable
fun DetailHistoryLoading(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .size(150.dp)
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(10.dp))
            .loadingFx()
    )
}
