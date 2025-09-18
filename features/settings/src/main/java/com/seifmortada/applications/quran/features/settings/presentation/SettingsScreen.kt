package com.seifmortada.applications.quran.features.settings.presentation

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seifmortada.applications.quran.features.settings.domain.model.Language
import com.seifmortada.applications.quran.features.settings.domain.model.Theme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SettingsContract.Effect.ShowToast -> {
                    Toast.makeText(
                        context,
                        effect.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is SettingsContract.Effect.FeedbackSent -> {
                    Toast.makeText(
                        context,
                        "Feedback sent successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is SettingsContract.Effect.NavigateToEmail -> {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:")
                        putExtra(Intent.EXTRA_EMAIL, effect.emailData.recipients.toTypedArray())
                        putExtra(Intent.EXTRA_SUBJECT, effect.emailData.subject)
                        putExtra(Intent.EXTRA_TEXT, effect.emailData.body)
                    }
                    context.startActivity(Intent.createChooser(intent, "Send Feedback"))
                }
            }
        }
    }

    SettingsScreen(
        state = state,
        onIntent = viewModel::handleIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsContract.State,
    onIntent: (SettingsContract.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Start
            )
        }

        SettingsSection(
            title = "Appearance",
            icon = Icons.Default.Palette
        ) {
            ThemeSelector(
                currentTheme = state.theme,
                onThemeChange = { onIntent(SettingsContract.Intent.UpdateTheme(it)) }
            )
        }

        SettingsSection(
            title = "Language",
            icon = Icons.Default.Language
        ) {
            LanguageSelector(
                currentLanguage = state.language,
                onLanguageChange = { onIntent(SettingsContract.Intent.UpdateLanguage(it)) }
            )
        }

        SettingsSection(
            title = "Feedback & Suggestions",
            icon = Icons.Default.Feedback
        ) {
            FeedbackSection(
                onFeedbackClick = { onIntent(SettingsContract.Intent.ShowFeedbackDialog) }
            )
        }

        SettingsSection(
            title = "About",
            icon = Icons.Default.Info
        ) {
            AboutSection()
        }
    }

    if (state.showFeedbackDialog) {
        FeedbackDialog(
            onDismiss = { onIntent(SettingsContract.Intent.HideFeedbackDialog) },
            onSendFeedback = { onIntent(SettingsContract.Intent.SendFeedback(it)) }
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
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            FilterChip(
                onClick = { onThemeChange(Theme.LIGHT) },
                label = {
                    Text(
                        text = "Light Theme"
                    )
                },
                selected = currentTheme == Theme.LIGHT,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LightMode,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier.weight(1f)
            )

            FilterChip(
                onClick = { onThemeChange(Theme.DARK) },
                label = {
                    Text(
                        text = "Dark Theme"
                    )
                },
                selected = currentTheme == Theme.DARK,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DarkMode,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier.weight(1f)
            )
        }


        FilterChip(
            onClick = { onThemeChange(Theme.SYSTEM) },
            label = {
                Text(
                    text = "System Theme"
                )
            },
            selected = currentTheme == Theme.SYSTEM,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.SettingsBrightness,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
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
private fun FeedbackSection(
    onFeedbackClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Your feedback and suggestions are greatly appreciated.",
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
            Text("Send Feedback")
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

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Send Feedback",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column {
                Text(
                    text = "Please enter your feedback and suggestions.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = feedbackText,
                    onValueChange = { feedbackText = it },
                    label = { Text("Your Feedback") },
                    placeholder = { Text("Enter your feedback") },
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
                        onSendFeedback(feedbackText)
                        onDismiss()
                    }
                },
                enabled = feedbackText.isNotBlank()
            ) {
                Text("Send")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun AboutSection() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Quran App - Version 1.0.0",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "A beautiful and comprehensive Quran reading and listening app.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    MaterialTheme {
        val state = SettingsContract.State(
            theme = Theme.SYSTEM,
            language = Language.ENGLISH,
            isLoading = false,
            showFeedbackDialog = false
        )
        SettingsScreen(
            state = state,
            onIntent = {}
        )
    }
}
