package com.learning.pestifyapp.ui.screen.dashboard.detail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.learning.pestifyapp.R
import com.learning.pestifyapp.data.model.HistoryData
import com.learning.pestifyapp.data.model.local.entity.HistoryImageEntity
import com.learning.pestifyapp.ui.common.converterStringToBitmap
import com.learning.pestifyapp.ui.components.CustomAlertDialog
import com.learning.pestifyapp.ui.components.CustomTab
import com.learning.pestifyapp.ui.screen.dashboard.history.HistoryViewModel

@Composable
fun DetailHistoryScreen(
    historyId: String,
    viewModel: HistoryViewModel,
    navController: NavHostController,
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
                    ImageSection(data, viewModel)
                    ButtonSection(data)
                } ?: run {
                    Text("Loading response...", style = MaterialTheme.typography.bodyLarge)
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
            text = "Detail",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontWeight = FontWeight.SemiBold,
            fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
            color = MaterialTheme.colorScheme.primary

        )
        IconButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .clip(CircleShape)
                .border(0.5.dp, Color.Gray, CircleShape)
                .size(34.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.delete_alt_2),
                contentDescription = "Navigate Back"
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
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (bitmap != null) {

                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = "Item Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            } else {
                val imageResource = if (name == "Mealybugs") {
                    R.drawable.aloe_vera
                } else if (name == "Aphids") {
                    R.drawable.snake_plant
                } else if (name == "Whiteflies") {
                    R.drawable.basil
                } else {
                    R.drawable.bok_choy
                }
                Image(
                    painter = painterResource(id = imageResource),
                    contentDescription = "Item Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .drawWithContent {
                            drawContent()
                        }
                )
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        historyData.pest.let { data ->
            Text(
                text = data?.pestName ?: "N/A",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = String.format("%.2f", data?.confidenceScore),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        } ?: run {
            Text(text = "No data available")
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
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                        )
                    }
                } ?: run {
                    Text(text = "No data available")
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
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                        )
                    }
                } ?: run {
                    Text(text = "No data available")
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
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                        )
                    }
                } ?: run {
                    Text(text = "No data available")
                }
            }
        }
    }
}
