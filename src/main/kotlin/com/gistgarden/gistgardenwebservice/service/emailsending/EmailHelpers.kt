package com.gistgarden.gistgardenwebservice.service.emailsending

import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Email
import com.sendgrid.helpers.mail.objects.Personalization


object EmailHelpers {
    /**
     * Addressees are NOT visible for other addressees
     */
    fun createPersonalizationForAddressee(toEmailAddress: String): Personalization {
        return Personalization().apply {
            addTo(Email(toEmailAddress))
        }
    }

    /**
     * All addressees are visible for every addressee
     */
    fun createPersonalizationForAddressees(toEmailAddresses: Collection<String>): Personalization {
        val personalization = Personalization()

        toEmailAddresses.forEach {
            personalization.addTo(Email(it))
        }

        return personalization
    }
}

fun Mail.addPersonalisationForAddressee(toEmailAddress: String) {
    this.addPersonalization(
        Personalization().apply {
            addTo(Email(toEmailAddress))
        }
    )
}