package com.learning.pestifyapp.ui.screen.dashboard.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.learning.pestifyapp.MainActivity
import com.learning.pestifyapp.R
import com.learning.pestifyapp.ui.components.CustomButton


@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel,
    navController: NavHostController,
    context: MainActivity,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding()
        )
        AccountSection(
            modifier = Modifier.fillMaxWidth()
        )

        CustomButton(
            text = "Logout", onClick = {
                navController.popBackStack()
                viewModel.logout()
                Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show()
                navController.navigate("login")
            }, modifier = Modifier.padding(bottom = 8.dp)
        )


    }
}

@Composable
fun TopSection(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 20.dp)
                    .fillMaxWidth()

            ) {
                Column {
                    Text(
                        text = "Sherlock",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF006d5b)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Image(
                    alignment = Alignment.TopEnd,
                    painter = painterResource(id = R.drawable.sherlock_profile),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)

                )

            }
        }
    }
}

@Composable
fun AccountSection(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Column {
                Text(
                    text = "My Account",
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Change Profile Picture!",
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color = Color.Black
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Arrow Icon",
                        tint = Color.Black
                    )
                }
                Divider(color = Color.Gray, thickness = 1.dp)
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Privacy",
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color = Color.Black
                    )
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Arrow Icon",
                        tint = Color.Black
                    )
                }
                Divider(color = Color.Gray, thickness = 1.dp)
            }

            Text(
                text = "My Account",
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Row(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 16.dp)
                    .fillMaxWidth()
                    .clickable {

                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Change Profile Picture!",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = Color.Black
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Arrow Icon",
                    tint = Color.Black
                )
            }
            Divider(color = Color.Gray, thickness = 1.dp)
            Row(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 16.dp)
                    .fillMaxWidth()
                    .clickable {

                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Privacy",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = Color.Black
                )
                Icon(
                    modifier = Modifier,
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Arrow Icon",
                    tint = Color.Black
                )
            }
            Divider(color = Color.Gray, thickness = 1.dp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopSectionPrev() {
    AccountSection()


}