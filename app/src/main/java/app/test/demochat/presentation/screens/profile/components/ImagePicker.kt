package app.test.demochat.presentation.screens.profile.components

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import app.test.demochat.utils.asImageBitmap

@Composable
fun ImagePicker(
    currentAvatar: String?,
    onImageSelected: (filename: String, base64: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showImagePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val contentResolver = context.contentResolver
            val filename = getFileName(contentResolver, uri)

            val inputStream = contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()

            if (bytes != null) {
                val base64 = Base64.encodeToString(bytes, Base64.DEFAULT)
                onImageSelected(filename, base64)
            }
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { showImagePicker = true }
        ) {
            if (!currentAvatar.isNullOrBlank()) {
                Image(
                    bitmap = currentAvatar.asImageBitmap(),
                    contentDescription = "Avatar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Select avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color.Black.copy(alpha = 0.3f),
                        shape = CircleShape
                    )
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Change avatar",
                    tint = Color.White
                )
            }
        }
    }

    if (showImagePicker) {
        launcher.launch("image/*")
        showImagePicker = false
    }
}

private fun getFileName(contentResolver: ContentResolver, uri: Uri): String {
    var filename = ""
    val cursor = contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val displayName = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (displayName != -1) {
                filename = it.getString(displayName)
            }
        }
    }
    return filename
}