package com.learning.pestifyapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.pestifyapp.R
import com.learning.pestifyapp.ui.theme.base60

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    onBackClick: () -> Unit,
    title: String,
    modifier: Modifier

) {
    TopAppBar(
        modifier = modifier
            .fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
        ),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 42.dp)
                )
            }
        },
        navigationIcon = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Transparent, shape = RoundedCornerShape(50.dp))
                    .border(1.dp, color = base60, shape = RoundedCornerShape(50.dp))
                    .clickable(
                        onClick = {

                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.arrow_back),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 3.dp)
                        .clickable(
                            onClick = {
                                onBackClick()
                            },
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        )
                )
            }
        }
    )
}