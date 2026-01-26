@file:Suppress("UnstableApiUsage")

package io.github.lyxnx.kradle.android.dsl

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ManagedVirtualDevice
import org.gradle.kotlin.dsl.get

/**
 * Simplified configuration for gradle managed devices
 *
 * @param configs device configs to add
 * @param configGroups an optional list of device groups to add. For example, there could be a group of devices for CI,
 * which only includes the devices from [configs] that run on a CI server
 */
public fun CommonExtension.configureManagedDevices(
    configs: List<ManagedDeviceConfig>,
    configGroups: List<ManagedDeviceGroupConfig> = emptyList()
) {
    testOptions.apply {
        managedDevices {
            allDevices.apply {
                configs.forEach { config ->
                    maybeCreate(config.taskName, ManagedVirtualDevice::class.java).apply {
                        device = config.device
                        apiLevel = config.apiLevel
                        systemImageSource = config.systemImageSource
                    }
                }
            }
            if (configGroups.isNotEmpty()) {
                groups.apply {
                    configGroups.forEach { group ->
                        maybeCreate(group.name).apply {
                            group.devices.forEach { config ->
                                targetDevices.add(allDevices[config.taskName])
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Represents a group of managed devices
 *
 * @property name name of the group
 * @property devices list of devices in the group
 */
public data class ManagedDeviceGroupConfig(val name: String, val devices: List<ManagedDeviceConfig>)

/**
 * Represents a gradle managed device
 *
 * @property device the device name
 * @property apiLevel the android api level of the device
 * @property systemImageSource the system image to use. Typically either `aosp` or `aosp-atd` for complete automated tests
 */
public data class ManagedDeviceConfig(
    val device: String,
    val apiLevel: Int,
    val systemImageSource: String
) {

    /**
     * The gradle task name for this device
     *
     * This will be in the format `device'api'apiLevelsystemImageSource`.
     * For example, a `ManagedDeviceConfig("Pixel 6", 31, "aosp")` will have a base task name of
     * `pixel6api31aosp`
     */
    val taskName: String = buildString {
        append(device.lowercase().replace(" ", ""))
        append("api")
        append(apiLevel.toString())
        append(systemImageSource.replace("-", ""))
    }
}
