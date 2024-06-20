package com.learning.pestifyapp.ui.screen.dashboard.pescan

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.R
import com.learning.pestifyapp.data.model.remote.PestResponse
import com.learning.pestifyapp.ui.common.compressBitmap
import com.learning.pestifyapp.ui.components.CustomTab

@Composable
fun PestResultScreen(
    viewModel: PestViewModel,
    navController: NavHostController,
    context: MainActivity,
    imageUri: Uri?,
    modifier: Modifier = Modifier,
) {
    val pestResponse by viewModel.pestResponse.collectAsState()

    LaunchedEffect(true) {
        viewModel.loadPestDataFromPreferences()
    }
    Box(modifier = modifier.fillMaxSize().background(Color.White)) {

        LazyColumn {
            item {
                pestResponse?.let { response ->
                    TopSection(response, viewModel, navController, imageUri, context)
                    ImageSection(response, imageUri)
                    ButtonSection(response)
                } ?: run {
                    //implement animated data loading
                }
            }
        }
    }
}
@Composable
fun TopSection(
    response: PestResponse,
    viewModel: PestViewModel,
    navController: NavHostController,
    imageUri: Uri?,
    context: MainActivity,
) {
    var isSaved by rememberSaveable { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp)
    ) {
        IconButton(
            onClick = {
                if (!isSaved) {
                    response.data?.id?.let { viewModel.DeletePrediction(it) }
                }
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
            text =  stringResource(R.string.result),
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )
        IconButton(
            onClick = {
                imageUri?.let { uri ->
                    response.data?.id?.let { id ->
                        val bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
                        val compressedBitmap = compressBitmap(bitmap, 20)
                        viewModel.savePrediction(id, compressedBitmap)
                        isSaved = true
                    }
                }
            },
            modifier = Modifier
                .clip(CircleShape)
                .border(0.5.dp, Color.Gray, CircleShape)
                .size(34.dp)
        ) {
            Icon(
                painter = painterResource(id = if (isSaved) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24),
                contentDescription = "Bookmarks"
            )
        }
    }
}

@Composable
fun ImageSection(pestResponse: PestResponse, imageUri: Uri?, modifier: Modifier = Modifier) {
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
            imageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Pest Image",
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp))
                )
            } ?: run {
                Text("No image captured", style = MaterialTheme.typography.bodyLarge)
            }

        }
        Spacer(modifier = modifier.height(18.dp))
        pestResponse.data?.let { data ->
            Text(
                text = data.pestName ?: "N/A",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = modifier.fillMaxWidth()
            )
            Text(
                text = String.format("%.2f", data.confidenceScore),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                modifier = modifier.fillMaxWidth()
            )
        } ?: run {
            Text(text = "No data available")
        }
    }
}
@Composable
fun ButtonSection(pestResponse: PestResponse) {
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
                pestResponse.data?.let { data ->
                    Column {
                        Text(
                            text = data.pestDescription ?: "N/A",
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
                pestResponse.data?.let { data ->
                    Column {
                        Text(
                            text = data.pestEffect ?: "N/A",
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
                pestResponse.data?.let { data ->
                    Column {
                        Text(
                            text = data.solution ?: "N/A",
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