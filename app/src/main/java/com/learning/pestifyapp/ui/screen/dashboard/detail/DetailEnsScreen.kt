package com.learning.pestifyapp.ui.screen.dashboard.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.learning.pestifyapp.R
import com.learning.pestifyapp.data.model.ensdata.Ensiklopedia
import com.learning.pestifyapp.data.model.ensdata.EnsiklopediaData
import com.learning.pestifyapp.di.factory.PespediaFactory
import com.learning.pestifyapp.ui.common.UiState
import com.learning.pestifyapp.ui.components.CustomTopAppBar
import com.learning.pestifyapp.ui.components.ItemSection
import com.learning.pestifyapp.ui.components.PlantCategory
import com.learning.pestifyapp.ui.screen.dashboard.ensiklopedia.EnsiklopediaViewModel
import com.learning.pestifyapp.ui.screen.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun DetailEnsScreen(
    modifier: Modifier = Modifier,
    ensId: String,
    navigateBack: () -> Unit,
    viewModel: EnsiklopediaViewModel = viewModel(factory = PespediaFactory.getInstance())
) {

    val scope = rememberCoroutineScope()

    val uiState by viewModel.uiStateEns.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current,
    )

//    DetailEnsScreenContent(
//        ensData = EnsiklopediaData.ensiklopediaList[0],
//        navigateBack = navigateBack,
//        modifier = modifier
//    )

    when (uiState) {
        is UiState.Loading -> {
            LaunchedEffect(key1 = ensId) {
                scope.launch {
                    viewModel.getEnsArticleById(ensId)
                }
            }
        }

        is UiState.Success -> {
            val ensItem = (uiState as UiState.Success<Ensiklopedia>).data
            DetailEnsScreenContent(
                ensData = ensItem,
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
fun DetailEnsScreenContent(
    ensData: Ensiklopedia,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White),
    ) {
        Column(
            modifier.fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            CustomTopAppBar(
                onBackClick = navigateBack,
                title = stringResource(id = R.string.detail),
                modifier = Modifier.padding(top = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Image(
                painter = painterResource(id = ensData.image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(210.dp)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = ensData.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = ensData.description,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}