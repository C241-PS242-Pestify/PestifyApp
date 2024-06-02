package com.learning.pestifyapp.ui.screen.dashboard.pescan

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.R
import com.learning.pestifyapp.ui.screen.navigation.Graph

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PescanScreen(
    viewModel: PescanScreenViewModel,
    navController: NavHostController,
    context: MainActivity,
    modifier: Modifier = Modifier,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabs = listOf("Image Picker", "Live Scan")

    Scaffold(
        topBar = {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index }
                    )
                }
            }
        }
    ) {
        when (selectedTabIndex) {
            0 -> ImagePickerTab(viewModel, modifier, context, navController)
            1 -> LiveScanTab(viewModel, navController, context, modifier)
        }
    }
}

@Composable
fun ImagePickerTab(
    viewModel: PescanScreenViewModel,
    modifier: Modifier = Modifier,
    context: MainActivity,
    navController: NavHostController,
) {
    val pager = remember {
        Pager(PagingConfig(pageSize = 20)) {
            ImagePagingSource(context)
        }.flow.cachedIn(viewModel.viewModelScope)
    }

    val imagePagingItems: LazyPagingItems<Uri> = pager.collectAsLazyPagingItems()

    LaunchedEffect(imagePagingItems) {
        snapshotFlow { imagePagingItems.itemSnapshotList.items }
            .collect { items ->
                Log.d("ImagePickerTab", "Loaded ${items.size} images")
            }
    }

    val selectedImageUri by viewModel.imageUri.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 50.dp)
        ) {
            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Pick an image from your device")
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 4.dp, end = 8.dp)
        ) {
            Button(onClick = {

            }) {
                Text(text = "Analyze")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                navController.navigate(Graph.CAMERA)
            }) {
                Icon(
                    painterResource(id = R.drawable.ic_opcam),
                    contentDescription = null
                )
            }
            Button(onClick = {

            }) {
                Icon(
                    painterResource(id = R.drawable.baseline_browse_gallery_24),
                    contentDescription = null
                )
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
                            .clickable {
                                viewModel.updateImageUri(it)
                            }
                    )
                }
            }
        }
    }
}



@Composable
fun pickImage(viewModel: PescanScreenViewModel): ActivityResultLauncher<String> {
    return rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            viewModel.updateImageUri(it)
        }
    }
}

@Composable
fun LiveScanTab(
    viewModel: PescanScreenViewModel,
    navController: NavHostController,
    context: MainActivity,
    modifier: Modifier = Modifier,
) {
    val cameraPermissionRequest =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                navController.navigate(Graph.CAMERA)
            } else {
                // Permission not Granted
            }
        }

    LaunchedEffect(Unit) {
        val permissionStatus =
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (permissionStatus == PermissionChecker.PERMISSION_GRANTED) {
            navController.navigate(Graph.CAMERA)
        } else {
            cameraPermissionRequest.launch(Manifest.permission.CAMERA)
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Live Scan")
    }

}
