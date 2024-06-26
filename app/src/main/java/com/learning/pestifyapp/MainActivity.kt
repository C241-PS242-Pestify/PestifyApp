package com.learning.pestifyapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.learning.pestifyapp.data.repository.UserRepository
import com.learning.pestifyapp.ui.theme.PestifyAppTheme

class MainActivity : ComponentActivity() {
    private lateinit var userRepository: UserRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CONTEXT = this
        userRepository = UserRepository(context = this)
        setContent {
            PestifyAppTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PestifyApp(context = this)
                }
            }
        }
    }

    companion object {
        lateinit var CONTEXT : MainActivity
    }
}

