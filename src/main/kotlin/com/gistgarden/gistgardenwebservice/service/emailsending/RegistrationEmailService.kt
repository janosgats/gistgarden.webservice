package com.gistgarden.gistgardenwebservice.service.emailsending

import com.gistgarden.gistgardenwebservice.config.properties.WebsiteConfigurationProperties
import com.gistgarden.gistgardenwebservice.util.logging.LoggerDelegate
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Content
import org.springframework.stereotype.Service
import java.net.URLEncoder


@Service
class RegistrationEmailService(
    private val emailSenderService: EmailSenderService,
    private val websiteConfigurationProperties: WebsiteConfigurationProperties,
) {
    private val log by LoggerDelegate()

    fun sendEmailPasswordRegistrationVerificationEmail(emailAddress: String, inquiryId: Long, inquirySecret: String) {
        val urlEncodedInquirySecret = URLEncoder.encode(inquirySecret, "UTF-8")
        val verificationLink = "${websiteConfigurationProperties.mainSiteBaseUrl}/registration/verifyEmail?id=$inquiryId&secret=$urlEncodedInquirySecret"

        val mail = Mail()
        mail.addPersonalisationForAddressee(emailAddress)
        mail.subject = "Verify Your Email"

        mail.addContent(
            Content(
                "text/html",
                """
                 <html lang="en">
                     <body>
                         <p>Hello from Gist Garden!</p>
                         
                         <p> 
                             <a href="$verificationLink">Click here</a> 
                             to verify your email and start using this heap of awesomeness
                         </p> 
                          
                         <br/>
                         <br/>
                          
                         <a href="$verificationLink">$verificationLink</a> 
                     </body>
                 </html>
         """.trimIndent()
            )
        )

        emailSenderService.sendMailWithHelloSender(
            mail,
            logContext = "sendEmailPasswordRegistrationVerificationEmail"
        )
    }
}