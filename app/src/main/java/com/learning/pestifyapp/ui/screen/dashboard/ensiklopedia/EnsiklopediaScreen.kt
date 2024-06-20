package com.learning.pestifyapp.ui.screen.dashboard.ensiklopedia

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.learning.pestifyapp.R
import com.learning.pestifyapp.data.model.ensdata.Ensiklopedia
import com.learning.pestifyapp.data.model.ensdata.EnsiklopediaData
import com.learning.pestifyapp.data.model.local.entity.PespediaEntity
import com.learning.pestifyapp.ui.common.RememberScrollDirection
import com.learning.pestifyapp.ui.common.UiState
import com.learning.pestifyapp.ui.components.BottomBarState
import com.learning.pestifyapp.ui.components.CustomSearchBar
import com.learning.pestifyapp.ui.components.ItemSection
import com.learning.pestifyapp.ui.components.PespediaCategoryLoading
import com.learning.pestifyapp.ui.components.PestDiseaseItem
import com.learning.pestifyapp.ui.components.PlantCategory
import com.learning.pestifyapp.ui.screen.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@Composable
fun EnsiklopediaScreen(
    bottomBarState: BottomBarState,
    viewModel: EnsiklopediaViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val isBottomBarVisible = bottomBarState.bottomAppBarState.collectAsState()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val showButton: Boolean by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 1 }
    }
    RememberScrollDirection(listState, bottomBarState, scope)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle(lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current)
    val query by viewModel.query

    EnsiklopediaContent(
        modifier = modifier,
        scope = scope,
        viewModel = viewModel,
        query = query,
        uiState = uiState,
        isBottomBarVisible = isBottomBarVisible,
        bottomBarState = bottomBarState,
        showButton = showButton,
        listState = listState,
        navController = navController
    )
}

@Composable
fun EnsiklopediaContent(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    viewModel: EnsiklopediaViewModel,
    query: String,
    uiState: UiState<List<PespediaEntity>>,
    isBottomBarVisible: State<Boolean>,
    bottomBarState: BottomBarState,
    showButton: Boolean,
    listState: LazyListState,
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {

            LaunchedEffect(true) {
                bottomBarState.setBottomAppBarState(true)
                listState.scrollToItem(0)
            }

            when (uiState) {
                is UiState.Loading -> {
                    PespediaCategoryLoading()
                    LaunchedEffect(true) {
                        viewModel.getAllEnsArticles()
                    }
                }

                is UiState.Success -> {
                    val ensList = uiState.data

                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        item {
                            Text(
                                text = stringResource(R.string.ensiklopedia),
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 16.dp)
                            )

                            CustomSearchBar(
                                query = query,
                                onQueryChange = {
                                    viewModel.search(it)
                                },
                                onSearch = {
                                    viewModel.onSearch(it)
                                },
                                modifier = Modifier.padding(top = 24.dp, bottom = 24.dp)
                            )
                        }

                        if (ensList.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = modifier.padding(top = 200.dp),
                                    ) {
                                        Text(
                                            text = stringResource(R.string.onSearch),
                                            fontSize = 24.sp,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold,
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = stringResource(R.string.onSearchDesc),
                                            fontSize = 18.sp,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Normal,
                                        )
                                    }
                                }
                            }
                        } else {
                            items(ensList, key = { it.id }) { ensiklopedia ->
                                PestDiseaseItem(
                                    item = ensiklopedia,
                                    navigateToDetail = { pestId ->
                                        navController.navigate(
                                            Screen.DetailEns.createRoute(pestId)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }


                is UiState.Error -> {
                    // Show error message
                }
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
fun ScrollToTopButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = Modifier
            .width(160.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(
                    16.dp
                )
            )
            .clickable(
                onClick = {
                    onClick()
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Row(
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .padding(8.dp)
        ) {
            Text(
                text = stringResource(R.string.backtop),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = stringResource(R.string.backtop),
            )
        }
    }
}

