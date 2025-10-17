package com.scraping.scrapingdemo.browser;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller for browser UI demo endpoints.
 * Demonstrates Playwright browser automation with visible UI via noVNC.
 */
@Slf4j
@RestController
@RequestMapping("/api/browser-demo")
public class BrowserDemoController {

    @Autowired
    private BrowserDemoService browserDemoService;

    /**
     * Test browser UI by navigating to a simple website.
     * Opens a visible browser that can be accessed via noVNC at port 6080.
     * 
     * Example: GET /api/browser-demo/test-ui
     * Example: GET /api/browser-demo/test-ui?url=https://playwright.dev
     */
    @GetMapping("/test-ui")
    public ResponseEntity<Map<String, Object>> testBrowserUI(
            @RequestParam(defaultValue = "https://example.com") String url) {

        log.info("Received test-ui request for URL: {}", url);

        Map<String, Object> response = new HashMap<>();

        try {
            String result = browserDemoService.runBrowserDemo(url);

            response.put("success", true);
            response.put("message", result);
            response.put("vncUrl", "http://localhost:6080");
            response.put("vncPassword", "vscode");
            response.put("instructions", new String[] {
                    "1. The browser has been launched in headed mode",
                    "2. Open port 6080 in your browser to see the noVNC interface",
                    "3. Use password 'vscode' if prompted",
                    "4. You should see the Chromium browser navigating to: " + url,
                    "5. The browser window will remain open for you to interact with"
            });

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error in test-ui endpoint", e);
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Run an extended demo that navigates through multiple websites.
     * This shows that the browser UI remains active throughout.
     * 
     * Example: GET /api/browser-demo/extended-demo
     */
    @GetMapping("/extended-demo")
    public ResponseEntity<Map<String, Object>> runExtendedDemo() {

        log.info("Received extended-demo request");

        Map<String, Object> response = new HashMap<>();

        try {
            String result = browserDemoService.runExtendedDemo();

            response.put("success", true);
            response.put("message", result);
            response.put("vncUrl", "http://localhost:6080");
            response.put("vncPassword", "vscode");
            response.put("note", "Watch the browser navigate through multiple sites at port 6080");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error in extended-demo endpoint", e);
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Get current page information.
     * 
     * Example: GET /api/browser-demo/page-info
     */
    @GetMapping("/page-info")
    public ResponseEntity<Map<String, Object>> getPageInfo() {

        Map<String, Object> response = new HashMap<>();

        try {
            String info = browserDemoService.getCurrentPageInfo();

            response.put("success", true);
            response.put("pageInfo", info);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error getting page info", e);
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Close the browser and cleanup resources.
     * 
     * Example: POST /api/browser-demo/close
     */
    @PostMapping("/close")
    public ResponseEntity<Map<String, Object>> closeBrowser() {

        log.info("Received close browser request");

        Map<String, Object> response = new HashMap<>();

        try {
            browserDemoService.closeBrowser();

            response.put("success", true);
            response.put("message", "Browser closed successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error closing browser", e);
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Health check endpoint.
     * 
     * Example: GET /api/browser-demo/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {

        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("service", "Browser Demo Service");
        response.put("vncPort", 6080);
        response.put("browserActive", browserDemoService.isBrowserActive());

        return ResponseEntity.ok(response);
    }
}
