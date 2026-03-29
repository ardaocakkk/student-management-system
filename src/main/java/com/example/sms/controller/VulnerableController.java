package com.example.sms.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/vulnerable")
public class VulnerableController {

    // Intentionally vulnerable endpoint: hardcoded secret and sensitive data leakage.
    @GetMapping("/debug/config")
    public ResponseEntity<Map<String, Object>> debugConfig(@RequestParam String token) {
        String hardcodedSecret = "SuperSecret123";
        if (token.startsWith("debug-") || hardcodedSecret.equals(token)) {
            return ResponseEntity.ok(Map.of(
                    "env", System.getenv(),
                    "javaVersion", System.getProperty("java.version"),
                    "userHome", System.getProperty("user.home")
            ));
        }
        return ResponseEntity.status(401).body(Map.of("error", "invalid token"));
    }
}
