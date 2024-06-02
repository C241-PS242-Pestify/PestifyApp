package com.learning.pestifyapp.ui.screen.dashboard.home

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.R
import com.learning.pestifyapp.ui.components.ItemSection
import com.learning.pestifyapp.ui.components.PlantCategory
import com.learning.pestifyapp.ui.screen.navigation.Graph
import com.learning.pestifyapp.ui.theme.PestifyAppTheme

@Composable
fun HomeScreen(
    context: Context,
    viewModel: HomeScreenViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    DisposableEffect(Unit) {
        val window = (context as Activity).window
        val originalStatusBarColor = window.statusBarColor

        window.statusBarColor = Color(0xFFB2DFDB).toArgb()

        onDispose {
            window.statusBarColor = originalStatusBarColor
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.onPrimary)
    ) {
        LazyColumn {
            item {
                TopSection(context = context)
            }
            item {
                ItemSection(
                    title = context.getString(R.string.menu_home),
                    content = { PlantCategory() }
                )
            }
        }
    }
}

//            Button(
//                onClick = {
//                    viewModel.logout()
//                    navController.navigate(Graph.LOGIN) {
//                        popUpTo(Graph.LOGIN) { inclusive = true }
//                    }
//                },
//                modifier = Modifier.padding(top = 16.dp)
//            ) {
//                Text(text = "Logout")
//            }


@Composable
fun TopSection(
    context: Context
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFB2DFDB),
                        Color(0xFFB2DFDB),
                        Color.Transparent
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sherlock_profile),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Welcome,",
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Sherlock!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF006d5b)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.aquaponics),
                    contentDescription = "Aquaponics",
                    modifier = Modifier.height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxHeight()
                        .width(250.dp)
                        .background(
                            Brush.horizontalGradient(
                                startX = 1f,
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.9f),
                                    Color.Black.copy(alpha = 0.8f),
                                    Color.Transparent,
                                )
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    Column (
                        modifier = Modifier
                            .width(200.dp)
                            .align(Alignment.CenterStart),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = context.getString(R.string.image_desc),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(
                                start = 12.dp,
                                bottom = 4.dp,
                                top = 12.dp,
                                end = 12.dp
                                )
                        )

                        Button(
                            onClick = { /* TODO */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0XFFFFC107)
                            ),
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .align(Alignment.Start),

                        ) {
                            Text(
                                text = "Read More",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(0.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopSectionPreview() {
    PestifyAppTheme {
        TopSection(context = MainActivity.CONTEXT)
    }

}
