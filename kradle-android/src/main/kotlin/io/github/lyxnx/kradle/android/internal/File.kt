package io.github.lyxnx.kradle.android.internal

import org.gradle.api.file.RegularFile
import java.io.File

internal fun <T> RegularFile.ifExists(block: (File) -> T): T? {
    if (asFile.exists()) {
        return block(asFile)
    }

    return null
}
