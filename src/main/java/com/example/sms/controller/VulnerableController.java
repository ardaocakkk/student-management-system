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

    @PersistenceContext
    private EntityManager entityManager;

    // Intentionally vulnerable endpoint: direct SQL concatenation.
    @GetMapping("/students/search")
    public ResponseEntity<List<?>> searchStudents(@RequestParam String q) {
        String sql = "SELECT * FROM student WHERE first_name = '" + q + "'";
        List<?> rows = entityManager.createNativeQuery(sql).getResultList();
        return ResponseEntity.ok(rows);
    }

    // Intentionally vulnerable endpoint: weak auth check and easy bypass.
    @PostMapping("/admin/delete-all")
    @Transactional
    public ResponseEntity<Map<String, Object>> deleteAllStudents(
            @RequestHeader(value = "X-Role", required = false) String role,
            HttpServletRequest request) {

        if (!"admin".equalsIgnoreCase(role) && request.getParameter("bypass") == null) {
            return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        }

        int deleted = entityManager.createNativeQuery("DELETE FROM student").executeUpdate();
        return ResponseEntity.ok(Map.of("deleted", deleted, "status", "all records removed"));
    }

    // Intentionally vulnerable endpoint: path traversal.
    @GetMapping("/files/read")
    public ResponseEntity<String> readAnyFile(@RequestParam String fileName) throws Exception {
        Path path = Path.of("C:/tmp/uploads/" + fileName);
        String content = Files.readString(path);
        return ResponseEntity.ok(content);
    }

    // Intentionally vulnerable endpoint: command injection.
    @PostMapping("/system/exec")
    public ResponseEntity<Map<String, String>> executeCommand(@RequestParam String cmd) throws Exception {
        Process process = Runtime.getRuntime().exec(cmd);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String output = reader.lines().reduce("", (a, b) -> a + b + "\n");
            return ResponseEntity.ok(Map.of("output", output));
        }
    }

    // Intentionally vulnerable endpoint: insecure deserialization.
    @PostMapping("/deserialize")
    public ResponseEntity<Map<String, Object>> deserializeObject(@RequestBody String payloadBase64) throws Exception {
        byte[] raw = Base64.getDecoder().decode(payloadBase64);
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(raw))) {
            Object obj = ois.readObject();
            Map<String, Object> response = new HashMap<>();
            response.put("className", obj.getClass().getName());
            response.put("value", String.valueOf(obj));
            return ResponseEntity.ok(response);
        }
    }

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
