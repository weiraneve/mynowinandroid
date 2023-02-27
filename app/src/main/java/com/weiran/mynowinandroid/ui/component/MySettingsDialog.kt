package com.weiran.mynowinandroid.ui.component

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.core.content.ContextCompat
import com.weiran.mynowinandroid.R
import com.weiran.mynowinandroid.modules.home.HomeAction
import com.weiran.mynowinandroid.modules.home.SettingThemeState
import com.weiran.mynowinandroid.ui.theme.Dimensions

@Composable
fun MySettingsDialog(
    themeState: SettingThemeState,
    dispatchAction: (action: HomeAction) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Divider()
            Column(Modifier.verticalScroll(rememberScrollState())) {
                SettingsPanel(
                    themeState = themeState,
                    onChangeSettingDefaultTheme = { dispatchAction.invoke(HomeAction.SettingDefaultThemeChange) },
                    onChangeSettingAndroidTheme = { dispatchAction.invoke(HomeAction.SettingAndroidThemeChange) }
                )
                Divider(Modifier.padding(top = Dimensions.dimension8))
                LinksPanel()
            }
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.dismiss_dialog_button_text),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = Dimensions.dimension8)
                    .clickable { onDismiss() }
            )
        }
    )
}

@Composable
private fun SettingsPanel(
    themeState: SettingThemeState,
    onChangeSettingDefaultTheme: () -> Unit,
    onChangeSettingAndroidTheme: () -> Unit
) {
    SettingsDialogSectionTitle(text = stringResource(R.string.theme))
    Column(Modifier.selectableGroup()) {
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.brand_default),
            selected = themeState == SettingThemeState.DefaultThemeState,
            onClick = onChangeSettingDefaultTheme
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.brand_android),
            selected = themeState == SettingThemeState.AndroidThemeState,
            onClick = onChangeSettingAndroidTheme
        )
    }
    SettingsDialogSectionTitle(text = stringResource(R.string.dark_mode_preference))
    Column(Modifier.selectableGroup()) {
        SettingsDialogThemeChooserRow(text = stringResource(R.string.dark_mode_config_system_default))
        SettingsDialogThemeChooserRow(text = stringResource(R.string.dark_mode_config_light))
        SettingsDialogThemeChooserRow(text = stringResource(R.string.dark_mode_config_dark))
    }
}

@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(
            top = Dimensions.standardSpacing,
            bottom = Dimensions.dimension8
        )
    )
}

@Composable
fun SettingsDialogThemeChooserRow(
    text: String,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val flag = remember { mutableStateOf(selected) }
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = flag.value,
                role = Role.RadioButton,
                onClick = onClick
            )
            .padding(Dimensions.dimension8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(Modifier.width(Dimensions.dimension8))
        Text(text)
    }
}

@Composable
private fun LinksPanel() {
    Row(modifier = Modifier.padding(top = Dimensions.standardSpacing)) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row {
                TextLink(
                    text = stringResource(R.string.privacy_policy),
                    url = PRIVACY_POLICY_URL
                )
                Spacer(Modifier.width(Dimensions.standardSpacing))
                TextLink(
                    text = stringResource(R.string.licenses),
                    url = LICENSES_URL
                )
            }
            Spacer(Modifier.height(Dimensions.standardSpacing))
            Row {
                TextLink(
                    text = stringResource(R.string.brand_guidelines),
                    url = BRAND_GUIDELINES_URL
                )
                Spacer(Modifier.width(Dimensions.standardSpacing))
                TextLink(
                    text = stringResource(R.string.feedback),
                    url = FEEDBACK_URL
                )
            }
        }
    }
}

@Composable
private fun TextLink(text: String, url: String) {

    val launchResourceIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    val context = LocalContext.current

    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .clickable {
                ContextCompat.startActivity(context, launchResourceIntent, null)
            }
    )
}

private const val PRIVACY_POLICY_URL = "https://policies.google.com/privacy"
private const val LICENSES_URL =
    "https://github.com/android/nowinandroid/blob/main/app/LICENSES.md#open-source-licenses-and-copyright-notices"
private const val BRAND_GUIDELINES_URL =
    "https://developer.android.com/distribute/marketing-tools/brand-guidelines"
private const val FEEDBACK_URL = "https://goo.gle/nia-app-feedback"
