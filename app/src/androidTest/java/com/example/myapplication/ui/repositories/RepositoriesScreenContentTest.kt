package com.example.myapplication.ui.repositories

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.ui.theme.MyApplicationTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoriesScreenContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loading_showsProgressIndicator() {
        composeRule.setContent {
            MyApplicationTheme {
                RepositoriesScreenContent(
                    state = RepositoriesUiState.Loading,
                    onRetry = {},
                )
            }
        }
        composeRule.onNodeWithTag(TAG_LOADING).assertIsDisplayed()
    }

    @Test
    fun error_showsRetryAction() {
        composeRule.setContent {
            MyApplicationTheme {
                RepositoriesScreenContent(
                    state = RepositoriesUiState.Error("Failed", canRetry = true),
                    onRetry = {},
                )
            }
        }
        composeRule.onNodeWithTag(TAG_ERROR).assertIsDisplayed()
    }
}
