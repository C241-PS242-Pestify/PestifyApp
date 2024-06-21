package com.learning.pestifyapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomTab(
    text: String,
    isSelected: Boolean,
    onTabSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = if (isSelected) Color.Gray else Color.Transparent
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .width(120.dp)
            .height(37.dp)
            .border(0.5.dp, Color.Gray, RoundedCornerShape(5.dp))
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(5.dp),
            )
            .border(
                border = BorderStroke(width = 1.dp, color = borderColor),
                shape = RoundedCornerShape(5.dp)
            )
            .clickable(onClick = onTabSelected)
    ) {
        Text(
            text = text,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}