package com.learning.pestifyapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.learning.pestifyapp.R
import com.learning.pestifyapp.data.model.local.entity.PespediaEntity
import com.learning.pestifyapp.ui.common.loadingFx
import com.learning.pestifyapp.ui.theme.PestifyAppTheme

@Composable
fun PestDiseaseItem(
    modifier: Modifier = Modifier,
    item: PespediaEntity,
    navigateToDetail: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable(
                onClick = {
                    navigateToDetail(item.id)
                },
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
            .padding(bottom = 20.dp)
            .background(Color.White, shape = RoundedCornerShape(10.dp))
    ) {
        Column {

        }
        Image(
            painter = rememberAsyncImagePainter(
                model = item.picture,
                placeholder = painterResource(id = R.drawable.placeholder)
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .height(200.dp)
                .fillMaxSize()
                .clip(RoundedCornerShape(10.dp))
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .align(Alignment.CenterStart)
                .width(250.dp)
                .background(
                    Brush.verticalGradient(
                        startY = 1f,
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.1f),
                            Color.Black.copy(alpha = 0.4f),
                            Color.Black.copy(alpha = 0.8f),
                        )
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 20.dp, start = 16.dp)
            ) {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 18.dp, end = 16.dp)
                    .size(35.dp)
                    .background(Color.White, shape = RoundedCornerShape(50.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.arrow_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun PespediaCategoryLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        LazyColumn {
            items(7) {
                PespediaItemLoading()
            }
        }
    }
}

@Composable
fun PespediaItemLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(bottom = 20.dp)
            .clip(RoundedCornerShape(10.dp))
            .loadingFx()
    )
}

@Preview(showBackground = true)
@Composable
private fun PespediaItemLoadingPreview() {
    PestifyAppTheme {
        Box(
            modifier = Modifier
                .background(Color.Gray)
                .padding(50.dp)
                .fillMaxWidth()
        ) {
            PespediaItemLoading(
                modifier = Modifier.padding(50.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PespediaCategoryPreview() {
    PestifyAppTheme {
        Box(
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxWidth()
        ) {
            PespediaCategoryLoading()
        }
    }
}
