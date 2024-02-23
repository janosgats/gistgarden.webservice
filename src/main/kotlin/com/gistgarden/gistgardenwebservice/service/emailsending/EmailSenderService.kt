package com.gistgarden.gistgardenwebservice.service.emailsending

import com.gistgarden.gistgardenwebservice.exception.EmailSendingException
import com.gistgarden.gistgardenwebservice.util.logging.LoggerDelegate
import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Email
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.stream.Collectors


@Service
class EmailSenderService(
    private val sendGrid: SendGrid,
    @Value("\${email.logEmailsInsteadOfSendingThem:false}")
    private val logEmailsInsteadOfSendingThem: Boolean
) {
    private val log by LoggerDelegate()

    private companion object {
        private val EMAIL_FROM_HELLO = Email("hello@gistgarden.com", "Gist Garden")
        private val EMAIL_REPLYTO_NOREPLY = Email("noreply@gistgarden.com", "Gist Garden")
    }

    fun sendMailWithHelloSender(mail: Mail, logContext: String) {
        sendMailWithSender(mail, from = EMAIL_FROM_HELLO, replayTo = EMAIL_REPLYTO_NOREPLY, logContext)
    }

    fun sendMailWithSender(mail: Mail, from: Email, replayTo: Email, logContext: String) {
        mail.from = from
        mail.replyTo = replayTo

        if (logEmailsInsteadOfSendingThem) {
            logEmail(mail)
            return
        }

        val request = Request().apply {
            method = Method.POST
            endpoint = "mail/send"
            body = mail.build();
        }

        val response = sendGrid.api(request);

        if (response.statusCode < 200 || response.statusCode > 299) {
            throw EmailSendingException(
                "StatusCode is not 2xx when sending SendGrid Email in EmailSenderService::sendMailWithSender ($logContext).\n" +
                        "responseStatusCode: ${response.statusCode},\nresponseHeaders: ${response.headers},\nresponseBody: ${response.body}"
            )
        }
    }

    private fun logEmail(mail: Mail) {
        log.info("""Email to send:
            |from: ${mail.from.email}
            |replyTo: ${mail.replyTo.email}
            |to: ${
            mail.personalization.stream()
                .map { it ->
                    it.tos.stream()
                        .map { it.email }
                        .collect(Collectors.joining(","))
                }
                .collect(Collectors.joining(";"))
        }
            |subject: ${mail.subject}
            |content:
            |${
            mail.content.stream()
                .map { it.value }
                .collect(Collectors.joining("\n-------------------------\n"))
        }
        """.trimMargin())
    }
}