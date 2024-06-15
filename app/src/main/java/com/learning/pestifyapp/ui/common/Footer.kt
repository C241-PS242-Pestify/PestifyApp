package com.learning.pestifyapp.ui.common

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import com.learning.pestifyapp.R

object TopWithFooter : Arrangement.Vertical {
    override fun Density.arrange(
        totalSize: Int,
        sizes: IntArray,
        outPositions: IntArray
    ) {
        var y = 0
        sizes.forEachIndexed { index, size ->
            outPositions[index] = y
            y += size
        }
        if (y < totalSize) {
            val lastIndex = outPositions.lastIndex
            outPositions[lastIndex] = totalSize - sizes.last()
        }
    }
}

@Composable
fun SetWindowBackground(){
    val activity = LocalContext.current as Activity
    LaunchedEffect(activity) {
        activity.window.setBackgroundDrawableResource(R.drawable.aquaponics)
    }
}