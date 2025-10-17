import json
import nodriver as uc
import asyncio

async def main():
    # Start browser with minimal config - nodriver handles stealth automatically
    browser = await uc.start(
        browser_executable_path="/usr/bin/brave-browser",
        headless=False
    )

    # Wait a bit for browser to fully initialize
    await asyncio.sleep(2)

    # Create a new tab and navigate
    page = await browser.get('about:blank')

    # Additional stealth measures
    await page.evaluate("""
        Object.defineProperty(navigator, 'webdriver', {
            get: () => undefined
        });
        Object.defineProperty(navigator, 'plugins', {
            get: () => [1, 2, 3, 4, 5]
        });
        Object.defineProperty(navigator, 'languages', {
            get: () => ['en-US', 'en']
        });
    """)

    print("Browser launched. Navigating to Codeforces...")

    # Now navigate to Codeforces
    await page.get('https://codeforces.com/enter')

    print("Page loaded. Please log in and solve any captcha if needed.")
    print("You have 30 seconds...")

    await asyncio.sleep(30)  # Give user time to interact

    print("Time's up! Saving cookies and localStorage...")

    origin = await page.evaluate("window.location.origin")
    cookies = await browser.cookies.get_all()
    # Fetch localStorage as a dict
    local_storage = await page.evaluate("Object.fromEntries(Object.entries(window.localStorage))")

    # Format cookies for Playwright compatibility
    cookies_formatted = [
        {
            "name": c.name,
            "value": c.value,
            "domain": c.domain,
            "path": c.path,
            "expires": c.expires if c.expires is not None else -1.0,
            "httpOnly": c.http_only,
            "secure": c.secure,
            "sameSite": c.same_site.name.capitalize() if c.same_site else "Lax"
        }
        for c in cookies
    ]

    data = {
        "cookies": cookies_formatted,
        "origins": [
            {
                "origin": origin,
                "localStorage": local_storage,
            }
        ]
    }

    with open("auth.json", "w") as f:
        json.dump(data, f, indent=2)

    print(f"Saved {len(cookies_formatted)} cookies to auth.json")
    print(f"Saved localStorage from {origin}")

    await asyncio.sleep(2)
    await browser.stop()

if __name__ == '__main__':
    uc.loop().run_until_complete(main())
