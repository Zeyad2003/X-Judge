# Replace the current complex logic with a simpler one by re-implementing each step alone, and in a good way.

- [x] First step:

  - First let's establish an environment that allows access the browser ui within codespaces (browser in a browser).

    - This can be done by using a vnc server and a noVNC client, and it should open automatically after hitting an end-point called.

  - Create a demo application that shows navigating through a website (don't have captcha) but demonstrate the browser ui is activate (similar to the local one) when needing the headful mode.

  Make your work in isolated environment and in a separate package "browser" within the repo.

  we will continue after finishing this.

- [x] Second step:

  - Forget about the previous demo,remove all stuff related to it, and let's create a basic playwright setup that stores the browser session, cookies, local storage, etc in a json file.
  - the index.html should be simple, and shows a website name and status beside it.

    - The status represents the website has a valid session or not. (see how to identify that, maybe the json file isn't exist, maybe some stuff has expired ...etc).
    - if it's expired show a button to activate (AKA solve the captcha).
    - After the captcha is solved, the we should create/update the json file with the new session data, and hit the /login end-point to login to the website, and update the session data.

      - note that this involve two parts:
        - solving the captcha (manual by the user using the vnc browser ui).
        - hitting the /login end-point to update the session data (done with playwright).

    - This work should be very simple without too much logic and complex stuff, and don't create a .md summary for what you did.
