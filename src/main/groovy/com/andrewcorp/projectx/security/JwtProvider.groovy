package com.andrewcorp.projectx.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import jakarta.annotation.PostConstruct
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import java.nio.file.Files
import java.nio.file.Path
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.time.LocalDateTime
import java.time.ZoneId

/**
 *
 * @author Andrew
 * @since 02.10.2023
 */
@Component
class JwtProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class)

    private List<String> invalidatedTokens = new ArrayList<>()
    private Key jwtSecretKey
    @Value('${jwt.secret.file.path}')
    private String jwtSecretFile
    @Value('${jwt.expiration.hours}')
    private int jwtExpirationHours

    @PostConstruct
    def init() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        createPemIfNotExists()
        jwtSecretKey = loadPrivateKey(jwtSecretFile)
    }

    private void createPemIfNotExists() throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
        if (!new File(jwtSecretFile).exists()) {
            logger.warn("private_key.pem not found, generating new key")
            Security.addProvider(new BouncyCastleProvider())

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC")
            keyPairGenerator.initialize(2048)
            KeyPair keyPair = keyPairGenerator.generateKeyPair()

            PrivateKey privateKey = keyPair.getPrivate()
            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKey.getEncoded())

            try (FileOutputStream fos = new FileOutputStream(jwtSecretFile)) {
                fos.write(privateKeyInfo.getEncoded())
            }
            logger.info("private_key.pem generated")
        }
    }

    private Key loadPrivateKey(String privateKeyPath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateKeyBytes = Files.readAllBytes(Path.of(privateKeyPath))
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes)
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec)
    }

    def generateToken(String username, String userId) {
        LocalDateTime currentTime = LocalDateTime.now()
        LocalDateTime expirationTime = currentTime.plusHours(jwtExpirationHours)

        return Jwts.builder()
                .setSubject(username)
                .claim("id", userId)
                .setIssuedAt(java.sql.Timestamp.valueOf(currentTime))
                .setExpiration(java.sql.Timestamp.valueOf(expirationTime))
                .signWith(jwtSecretKey)
                .compact()
    }

    def getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject()
    }

    def getUserIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id", String.class)
    }

    def validateToken(String token) {
        if (invalidatedTokens.contains(token)) {
            return false
        }
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(token)

            LocalDateTime expirationTime = claims.getBody().getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
            return !expirationTime.isBefore(LocalDateTime.now())
        } catch (JwtException | IllegalArgumentException ex) {
            logger.error("Invalid token: " + ex.getMessage())
            return false
        }
    }

    def invalidateToken(String token) {
        invalidatedTokens.add(token)
    }
}
