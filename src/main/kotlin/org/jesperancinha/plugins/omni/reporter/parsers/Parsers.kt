package org.jesperancinha.plugins.omni.reporter.parsers

import org.jesperancinha.plugins.omni.reporter.domain.jacoco.Line
import java.security.MessageDigest

val messageDigester: MessageDigest = MessageDigest.getInstance("MD5")

internal fun isBranch(it: Line) =
    it.mb > 0 || it.cb > 0

internal val String.toFileDigest: String
    get() = messageDigester.digest(toByteArray())
        .joinToString(separator = "") { byte -> "%02x".format(byte) }
        .uppercase()

