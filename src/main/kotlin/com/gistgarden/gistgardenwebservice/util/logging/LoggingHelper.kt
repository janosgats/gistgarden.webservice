package com.gistgarden.gistgardenwebservice.util.logging

import java.io.PrintWriter
import java.io.StringWriter

object LoggingHelper {
    @JvmStatic
    fun getStackTraceAsString(throwable: Throwable): String {
        val stackTrace = StringWriter()
        throwable.printStackTrace(PrintWriter(stackTrace))
        return stackTrace.toString()
    }

    /**
     * Formats a dot separated (canonical) java class name and appends it to `logBuilder`.
     * <br></br><br></br>
     * **Formatting rules**:
     *
     *  * Leaves the last `leaveAsIsLevel` name elements as is.
     *  * Shortens the remaining package names to their first 3 characters.
     *
     *
     * @param loggerName     Fully qualified class name
     * @param leaveAsIsLevel Count of the last name elements to leave unshortened (`leaveAsIsLevel >= 0`)
     * @param logBuilder     StringBuilder to append logger name to
     */
    fun appendFormattedLoggerNameToStringBuilder(loggerName: String, leaveAsIsLevel: Int, logBuilder: StringBuilder) {
        val nameParts = loggerName.split(".")
        var i = 0
        while (i < nameParts.size - leaveAsIsLevel) {
            logBuilder.append(nameParts[i][0])
            if (nameParts[i].length >= 2) {
                logBuilder.append(nameParts[i][1])
            }
            if (nameParts[i].length >= 3) {
                logBuilder.append(nameParts[i][2])
            }
            if (i < nameParts.size - 1) {
                logBuilder.append(".")
            }
            ++i
        }

        while (i < nameParts.size - 1) {
            logBuilder
                .append(nameParts[i])
                .append(".")
            ++i
        }

        if (i < nameParts.size) {
            logBuilder.append(nameParts[i])
        }
    }
}