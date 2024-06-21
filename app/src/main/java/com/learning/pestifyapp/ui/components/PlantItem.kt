package com.learning.pestifyapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.learning.pestifyapp.R
import com.learning.pestifyapp.data.model.local.entity.PlantEntity
import com.learning.pestifyapp.ui.common.loadingFx
import com.learning.pestifyapp.ui.common.truncateText
import com.learning.pestifyapp.ui.theme.PestifyAppTheme

@Composable
fun PlantItem(
    item: PlantEntity,
    navigateToDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isClickable = remember { mutableStateOf(true) }
    Card(
        modifier = modifier
            .width(110.dp)
            .height(180.dp)
            .clickable(
                onClick = {
                    if (isClickable.value) {
                        isClickable.value = false
                        navigateToDetail(item.id)
                    }

                },
                interactionSource = remember { MutableInteractionSource() },
                indication = null

            ),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
        ),
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(
                    model = item.picture,
                    placeholder = painterResource(id = R.drawable.placeholder)
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Column {
                Text(
                    text = item.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Text(
                    text = truncateText(item.description, 50),
                    fontSize = 10.sp,
                    maxLines = 3,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 14.sp,
                    color = Color(0xFF555555),
                )
            }
        }
    }
}

@Composable
fun PlantItemLoading(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .width(110.dp)
            .height(180.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
        ),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .loadingFx()
            )
            Column {
                Box(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 2.dp)
                        .height(10.dp)
                        .fillMaxWidth(0.5f)
                        .loadingFx()
                )
                Box(
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .height(10.dp)
                        .fillMaxWidth(0.9f)
                        .loadingFx()
                )
                Box(
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .height(10.dp)
                        .fillMaxWidth(0.7f)
                        .loadingFx()
                )
            }
        }
    }
}

@Composable
fun PlantCategoryLoading(
    modifier: Modifier = Modifier

) {
    Column() {
        Box(
            modifier = modifier
                .padding(top = 4.dp, bottom = 8.dp)
                .height(20.dp)
                .width(50.dp)
                .loadingFx()
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 2.dp)
        )
        {
            items(5) {
                PlantItemLoading()
            }
        }
    }
}

@Composable
fun PlantCategory(
    plantList: List<PlantEntity>,
    navigateToDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    )
    {
        items(plantList, key = { it.id }) { plant ->
            PlantItem(
                item = plant,
                navigateToDetail = navigateToDetail,
            )
        }
    }
}

@Composable
fun ItemSection(
    title: String,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(bottom = 8.dp)
    ) {
        SectionText(title)
        content()
    }

}

@Composable
fun SectionText(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .padding(top = 2.dp, bottom = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun SectionTextPreview() {
    PestifyAppTheme {
        SectionText("Section Title")
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingPlantItemPreview() {
    PestifyAppTheme {
        Box(
            modifier = Modifier
                .background(Color.Gray)
        ) {
            PlantItemLoading(
                modifier = Modifier.padding(50.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlantCategoryLoadingPreview() {
    PestifyAppTheme {
        Box(
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxWidth()
        ) {
            PlantCategoryLoading()
        }
    }
}
