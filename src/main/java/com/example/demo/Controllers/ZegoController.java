package com.example.demo.Controllers;

import org.springframework.web.bind.annotation.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

@RestController
@RequestMapping("/api/zego")
@CrossOrigin
public class ZegoController {

    private final long appID = 605040740;
    private final String serverSecret = "YOUR_SERVER_SECRET"; // ⚠️ safe hai backend me

    @PostMapping("/generate-token")
    public Map<String, Object> generateToken(@RequestBody Map<String, String> body) {
        String userId = body.get("userId");

        String token = createToken(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("appID", appID);

        return response;
    }

    private String createToken(String userId) {
        try {
            long ctime = System.currentTimeMillis() / 1000;
            long expire = 3600;

            String payload = "{"
                    + "\"app_id\":" + appID + ","
                    + "\"user_id\":\"" + userId + "\","
                    + "\"ctime\":" + ctime + ","
                    + "\"expire\":" + expire
                    + "}";

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(serverSecret.getBytes(), "HmacSHA256");
            mac.init(secretKey);

            byte[] hashBytes = mac.doFinal(payload.getBytes());
            String hash = Base64.getEncoder().encodeToString(hashBytes);

            String tokenJson = "{"
                    + "\"payload\":" + payload + ","
                    + "\"hash\":\"" + hash + "\""
                    + "}";

            return Base64.getEncoder().encodeToString(tokenJson.getBytes());

        } catch (Exception e) {
            throw new RuntimeException("Token generation failed", e);
        }
    }
}