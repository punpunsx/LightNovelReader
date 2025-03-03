package indi.dmzz_yyhyy.lightnovelreader.ui.home.settings.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import indi.dmzz_yyhyy.lightnovelreader.BuildConfig
import indi.dmzz_yyhyy.lightnovelreader.R
import indi.dmzz_yyhyy.lightnovelreader.ui.components.SettingsClickableEntry
import indi.dmzz_yyhyy.lightnovelreader.ui.components.SettingsSwitchEntry
import indi.dmzz_yyhyy.lightnovelreader.ui.home.settings.SettingState

@Composable
fun AboutSettingsList(
    settingState: SettingState) {
    val appInfo: String = buildString {
        appendLine(BuildConfig.APPLICATION_ID)
        append(BuildConfig.VERSION_NAME).append(" (").append(BuildConfig.VERSION_CODE).append(")")
    }

    val buildInfo: String = buildString {
        appendLine(stringResource(R.string.info_build_date))
        appendLine(stringResource(R.string.info_build_host))
        append(if (BuildConfig.DEBUG) "DEV (DEBUG)" else "RELEASE")
    }
    SettingsClickableEntry(
        title = stringResource(R.string.app_name),
        description = appInfo,
        openUrl = if (settingState.updateChannelKey == "Development") {
            "https://install.appcenter.ms/users/nightfish2009/apps/lightnovelreader/distribution_groups/development"
        } else {
            "https://install.appcenter.ms/users/nightfish2009/apps/lightnovelreader/distribution_groups/release"
        }
    )
    SettingsClickableEntry(
        title = stringResource(R.string.settings_app_build),
        description = buildInfo,
    )
    SettingsClickableEntry(
        title = stringResource(R.string.settings_github_repo),
        description = stringResource(R.string.settings_github_repo_desc),
        openUrl = "https://github.com/dmzz-yyhyy/LightNovelReader"
    )
    SettingsClickableEntry(
        title = stringResource(R.string.settings_communication),
        description = stringResource(R.string.settings_communication_desc),
        openUrl = "https://qm.qq.com/q/Tp80Hf9Oms"
    )
    SettingsSwitchEntry(
        title = stringResource(R.string.settings_statistics),
        description = stringResource(R.string.settings_statistics_desc),
        checked = if (BuildConfig.DEBUG) false else settingState.statistics,
        booleanUserData = settingState.statisticsUserData,
        disabled = BuildConfig.DEBUG
    )
}