package com.seifmortada.applications.quran.features.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seifmortada.applications.quran.R
import com.seifmortada.applications.quran.core.ui.theme.QuranAppTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    var startAnimation by remember { mutableStateOf(false) }

    // Animation values
    val scaleAnimation by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scaleAnimation"
    )

    val alphaAnimation by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = EaseInOut
        ),
        label = "alphaAnimation"
    )

    // Start animation when composable is first created
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2500) // Show splash for 2.5 seconds
        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .alpha(alphaAnimation)
                .scale(scaleAnimation)
        ) {
            // App Logo
            Image(
                painter = painterResource(id = R.drawable.quran_app_logo),
                contentDescription = "Quran App Logo",
                modifier = Modifier
                    .size(200.dp)
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // App Name
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                ),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // App Subtitle/Tagline
            Text(
                text = stringResource(R.string.app_subtitle),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Loading indicator
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp
            )
        }

        // Islamic/Arabic decorative text at bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 48.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.alpha(alphaAnimation)
            )
        }
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    QuranAppTheme {
        SplashScreen(
            onSplashFinished = { }
        )
    }
}
