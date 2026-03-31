package com.example.myapplication.ui.repositories

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

// Central place for card/list spacing so previews and device match without hunting literals.
object RepositoriesDimens {
    val CardCornerRadius = 20.dp
    val CardPaddingHorizontal = 18.dp
    val CardPaddingVertical = 16.dp
    val ListHorizontalPadding = 16.dp
    val ListTopPadding = 8.dp
    val ListBottomPadding = 24.dp
    val CardSpacing = 12.dp

    val AvatarSize = 48.dp
    val AvatarCornerShape = RoundedCornerShape(10.dp)
    val AvatarBorderWidth = 1.5.dp
    val AvatarFallbackIconSize = 26.dp

    val ContentSpacingAfterAvatar = 16.dp
    val TitleToDescriptionSpacing = 8.dp
    val DescriptionToMetaSpacing = 6.dp

    val MetaIconSize = 11.dp
    val MetaIconToTextSpacing = 4.dp
    val MetaSegmentSpacing = 8.dp
    val LanguageToLicenseGapText = "  "
    val LicenseSpdxMaxWidth = 200.dp
    val LastActivityTopSpacing = 4.dp

    val LoadingIndicatorSize = 48.dp
    val LoadingIndicatorStroke = 3.dp
    val EmptyStateIconSize = 56.dp
    val ErrorStateIconSize = 52.dp
    val EmptyErrorPadding = 32.dp
    val LoadingVerticalSpacing = 20.dp
    val EmptyVerticalSpacing = 16.dp
}
