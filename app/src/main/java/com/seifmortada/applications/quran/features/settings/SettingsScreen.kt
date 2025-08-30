package com.seifmortada.applications.quran.features.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seifmortada.applications.quran.R
import com.seifmortada.applications.quran.core.ui.theme.QuranAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    SettingsScreen(
        uiState = uiState,
        onThemeChange = viewModel::updateTheme,
        onLanguageChange = viewModel::updateLanguage,
        onSendFeedback = viewModel::sendFeedback
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onThemeChange: (Theme) -> Unit,
    onLanguageChange: (Language) -> Unit,
    onSendFeedback: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showFeedbackDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header with logo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.quran_app_logo),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier
                    .size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.settings),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Start
            )
        }

        // Theme Settings Section
        SettingsSection(
            title = stringResource(R.string.appearance),
            icon = Icons.Default.Palette
        ) {
            ThemeSelector(
                currentTheme = uiState.theme,
                onThemeChange = onThemeChange
            )
        }

        // Language Settings Section
        SettingsSection(
            title = stringResource(R.string.language),
            icon = Icons.Default.Language
        ) {
            LanguageSelector(
                currentLanguage = uiState.language,
                onLanguageChange = onLanguageChange
            )
        }

        // Support Section
        SettingsSection(
            title = stringResource(R.string.support_developer),
            icon = Icons.Default.Favorite
        ) {
            SupportSection(
                onSupportClick = {
                    // Navigate to support/payment screen (placeholder for future implementation)
                    val intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://your-support-link.com"))
                    context.startActivity(intent)
                }
            )
        }

        // Feedback Section
        SettingsSection(
            title = stringResource(R.string.feedback_and_suggestions),
            icon = Icons.Default.Feedback
        ) {
            FeedbackSection(
                onFeedbackClick = { showFeedbackDialog = true }
            )
        }
    }

    // Feedback Dialog
    if (showFeedbackDialog) {
        FeedbackDialog(
            onDismiss = { showFeedbackDialog = false },
            onSendFeedback = { feedback ->
                onSendFeedback(feedback)
                showFeedbackDialog = false
            }
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            content()
        }
    }
}

@Composable
private fun ThemeSelector(
    currentTheme: Theme,
    onThemeChange: (Theme) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Theme.values().forEach { theme ->
            FilterChip(
                onClick = { onThemeChange(theme) },
                label = {
                    Text(
                        text = when (theme) {
                            Theme.LIGHT -> stringResource(R.string.light_theme)
                            Theme.DARK -> stringResource(R.string.dark_theme)
                            Theme.SYSTEM -> stringResource(R.string.system_theme)
                        }
                    )
                },
                selected = currentTheme == theme,
                leadingIcon = {
                    Icon(
                        imageVector = when (theme) {
                            Theme.LIGHT -> Icons.Default.LightMode
                            Theme.DARK -> Icons.Default.DarkMode
                            Theme.SYSTEM -> Icons.Default.SettingsBrightness
                        },
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            )
        }
    }
}

@Composable
private fun LanguageSelector(
    currentLanguage: Language,
    onLanguageChange: (Language) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Language.values().forEach { language ->
            FilterChip(
                onClick = { onLanguageChange(language) },
                label = {
                    Text(
                        text = when (language) {
                            Language.ENGLISH -> "English"
                            Language.ARABIC -> "العربية"
                        }
                    )
                },
                selected = currentLanguage == language,
                leadingIcon = {
                    Text(
                        text = when (language) {
                            Language.ENGLISH -> "🇺🇸"
                            Language.ARABIC -> "🇸🇦"
                        },
                        fontSize = 16.sp
                    )
                }
            )
        }
    }
}

@Composable
private fun SupportSection(
    onSupportClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.support_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedButton(
            onClick = onSupportClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.support_us))
        }
    }
}

@Composable
private fun FeedbackSection(
    onFeedbackClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.feedback_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedButton(
            onClick = onFeedbackClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.send_feedback))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeedbackDialog(
    onDismiss: () -> Unit,
    onSendFeedback: (String) -> Unit
) {
    var feedbackText by remember { mutableStateOf("") }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.send_feedback),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column {
                Text(
                    text = stringResource(R.string.feedback_dialog_description),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = feedbackText,
                    onValueChange = { feedbackText = it },
                    label = { Text(stringResource(R.string.your_feedback)) },
                    placeholder = { Text(stringResource(R.string.feedback_placeholder)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    maxLines = 6
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (feedbackText.isNotBlank()) {
                        // Send email with feedback
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:")
                            putExtra(Intent.EXTRA_EMAIL, arrayOf("developer@quranapp.com"))
                            putExtra(Intent.EXTRA_SUBJECT, "Quran App Feedback")
                            putExtra(Intent.EXTRA_TEXT, feedbackText)
                        }
                        context.startActivity(Intent.createChooser(intent, "Send Feedback"))
                        onSendFeedback(feedbackText)
                    }
                },
                enabled = feedbackText.isNotBlank()
            ) {
                Text(stringResource(R.string.send))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

// Data Classes and Enums
data class SettingsUiState(
    val theme: Theme = Theme.SYSTEM,
    val language: Language = Language.ENGLISH,
    val isLoading: Boolean = false
)

enum class Theme {
    LIGHT, DARK, SYSTEM
}

enum class Language {
    ENGLISH, ARABIC
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    QuranAppTheme {
        SettingsScreen(
            uiState = SettingsUiState(),
            onThemeChange = {},
            onLanguageChange = {},
            onSendFeedback = {}
        )
    }
}