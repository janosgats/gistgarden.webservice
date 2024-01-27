package com.gistgarden.gistgardenwebservice.util.logging

import org.apache.logging.log4j.core.LogEvent
import org.apache.logging.log4j.core.config.Configuration
import org.apache.logging.log4j.core.config.plugins.Plugin
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory
import org.apache.logging.log4j.core.layout.AbstractStringLayout
import java.nio.charset.Charset
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val UTF_8 = Charset.forName("UTF-8")
private const val DEFAULT_LOGGER_NAME_LEAVE_AS_IS_LEVEL: Int = 2

private val DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").withZone(ZoneId.systemDefault());

/**
 * Layout to be used in localdev
 */
@Plugin(name = "LocaldevLayout", category = "Core", elementType = "layout", printObject = true)
class LocaldevLayout private constructor(
    /**
     * log4j2 configuration
     */
    config: Configuration,
    /**
     * loggerName shortening setting
     */
    private val loggerNameLeaveAsIsLevel: Int,
    /**
     * Print MDC onto console
     */
    private val printContextData: Boolean
) : AbstractStringLayout(config, UTF_8, null, null) {

    companion object {
        @JvmStatic
        @PluginBuilderFactory
        fun newBuilder(): Builder {
            return Builder().asBuilder()!!
        }
    }

    override fun toSerializable(event: LogEvent): String {
        val stringBuilder = getStringBuilder()

        val timeCodeString: String = getFormattedDateTimeStringFromLogEvent(event);

        stringBuilder
            .append(timeCodeString)
            .append("] ")
            .append(event.level)
            .append(" [")
            .append(event.threadName)
            .append(",")
            .append(event.threadId)
            .append(",")
            .append(event.threadPriority)
            .append("] ")

        if (printContextData) {
            stringBuilder.append("(")
            var preFix = ""
            event.contextData.forEach { k, v: Any? ->
                stringBuilder.append(preFix)
                stringBuilder.append("$k=${v?.toString()}")
                preFix = ", "
            }
            stringBuilder.append(") ")
        }

        stringBuilder.append("[")
        LoggingHelper.appendFormattedLoggerNameToStringBuilder(event.loggerName, loggerNameLeaveAsIsLevel, stringBuilder)
        stringBuilder.append("] ")

        if (event.marker != null) {
            stringBuilder.append("[")
                .append(event.marker.name)
                .append("] ")
        }

        stringBuilder.append("> ")
            .append(event.message.formattedMessage)
            .append(" ")

        if (event.thrown != null) {
            stringBuilder
                .append(">>>>>>>>>>>>>>\n")
                .append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n")
                .append(">>>>>>>>>>>>>>>> A Thrown is present in the LogEvent at: ")
                .append(timeCodeString)
                .append(" >>>>>>>>>>>>>>>>\n\n")
                .append(LoggingHelper.getStackTraceAsString(event.thrown))
                .append("\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ");
        }

        stringBuilder.append("\n")
        return stringBuilder.toString()
    }

    private fun getFormattedDateTimeStringFromLogEvent(event: LogEvent): String {
        return DEFAULT_DATE_TIME_FORMATTER.format(Instant.ofEpochMilli(event.instant.epochMillisecond));
    }

    class Builder internal constructor() : AbstractStringLayout.Builder<Builder?>(), org.apache.logging.log4j.core.util.Builder<LocaldevLayout> {
        @PluginBuilderAttribute("loggerNameLeaveAsIsLevel")
        private var loggerNameLeaveAsIsLevel: Int = DEFAULT_LOGGER_NAME_LEAVE_AS_IS_LEVEL

        @PluginBuilderAttribute("printContextData")
        private var printContextData: Boolean = false

        fun setLoggerNameLeaveAsIsLevel(loggerNameLeaveAsIsLevel: Int): Builder {
            this.loggerNameLeaveAsIsLevel = loggerNameLeaveAsIsLevel
            return asBuilder()!!
        }

        fun setPrintContextData(printContextData: Boolean): Builder {
            this.printContextData = printContextData
            return asBuilder()!!
        }

        override fun setConfiguration(configuration: Configuration?): Builder {
            return super.setConfiguration(configuration)!!
        }

        override fun build(): LocaldevLayout {
            return LocaldevLayout(
                configuration,
                loggerNameLeaveAsIsLevel,
                printContextData
            )
        }
    }
}
