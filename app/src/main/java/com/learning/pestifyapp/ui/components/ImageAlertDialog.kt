import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberImagePainter

@Composable
fun ImageAlertDialog(
    title: String,
    message: String,
    imageUri: Uri,
    onYesClick: () -> Unit,
    onNoClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = title)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = imageUri,
                        builder = {
                            // Konfigurasi placeholder dan error jika gambar gagal dimuat
                        }
                    ),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = message)
            }
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
