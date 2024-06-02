package com.learning.pestifyapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.pestifyapp.data.model.plant.FakePlantData
import com.learning.pestifyapp.data.model.plant.PlantData
import com.learning.pestifyapp.ui.common.truncateText
import com.learning.pestifyapp.ui.theme.PestifyAppTheme

@Composable
fun PlantItem(
    item : PlantData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(120.dp)
            .height(160.dp),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
        ),
    ){
        Column {
            Image (
                painter = painterResource(item.image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Column {
                Text(
                    text = item.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Text(
                    text = truncateText(item.description, 60),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 14.sp,
                    color = Color(0xFF555555),
                )
            }
        }
    }
}

@Composable
fun PlantCategory(
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    )
    {
        items(FakePlantData.dummyPlants, key = { it.id }) { plant ->
            PlantItem(plant)
        }
    }
}

@Composable
fun ItemSection(
    title: String,
    content :@Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
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
fun SectionTextPreview () {
    PestifyAppTheme {
        SectionText("Section Title")
    }
}

@Preview (showBackground = true)
@Composable
private fun PlantItemPreview() {
    PestifyAppTheme {
        PlantItem(
            FakePlantData.dummyPlants[0],
            modifier = Modifier.padding(8.dp)
        )
    }
}