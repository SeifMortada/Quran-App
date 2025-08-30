package com.seifmortada.applications.quran.core.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.seifmortada.applications.quran.core.permissions.PermissionDialog
import com.seifmortada.applications.quran.core.permissions.PermissionManager
import com.seifmortada.applications.quran.core.ui.locale.LocaleProvider
import com.seifmortada.applications.quran.core.ui.theme.QuranAppThemeProvider

class MainActivity : ComponentActivity() {
    private lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionManager = PermissionManager(this)

        setContent {
            LocaleProvider {
                QuranAppThemeProvider {
                    MainContent()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Check permissions when activity resumes
        permissionManager.checkAndRequestPermissions()
    }

    @Composable
    private fun MainContent() {
        val permissionsGranted by permissionManager.permissionsGranted
        val showPermissionDialog by permissionManager.showPermissionDialog

        if (permissionsGranted) {
            QuranApp()
        } else {
            LaunchedEffect(Unit) {
                permissionManager.checkAndRequestPermissions()
            }
        }

        if (showPermissionDialog) {
            PermissionDialog(
                onDismiss = {
                    permissionManager.dismissPermissionDialog()
                },
                onGrantPermissions = {
                    permissionManager.dismissPermissionDialog()
                    permissionManager.checkAndRequestPermissions()
                }
            )
        }
    }
}