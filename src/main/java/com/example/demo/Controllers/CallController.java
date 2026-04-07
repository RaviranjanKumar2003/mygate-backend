package com.example.demo.Controllers;

import com.example.demo.Entities.CallMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class CallController {

    private final Map<String, Set<String>> activeCalls = new HashMap<>();

    private final SimpMessagingTemplate messagingTemplate;

    public CallController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/start-call")
    public void startCall(CallMessage message) {

        System.out.println("Incoming Call: " + message.getRoomName() + ", " + message.getCallerName() + ", " + message.getType());

        Set<String> users = new HashSet<>();
        users.add(message.getCallerName()); // ✅ caller added

        activeCalls.put(message.getRoomName(), users);

        messagingTemplate.convertAndSend(
                "/topic/incoming-call",
                message
        );
    }

    @MessageMapping("/join-call")
    public void joinCall(CallMessage message) {

        activeCalls
                .computeIfAbsent(message.getRoomName(), k -> new HashSet<>())
                .add(message.getCallerName());

        System.out.println("User joined: " + message.getCallerName());
    }


    // ✅ END CALL (⭐ NEW ADD)
    @MessageMapping("/end-call")
    public void endCall(CallMessage message) {

        String room = message.getRoomName();
        String user = message.getCallerName();

        Set<String> users = activeCalls.get(room);

        if (users == null || users.isEmpty()) {
            messagingTemplate.convertAndSend("/topic/end-call", message);
            activeCalls.remove(room);
            return;
        }

        users.remove(user);

        if (users.isEmpty()) {
            // 🔥 sab nikal gaye → full end
            messagingTemplate.convertAndSend("/topic/end-call", message);
            activeCalls.remove(room);
        }
        else if (users.size() == 1) {
            // 🔥 sirf 1 banda bacha
            messagingTemplate.convertAndSend("/topic/alone-user", message);
        }
    }


}