package scripts

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.security.SecureRandom

class Scripts {

    private val passwordEncoder = BCryptPasswordEncoder(11, SecureRandom())

    @Test
    @Disabled
    fun bCryptEncode() {
        var toEncode = "N/A"
        var encoded = passwordEncoder.encode(toEncode)
        println("Encoded password: $encoded")
    }
}