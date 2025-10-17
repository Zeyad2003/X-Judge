# Browser UI Demo - X-Judge

A simple demonstration of Playwright browser automation with **visible browser UI** accessible via noVNC in Codespaces.

---

## 🎯 Purpose

This demo shows how to:

1. Launch a Chromium browser in **headed mode** (visible UI)
2. Access and interact with the browser through **noVNC** web interface
3. Perform browser automation while watching it happen in real-time
4. Lay the foundation for more complex automation tasks (like solving captchas)

---

## 📦 Package Structure

```
src/main/java/com/scraping/scrapingdemo/browser/
├── BrowserDemoService.java       # Service layer - browser operations
└── BrowserDemoController.java    # REST API endpoints

src/main/resources/static/
└── browser-demo.html             # Web UI for testing
```

---

## 🚀 Quick Start

### 1. Ensure Playwright Browsers are Installed

```bash
./gradlew playwrightInstall
```

### 2. Start the Spring Boot Application

```bash
./gradlew bootRun
```

The application will start on port **7070**.

### 3. Open the Web UI

Navigate to: **http://localhost:7070/browser-demo.html**

### 4. Test the Browser

1. Click **"🚀 Test Browser UI"** button
2. Wait for success response
3. Click **"🖥️ Open noVNC"** or manually go to **http://localhost:6080**
4. Enter password: **`vscode`**
5. You should see the Chromium browser navigating to the specified URL!

---

## 📡 API Endpoints

### 1. Test Browser UI

```http
GET /api/browser-demo/test-ui?url=https://example.com
```

**Description:** Launches browser and navigates to the specified URL (default: example.com)

**Response:**

```json
{
  "success": true,
  "message": "✅ Browser Demo Complete!\nURL: https://example.com\n...",
  "vncUrl": "http://localhost:6080",
  "vncPassword": "vscode",
  "instructions": [
    "1. The browser has been launched in headed mode",
    "2. Open port 6080 in your browser to see the noVNC interface",
    "..."
  ]
}
```

**Example:**

```bash
curl "http://localhost:7070/api/browser-demo/test-ui"
curl "http://localhost:7070/api/browser-demo/test-ui?url=https://playwright.dev"
```

---

### 2. Extended Demo

```http
GET /api/browser-demo/extended-demo
```

**Description:** Runs a multi-step demo that navigates through multiple websites (example.com → playwright.dev → github.com)

**Example:**

```bash
curl "http://localhost:7070/api/browser-demo/extended-demo"
```

---

### 3. Get Page Info

```http
GET /api/browser-demo/page-info
```

**Description:** Returns information about the current page (URL and title)

**Example:**

```bash
curl "http://localhost:7070/api/browser-demo/page-info"
```

---

### 4. Close Browser

```http
POST /api/browser-demo/close
```

**Description:** Closes the browser and cleans up resources

**Example:**

```bash
curl -X POST "http://localhost:7070/api/browser-demo/close"
```

---

### 5. Health Check

```http
GET /api/browser-demo/health
```

**Description:** Check if the service is running

**Example:**

```bash
curl "http://localhost:7070/api/browser-demo/health"
```

---

## 🖥️ Accessing the Browser UI

### Method 1: Through Web UI

1. Open **http://localhost:7070/browser-demo.html**
2. Click **"Open noVNC (Port 6080)"** button

### Method 2: Direct Access

1. Navigate to **http://localhost:6080**
2. Enter password: **`vscode`**

### Method 3: VS Code Ports Tab

1. Go to VS Code **"Ports"** tab
2. Find port **6080** (labeled "noVNC - Browser UI")
3. Click the **globe icon** to open in browser

---

## 🎓 How It Works

### 1. Desktop-Lite Feature

The dev container includes the `desktop-lite` feature which provides:

- **VNC Server** (port 5901)
- **noVNC Web Client** (port 6080)
- **Fluxbox Desktop Environment**

### 2. Headed Browser Mode

The service launches Playwright with `headless: false`:

```java
BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
    .setHeadless(false)  // Makes browser visible
    .setArgs(Arrays.asList(
        "--no-sandbox",
        "--disable-setuid-sandbox"
    ));
```

### 3. Browser Display

When the browser launches in headed mode:

- It appears on the virtual desktop provided by desktop-lite
- The desktop is accessible via noVNC on port 6080
- You can see and interact with the browser in real-time

---

## 📝 Example Workflows

### Workflow 1: Simple Navigation Test

```bash
# Start the app
./gradlew bootRun

# In another terminal, test the endpoint
curl "http://localhost:7070/api/browser-demo/test-ui?url=https://github.com"

# Open browser to see the result
open http://localhost:6080  # macOS
# or
xdg-open http://localhost:6080  # Linux
```

### Workflow 2: Extended Demo

```bash
# Run extended demo
curl "http://localhost:7070/api/browser-demo/extended-demo"

# Watch the browser navigate through multiple sites in noVNC
```

### Workflow 3: Interactive Development

```bash
# Open the web UI
open http://localhost:7070/browser-demo.html

# Click buttons to test different scenarios
# Watch results in noVNC window
```

---

## 🔧 Technical Details

### Service Layer (`BrowserDemoService.java`)

- Manages Playwright lifecycle
- Launches browser in headed mode
- Performs navigation and interaction
- Provides cleanup methods

### Controller Layer (`BrowserDemoController.java`)

- Exposes REST API endpoints
- Handles HTTP requests/responses
- Provides JSON-formatted responses

### Key Features

- ✅ Headed browser mode (visible UI)
- ✅ noVNC web access on port 6080
- ✅ Multiple navigation demos
- ✅ Clean resource management
- ✅ Error handling and logging
- ✅ Web UI for easy testing

---

## 🐛 Troubleshooting

### Browser doesn't appear in noVNC

**Solution:**

1. Ensure Playwright browsers are installed: `./gradlew playwrightInstall`
2. Verify desktop-lite is configured in `.devcontainer/devcontainer.json`
3. Rebuild the dev container if needed

### Port 6080 not accessible

**Solution:**

1. Check the **Ports** tab in VS Code
2. Ensure port 6080 is forwarded
3. Try accessing via the forwarded URL

### noVNC shows "Failed to connect"

**Solution:**

1. Restart the dev container
2. Ensure the desktop-lite feature is properly initialized
3. Check VNC server is running: `ps aux | grep vnc`

### Browser launches but is invisible

**Solution:**

1. Verify `headless: false` in the launch options
2. Check that DISPLAY environment variable is set
3. Restart the Spring Boot application

---

## 🎯 Next Steps

After verifying this demo works, you can:

1. **Add Authentication Capture**

   - Navigate to login pages
   - Wait for user to solve captcha
   - Capture cookies and localStorage
   - Save to JSON file

2. **Build Submission Logic**

   - Use captured auth state
   - Submit code to online judges
   - Handle form filling and submission

3. **Extend to Multiple OJs**
   - Abstract the browser service
   - Support different platforms
   - Unified interface for all OJs

---

## 🔐 Security Notes

- Default VNC password: **`vscode`**
- Browser runs in dev container (isolated)
- No sensitive data should be stored in demo
- Always close browser when done to free resources

---

## 📚 References

- [Playwright Java Docs](https://playwright.dev/java/)
- [VS Code Dev Containers](https://code.visualstudio.com/docs/devcontainers/containers)
- [Desktop-Lite Feature](https://github.com/devcontainers/features/tree/main/src/desktop-lite)
- [noVNC](https://novnc.com/)

---

## ✅ Success Criteria

This demo is successful if:

- [ ] Browser launches without errors
- [ ] Browser UI is visible in noVNC
- [ ] Navigation works as expected
- [ ] Multiple sites can be visited
- [ ] Browser can be closed cleanly
- [ ] All endpoints respond correctly

---

**Happy Testing! 🚀**
