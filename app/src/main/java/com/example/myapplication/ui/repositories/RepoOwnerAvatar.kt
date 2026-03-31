package com.example.myapplication.ui.repositories

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent

/** Square (or other) owner logo: rounded square, white border, Coil or code fallback. */
@Composable
internal fun RepoOwnerAvatar(
    ownerAvatarUrl: String?,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    val tint = MaterialTheme.colorScheme.onPrimaryContainer
    val container = MaterialTheme.colorScheme.primaryContainer
    val url = ownerAvatarUrl?.takeIf { it.isNotBlank() }
    val shape = RepositoriesDimens.AvatarCornerShape
    Surface(
        modifier = modifier.size(RepositoriesDimens.AvatarSize),
        shape = shape,
        color = container,
        border = BorderStroke(RepositoriesDimens.AvatarBorderWidth, Color.White),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape),
            contentAlignment = Alignment.Center,
        ) {
            if (url == null) {
                Icon(
                    imageVector = Icons.Outlined.Code,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(RepositoriesDimens.AvatarFallbackIconSize),
                    tint = tint,
                )
            } else {
                SubcomposeAsyncImage(
                    model = url,
                    contentDescription = contentDescription,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                ) {
                    when (painter.state) {
                        is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
                        is AsyncImagePainter.State.Loading -> Icon(
                            imageVector = Icons.Outlined.Code,
                            contentDescription = contentDescription,
                            modifier = Modifier.size(RepositoriesDimens.AvatarFallbackIconSize),
                            tint = tint.copy(alpha = 0.35f),
                        )
                        is AsyncImagePainter.State.Error -> Icon(
                            imageVector = Icons.Outlined.Code,
                            contentDescription = contentDescription,
                            modifier = Modifier.size(RepositoriesDimens.AvatarFallbackIconSize),
                            tint = tint,
                        )
                        else -> Icon(
                            imageVector = Icons.Outlined.Code,
                            contentDescription = contentDescription,
                            modifier = Modifier.size(RepositoriesDimens.AvatarFallbackIconSize),
                            tint = tint,
                        )
                    }
                }
            }
        }
    }
}
