package com.gistgarden.gistgardenwebservice.api.controller

import com.gistgarden.gistgardenwebservice.service.emailsending.EmailSenderService
import com.gistgarden.gistgardenwebservice.service.emailsending.addPersonalisationForAddressee
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Content
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/api/dev")
class DevController(
    private val emailSenderService: EmailSenderService,
) {

    @PostMapping("/sendTestEmail")
    fun sendTestEmail(@RequestParam recipientEmail: String): String {
        val mail = Mail()

        mail.addPersonalisationForAddressee(recipientEmail)
        mail.subject = "Test Email Here"

        mail.addContent(
            Content(
                "text/html",
                """
                 <html lang="en">
                     <body>
                         <p> This is a test email from GistGarden. </p> 
                         <p> abcd aáeéiíoóöűuúüű </p> 
                         <p> ABCD AÁEÉIÍOÓÖŰUÚÜŰ </p> 
                         <p> current time: ${Instant.now()} </p> 
                     </body>
                 </html>
         """.trimIndent()
            )
        )

        emailSenderService.sendMailWithHelloSender(mail, "sendTestEmail")

        return "successfully sent email"
    }

}