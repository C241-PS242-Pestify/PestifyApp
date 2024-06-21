package com.learning.pestifyapp.ui.screen.dashboard.detail

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.R
import com.learning.pestifyapp.data.model.local.entity.PlantEntity
import com.learning.pestifyapp.data.model.plant.FakePlantData
import com.learning.pestifyapp.data.model.plant.PlantData
import com.learning.pestifyapp.di.factory.HomeFactory
import com.learning.pestifyapp.ui.common.SetWindowBackground
import com.learning.pestifyapp.ui.common.UiState
import com.learning.pestifyapp.ui.components.CustomTopAppBar
import com.learning.pestifyapp.ui.screen.dashboard.home.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun DetailPlantScreen(
    modifier: Modifier = Modifier,
    plantId: String,
    context: Context,
    navigateBack: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = HomeFactory.getInstance(context))
) {

    val uiState by viewModel.uiPlantState.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current,
    )

//    DetailPlantScreenContent(
//        plantData = FakePlantData.dummyPlants[0],
//        navigateBack = navigateBack,
//        modifier = modifier
//    )

    when (uiState) {
        is UiState.Loading -> {
            LaunchedEffect(key1 = plantId) {
                    viewModel.getPlantById(plantId)
            }
        }

        is UiState.Success -> {
            val plantItem = (uiState as UiState.Success<PlantEntity>).data
            DetailPlantScreenContent(
                plantData = plantItem,
                navigateBack = navigateBack,
                modifier = modifier
            )
        }

        is UiState.Error -> {
            // Show error message
        }

        UiState.Empty -> TODO()
    }
}

@Composable
fun DetailPlantScreenContent(
    modifier: Modifier = Modifier,
    plantData: PlantEntity,
    navigateBack: () -> Unit
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
                title = plantData.title,
                modifier = Modifier.padding(top = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Image(
                painter = rememberAsyncImagePainter(
                    model = plantData.picture,
                    placeholder = painterResource(id = R.drawable.placeholder)
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = plantData.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = plantData.description,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}
