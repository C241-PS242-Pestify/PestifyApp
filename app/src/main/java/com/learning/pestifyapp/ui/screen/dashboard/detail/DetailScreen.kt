package com.learning.pestifyapp.ui.screen.dashboard.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.learning.pestifyapp.R
import com.learning.pestifyapp.ui.common.SetWindowBackground

@Composable
fun DetailScreen(
    plantId: Long,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    SetWindowBackground()
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Gray),
        contentAlignment = Alignment.Center,
    ) {
        Text("Detail")
    }
}