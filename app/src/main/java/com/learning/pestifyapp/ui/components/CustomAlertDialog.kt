package com.learning.pestifyapp.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties

@Composable
fun CustomAlertDialog(
    title: String,
    message: String,
    onYesClick: () -> Unit,
    onNoClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            Button(onClick = { onYesClick() }) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(onClick = { onNoClick() }) {
                Text("No")
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    )
}
