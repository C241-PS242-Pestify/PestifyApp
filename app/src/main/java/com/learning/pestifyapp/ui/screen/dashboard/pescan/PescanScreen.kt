package com.learning.pestifyapp.ui.screen.dashboard.pescan

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.R
import com.learning.pestifyapp.ui.screen.navigation.Graph

@Composable
fun PescanScreen(
    viewModel: PescanScreenViewModel,
    navController: NavHostController,
    context: MainActivity,
    modifier: Modifier = Modifier,
) {
    val selectedImageUri by viewModel.imageUri.collectAsState()

    LaunchedEffect(navController) {
        val result = navController.currentBackStackEntry
            ?.savedStateHandle
            ?.get<Uri>("imageUri")
        result?.let {
            Log.d("PescanScreen", "Received new image URI: $it")
            viewModel.updateImageUri(it)
        }
    }
    Column(modifier = modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        ImageSection(selectedImageUri, modifier)
        MenuSection(navController, modifier)
        ImagePickerSection(viewModel, context) { uri ->
            viewModel.updateImageUri(uri)
        }
    }
}

@Composable
fun ImageSection(selectedImageUri: Uri?, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(8.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
        ) {
            if (selectedImageUri != null) {
                Image(

                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(selectedImageUri)
                            .build()
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.snake_plant),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun MenuSection(navController: NavHostController, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Button(onClick = {}, shape = RoundedCornerShape(4.dp)) {
                Icon(painterResource(id = R.drawable.scan), contentDescription = null)
            }
            Button(
                onClick = { navController.navigate(Graph.CAMERA) },
                shape = RoundedCornerShape(4.dp)
            ) {
                Icon(painterResource(id = R.drawable.ic_opcam), contentDescription = null)
            }
            Button(onClick = {}, shape = RoundedCornerShape(4.dp)) {
                Icon(
                    painterResource(id = R.drawable.baseline_browse_gallery_24),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun ImagePickerSection(
    viewModel: PescanScreenViewModel,
    context: MainActivity,
    onImageSelected: (Uri) -> Unit,
) {
    val pager = remember {
        Pager(PagingConfig(pageSize = 20)) { ImagePagingSource(context) }.flow.cachedIn(viewModel.viewModelScope)
    }
    val imagePagingItems: LazyPagingItems<Uri> = pager.collectAsLazyPagingItems()

    LaunchedEffect(imagePagingItems) {
        snapshotFlow { imagePagingItems.itemSnapshotList.items }
            .collect { items ->
                Log.d("ImagePickerTab", "Loaded ${items.size} images")
            }
    }
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(210.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(imagePagingItems.itemCount) { index ->
            val uri = imagePagingItems[index]
            uri?.let {
                Log.d("ImagePickerTab", "Displaying image with URI: $uri")
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clickable { onImageSelected(it) }
                )
            }
        }
    }
}
