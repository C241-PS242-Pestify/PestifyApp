package com.learning.pestifyapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.learning.pestifyapp.ui.theme.PestifyAppTheme

@Composable
fun CaptureButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(70.dp)
            .background(Color.White, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(61.dp)
                .border(2.dp, Color.Black, CircleShape)
                .background(Color.White, CircleShape)
        )
    }
}