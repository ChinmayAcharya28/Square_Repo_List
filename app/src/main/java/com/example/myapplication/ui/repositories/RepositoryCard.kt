package com.example.myapplication.ui.repositories

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

/**
 * One repo row: title, description, metadata (language, license, stars, forks), optional last-activity line.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RepositoryCard(
    repo: RepoUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val descriptionText = repo.description.ifBlank {
        stringResource(R.string.repo_no_description)
    }
    val isPlaceholderDescription = repo.description.isBlank()

    ElevatedCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RepositoriesDimens.CardCornerRadius),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp,
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = RepositoriesDimens.CardPaddingHorizontal,
                    vertical = RepositoriesDimens.CardPaddingVertical,
                ),
            verticalAlignment = Alignment.Top,
        ) {
            RepoOwnerAvatar(
                ownerAvatarUrl = repo.ownerAvatarUrl,
                contentDescription = stringResource(R.string.repo_owner_avatar, repo.name),
            )
            Spacer(Modifier.width(RepositoriesDimens.ContentSpacingAfterAvatar))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = repo.name,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(RepositoriesDimens.TitleToDescriptionSpacing))
                Text(
                    text = descriptionText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2f,
                    ),
                    color = if (isPlaceholderDescription) {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                val langPart = repo.language?.trim()?.takeIf { it.isNotEmpty() }
                val licPart = repo.licenseSpdxId?.trim()?.takeIf { it.isNotEmpty() }
                val updatedLabel = repo.updatedAtDisplay
                Spacer(Modifier.height(RepositoriesDimens.DescriptionToMetaSpacing))
                val metaColor = MaterialTheme.colorScheme.onSurfaceVariant
                val metaStyle = MaterialTheme.typography.labelLarge
                val activityStyle = MaterialTheme.typography.labelMedium
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (langPart != null) {
                            Image(
                                painter = painterResource(R.drawable.ic_language_globe),
                                contentDescription = stringResource(R.string.repo_language_icon_content_description),
                                modifier = Modifier.size(RepositoriesDimens.MetaIconSize),
                                colorFilter = ColorFilter.tint(metaColor),
                            )
                            Spacer(Modifier.width(RepositoriesDimens.MetaIconToTextSpacing))
                            Text(
                                text = stringResource(R.string.repo_language_line, langPart),
                                style = metaStyle,
                                color = metaColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        if (langPart != null && licPart != null) {
                            Text(
                                text = RepositoriesDimens.LanguageToLicenseGapText,
                                style = metaStyle,
                                color = metaColor,
                            )
                        }
                        if (licPart != null) {
                            Image(
                                painter = painterResource(R.drawable.ic_license_balance),
                                contentDescription = stringResource(R.string.repo_license_icon_content_description),
                                modifier = Modifier.size(RepositoriesDimens.MetaIconSize),
                                colorFilter = ColorFilter.tint(metaColor),
                            )
                            Spacer(Modifier.width(RepositoriesDimens.MetaIconToTextSpacing))
                            Text(
                                text = licPart,
                                style = metaStyle,
                                color = metaColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.widthIn(max = RepositoriesDimens.LicenseSpdxMaxWidth),
                            )
                        }
                        if (langPart != null || licPart != null) {
                            Spacer(Modifier.width(RepositoriesDimens.MetaSegmentSpacing))
                        }
                        Image(
                            painter = painterResource(R.drawable.ic_star_outline),
                            contentDescription = stringResource(R.string.repo_stars_icon_content_description),
                            modifier = Modifier.size(RepositoriesDimens.MetaIconSize),
                            colorFilter = ColorFilter.tint(metaColor),
                        )
                        Spacer(Modifier.width(RepositoriesDimens.MetaIconToTextSpacing))
                        Text(
                            text = repo.stargazersCount.toString(),
                            style = metaStyle,
                            color = metaColor,
                            maxLines = 1,
                        )
                        Spacer(Modifier.width(RepositoriesDimens.MetaSegmentSpacing))
                        Image(
                            painter = painterResource(R.drawable.ic_fork),
                            contentDescription = stringResource(R.string.repo_forks_icon_content_description),
                            modifier = Modifier.size(RepositoriesDimens.MetaIconSize),
                            colorFilter = ColorFilter.tint(metaColor),
                        )
                        Spacer(Modifier.width(RepositoriesDimens.MetaIconToTextSpacing))
                        Text(
                            text = repo.forksCount.toString(),
                            style = metaStyle,
                            color = metaColor,
                            maxLines = 1,
                        )
                    }
                    if (!updatedLabel.isNullOrBlank()) {
                        Spacer(Modifier.height(RepositoriesDimens.LastActivityTopSpacing))
                        Text(
                            text = stringResource(R.string.repo_last_activity_line, updatedLabel),
                            style = activityStyle,
                            color = metaColor,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}
