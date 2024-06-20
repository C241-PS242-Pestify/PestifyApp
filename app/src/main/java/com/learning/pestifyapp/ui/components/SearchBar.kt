package com.learning.pestifyapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.pestifyapp.R
import com.learning.pestifyapp.ui.theme.PestifyAppTheme
import com.learning.pestifyapp.ui.theme.base60
import com.learning.pestifyapp.ui.theme.base80
import com.learning.pestifyapp.ui.theme.searchStroke

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp)
            .border(1.dp, searchStroke, CustomSearchBarShape) // Border pada kotak
    ) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = {
                onSearch(it)
            },
            active = false,
            onActiveChange = {},
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = null,
                    tint = base80,
                    modifier = Modifier.size(30.dp)
                )
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.search),
                    color = base80,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            },
            shape = CustomSearchBarShape,
            colors = SearchBarDefaults.colors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ){}
    }
}
val CustomSearchBarShape = RoundedCornerShape(12.dp)

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    PestifyAppTheme {
        CustomSearchBar(query = "Search", onQueryChange = {}, onSearch = {})
    }
}