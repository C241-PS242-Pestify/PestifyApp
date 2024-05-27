package com.learning.pestifyapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.learning.pestifyapp.R
import com.learning.pestifyapp.ui.screen.navigation.NavigationItem
import com.learning.pestifyapp.ui.screen.navigation.Screen
import com.learning.pestifyapp.ui.screen.navigation.navigationItems
import com.learning.pestifyapp.ui.theme.iconLight

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .drawWithContent {
                drawContent()
                drawLine(
                    color = Color.LightGray,
                    start = Offset.Zero,
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            navigationItems.forEach { item ->
                BottomNavBarItem(

                    item = item,
                    isSelected = navController.currentDestination?.route == item.screen.route,
                    onClick = {
                        navController.navigate(item.screen.route) {
                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }

        }
    }

}

@Composable
fun BottomNavBarItem(
    modifier: Modifier = Modifier,
    item: NavigationItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val icon = if (isSelected) item.iconSelected else item.icon

    Column (
        modifier = modifier
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Icon(
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = item.title,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else iconLight,
                modifier = Modifier.size(30.dp)
            )

            Text(
                text = item.title,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                color = if (isSelected) MaterialTheme.colorScheme.primary else iconLight
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun PrevButtonItem() {
    BottomNavBarItem(
        item = NavigationItem(
            title = "Home",
            icon = R.drawable.home_non,
            iconSelected = R.drawable.home_filled,
            screen = Screen.Home,

            ),
        isSelected = false,
        onClick = {}
    )
}