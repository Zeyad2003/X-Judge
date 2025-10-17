package com.scraping.scrapingdemo.browser;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import lombok.extern.slf4j.Slf4j;

/**
 * Simple browser demo service that demonstrates Playwright navigation
 * with visible browser UI accessible via noVNC in Codespaces.
 */
@Slf4j
@Service
public class BrowserDemoService {

    private Playwright playwright;
    private Browser browser;
    private Page page;

    /**
     * Launch browser in headed mode and perform a demo navigation.
     * The browser UI will be visible via noVNC at port 6080.
     *
     * @param url The URL to navigate to
     * @return A message describing what happened
     */
    public String runBrowserDemo(String url) {
        try {
            log.info("Starting browser demo for URL: {}", url);

            // Initialize Playwright
            if (playwright == null) {
                playwright = Playwright.create();
                log.info("Playwright initialized");
            }

            // Launch browser in headed mode (visible UI)
            if (browser == null) {
                BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                        .setHeadless(false) // Important: Makes browser visible
                        .setArgs(Arrays.asList(
                                "--no-sandbox",
                                "--disable-setuid-sandbox",
                                "--disable-dev-shm-usage",
                                "--disable-blink-features=AutomationControlled"));

                browser = playwright.chromium().launch(launchOptions);
                log.info("Browser launched in headed mode");
            }

            // Create a new page
            page = browser.newPage();
            log.info("New page created");

            // Navigate to the URL
            log.info("Navigating to: {}", url);
            page.navigate(url, new Page.NavigateOptions().setTimeout(30000));
            log.info("Navigation complete");

            // Get page title
            String title = page.title();
            log.info("Page title: {}", title);

            // Perform some demo actions to show the browser is working
            String currentUrl = page.url();

            // Take a screenshot (optional, for verification)
            byte[] screenshot = page.screenshot();
            log.info("Screenshot captured ({} bytes)", screenshot.length);

            String message = String.format(
                    "✅ Browser Demo Complete!\n" +
                            "URL: %s\n" +
                            "Title: %s\n" +
                            "Browser is now visible via noVNC at port 6080\n" +
                            "You can interact with it in the browser window.",
                    currentUrl, title);

            log.info(message);
            return message;

        } catch (Exception e) {
            log.error("Error during browser demo: {}", e.getMessage(), e);
            return "❌ Error: " + e.getMessage();
        }
    }

    /**
     * Perform an extended demo with multiple navigation steps.
     * This demonstrates that the browser UI remains active.
     *
     * @return A message describing the demo steps
     */
    public String runExtendedDemo() {
        try {
            log.info("Starting extended browser demo");

            // Initialize if needed
            if (playwright == null || browser == null) {
                runBrowserDemo("https://example.com");
            }

            StringBuilder result = new StringBuilder();
            result.append("🎬 Extended Browser Demo Started\n\n");

            // Step 1: Visit example.com
            log.info("Step 1: Navigating to example.com");
            page.navigate("https://example.com");
            result.append("Step 1: ✅ Visited example.com - Title: ").append(page.title()).append("\n");
            Thread.sleep(2000); // Pause for visibility

            // Step 2: Visit Playwright homepage
            log.info("Step 2: Navigating to Playwright site");
            page.navigate("https://playwright.dev");
            result.append("Step 2: ✅ Visited playwright.dev - Title: ").append(page.title()).append("\n");
            Thread.sleep(2000);

            // Step 3: Visit GitHub
            log.info("Step 3: Navigating to GitHub");
            page.navigate("https://github.com");
            result.append("Step 3: ✅ Visited github.com - Title: ").append(page.title()).append("\n");
            Thread.sleep(2000);

            result.append("\n🎉 Demo Complete! Browser window is still active at port 6080");

            String message = result.toString();
            log.info(message);
            return message;

        } catch (Exception e) {
            log.error("Error during extended demo: {}", e.getMessage(), e);
            return "❌ Error: " + e.getMessage();
        }
    }

    /**
     * Check if browser is currently active
     */
    public boolean isBrowserActive() {
        return playwright != null && browser != null && !browser.isConnected();
    }

    /**
     * Get current page information
     */
    public String getCurrentPageInfo() {
        if (page == null) {
            return "No active page";
        }

        return String.format("Current URL: %s\nCurrent Title: %s",
                page.url(), page.title());
    }

    /**
     * Close the browser and cleanup resources
     */
    public void closeBrowser() {
        log.info("Closing browser...");

        if (page != null) {
            page.close();
            page = null;
            log.info("Page closed");
        }

        if (browser != null) {
            browser.close();
            browser = null;
            log.info("Browser closed");
        }

        if (playwright != null) {
            playwright.close();
            playwright = null;
            log.info("Playwright closed");
        }

        log.info("Browser cleanup complete");
    }
}
