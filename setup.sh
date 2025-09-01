#!/usr/bin/env bash

# X-Judge development environment setup script
# - Zsh + Oh My Zsh + plugins (autosuggestions, syntax-highlighting, completions)
# - SDKMAN! + Zulu Java 21 (latest available 21.x)
# - MySQL Server + schema `X-Judge` + user judge/judge
# - Gemini CLI (@google/gemini-cli)

set -euo pipefail

DB_NAME="X-Judge"
DB_USER="judge"
DB_PASS="judge"

COLOR_BLUE="\033[1;34m"
COLOR_GREEN="\033[1;32m"
COLOR_YELLOW="\033[1;33m"
COLOR_RED="\033[1;31m"
COLOR_RESET="\033[0m"

log()  { echo -e "${COLOR_BLUE}==>${COLOR_RESET} $*"; }
ok()   { echo -e "${COLOR_GREEN}✔${COLOR_RESET} $*"; }
warn() { echo -e "${COLOR_YELLOW}⚠${COLOR_RESET} $*"; }
err()  { echo -e "${COLOR_RED}✘${COLOR_RESET} $*"; }

need_sudo() {
  if command -v sudo >/dev/null 2>&1; then
    echo sudo
  else
    echo ""
  fi
}

SUDO="$(need_sudo)"

apt_install() {
  local pkgs=("$@")
  $SUDO bash -c 'export DEBIAN_FRONTEND=noninteractive; apt-get update -y' >/dev/null
  $SUDO bash -c "export DEBIAN_FRONTEND=noninteractive; apt-get install -y ${pkgs[*]}" >/dev/null
}

ensure_packages() {
  log "Installing base packages (curl, git, ca-certificates)..."
  apt_install curl git ca-certificates gnupg lsb-release unzip software-properties-common >/dev/null || true
  ok "Base packages installed"
}

install_zsh_and_ohmyzsh() {
  if ! command -v zsh >/dev/null 2>&1; then
    log "Installing zsh..."
    apt_install zsh
    ok "zsh installed"
  else
    ok "zsh already installed"
  fi

  if [ ! -d "$HOME/.oh-my-zsh" ]; then
    log "Installing Oh My Zsh (unattended)..."
    export RUNZSH=no CHSH=no KEEP_ZSHRC=yes
    sh -c "$(curl -fsSL https://raw.githubusercontent.com/ohmyzsh/ohmyzsh/master/tools/install.sh)" >/dev/null
    ok "Oh My Zsh installed"
  else
    ok "Oh My Zsh already installed"
  fi

  local ZSH_CUSTOM_DIR="${ZSH_CUSTOM:-$HOME/.oh-my-zsh/custom}"
  # Plugins: zsh-autosuggestions, zsh-syntax-highlighting, zsh-completions
  if [ ! -d "$ZSH_CUSTOM_DIR/plugins/zsh-autosuggestions" ]; then
    log "Installing zsh-autosuggestions plugin..."
    git clone --depth=1 https://github.com/zsh-users/zsh-autosuggestions "$ZSH_CUSTOM_DIR/plugins/zsh-autosuggestions" >/dev/null
  fi
  if [ ! -d "$ZSH_CUSTOM_DIR/plugins/zsh-syntax-highlighting" ]; then
    log "Installing zsh-syntax-highlighting plugin..."
    git clone --depth=1 https://github.com/zsh-users/zsh-syntax-highlighting "$ZSH_CUSTOM_DIR/plugins/zsh-syntax-highlighting" >/dev/null
  fi
  if [ ! -d "$ZSH_CUSTOM_DIR/plugins/zsh-completions" ]; then
    log "Installing zsh-completions plugin..."
    git clone --depth=1 https://github.com/zsh-users/zsh-completions "$ZSH_CUSTOM_DIR/plugins/zsh-completions" >/dev/null
  fi

  # Ensure .zshrc exists
  [ -f "$HOME/.zshrc" ] || touch "$HOME/.zshrc"

  # Ensure plugins are enabled in .zshrc
  if grep -qE '^plugins=\(' "$HOME/.zshrc"; then
    # Replace existing plugins line with our set (idempotent)
    sed -i 's/^plugins=.*/plugins=(git zsh-autosuggestions zsh-syntax-highlighting zsh-completions)/' "$HOME/.zshrc"
  else
    echo "plugins=(git zsh-autosuggestions zsh-syntax-highlighting zsh-completions)" >> "$HOME/.zshrc"
  fi

  # Make sure compinit runs (Oh My Zsh usually handles this)
  if ! grep -q 'sdkman-init.sh' "$HOME/.zshrc"; then
    echo '[ -s "$HOME/.sdkman/bin/sdkman-init.sh" ] && source "$HOME/.sdkman/bin/sdkman-init.sh"' >> "$HOME/.zshrc"
  fi
  # Ensure zsh-completions is on fpath BEFORE oh-my-zsh is sourced
  if ! grep -q 'zsh-completions' "$HOME/.zshrc"; then
    if grep -q 'source \$ZSH/oh-my-zsh.sh' "$HOME/.zshrc"; then
      sed -i '/source \$ZSH\/oh-my-zsh.sh/i fpath+=(${ZSH_CUSTOM:-$HOME/.oh-my-zsh/custom}/plugins/zsh-completions/src)' "$HOME/.zshrc"
    else
      echo 'fpath+=(${ZSH_CUSTOM:-$HOME/.oh-my-zsh/custom}/plugins/zsh-completions/src)' >> "$HOME/.zshrc"
    fi
  fi

  # Attempt to set zsh as default shell
  if [ "${SHELL:-}" != "$(command -v zsh)" ]; then
    if $SUDO chsh -s "$(command -v zsh)" "$USER" >/dev/null 2>&1; then
      ok "Default shell changed to zsh for $USER"
    else
      warn "Could not change default shell automatically. You can run: chsh -s \"$(command -v zsh)\" $USER"
    fi
  fi

  ok "Zsh and Oh My Zsh configured"
}

install_sdkman_and_java() {
  if [ ! -d "$HOME/.sdkman" ]; then
    log "Installing SDKMAN!..."
    if ! curl -fsSL https://get.sdkman.io | bash >/dev/null 2>&1; then
      warn "SDKMAN installation failed; will use APT fallback for Zulu 21."
    fi
  fi

  if [ -f "$HOME/.sdkman/bin/sdkman-init.sh" ]; then
    # Load SDKMAN for current shell
    # shellcheck disable=SC1090
    source "$HOME/.sdkman/bin/sdkman-init.sh"
    # Also add to bashrc so sdk is available in bash terminals
    if ! grep -q 'sdkman-init.sh' "$HOME/.bashrc" 2>/dev/null; then
      echo '[ -s "$HOME/.sdkman/bin/sdkman-init.sh" ] && source "$HOME/.sdkman/bin/sdkman-init.sh"' >> "$HOME/.bashrc"
    fi

    local ZULU_21="21-zulu"
    log "Ensuring Zulu Java ($ZULU_21) via SDKMAN..."
    yes | sdk install java "$ZULU_21" >/dev/null 2>&1 || {
      warn "Install via alias failed; trying to detect a specific 21.x Zulu build..."
      local DETECTED
      DETECTED=$(sdk list java | tr -d '\r' | sed 's/\x1B\[[0-9;]*[JKmsu]//g' | grep -Eo '21(\.[0-9]+)*(-ea)?(-fx)?-zulu' | head -n 1 || true)
      if [ -n "${DETECTED:-}" ]; then
        yes | sdk install java "$DETECTED" >/dev/null 2>&1 || true
        ZULU_21="$DETECTED"
      fi
    }
    sdk default java "$ZULU_21" >/dev/null 2>&1 || true
  else
    warn "SDKMAN not available; using APT fallback for Java."
    install_zulu21_via_apt
  fi

  if ! command -v java >/dev/null 2>&1; then
    err "Java not found after installation attempts."
  else
    ok "Java configured: $(java -version 2>&1 | head -n 1)"
  fi
}

install_zulu21_via_apt() {
  log "Installing Azul Zulu 21 via APT (fallback)..."
  # Add Azul repo key and source list if missing
  if [ ! -f "/usr/share/keyrings/azul.gpg" ]; then
    $SUDO mkdir -p /usr/share/keyrings
    curl -fsSL https://repos.azul.com/azul-repo.key | $SUDO gpg --dearmor -o /usr/share/keyrings/azul.gpg
  fi
  if [ ! -f "/etc/apt/sources.list.d/azul.list" ]; then
    echo "deb [signed-by=/usr/share/keyrings/azul.gpg] https://repos.azul.com/zulu/deb stable main" | $SUDO tee /etc/apt/sources.list.d/azul.list >/dev/null
  fi
  $SUDO bash -c 'export DEBIAN_FRONTEND=noninteractive; apt-get update -y' >/dev/null
  $SUDO bash -c 'export DEBIAN_FRONTEND=noninteractive; apt-get install -y zulu21-jdk' >/dev/null 2>&1 || true
  # Prefer Zulu 21 via update-alternatives when possible
  if [ -x "/usr/lib/jvm/zulu21-ca-amd64/bin/java" ]; then
    $SUDO update-alternatives --install /usr/bin/java java /usr/lib/jvm/zulu21-ca-amd64/bin/java 21100 >/dev/null 2>&1 || true
    $SUDO update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/zulu21-ca-amd64/bin/javac 21100 >/dev/null 2>&1 || true
    $SUDO update-alternatives --set java /usr/lib/jvm/zulu21-ca-amd64/bin/java >/dev/null 2>&1 || true
    $SUDO update-alternatives --set javac /usr/lib/jvm/zulu21-ca-amd64/bin/javac >/dev/null 2>&1 || true
  fi
  if [ -d "/usr/lib/jvm/zulu21-ca-amd64" ]; then
    # Persist for new shells
    if ! grep -q 'JAVA_HOME=.*/zulu21' "$HOME/.zshrc" 2>/dev/null; then
      echo 'export JAVA_HOME=/usr/lib/jvm/zulu21-ca-amd64' >> "$HOME/.zshrc"
      echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> "$HOME/.zshrc"
    fi
    if ! grep -q 'JAVA_HOME=.*/zulu21' "$HOME/.bashrc" 2>/dev/null; then
      echo 'export JAVA_HOME=/usr/lib/jvm/zulu21-ca-amd64' >> "$HOME/.bashrc"
      echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> "$HOME/.bashrc"
    fi
    # Apply to current session
    export JAVA_HOME=/usr/lib/jvm/zulu21-ca-amd64
    export PATH="$JAVA_HOME/bin:$PATH"
  fi
}

install_mysql_and_setup_db() {
  if ! dpkg -s mysql-server >/dev/null 2>&1; then
    log "Installing MySQL Server..."
    apt_install mysql-server
  else
    ok "MySQL Server already installed"
  fi

  # Start MySQL service (handle both systemctl and service)
  if command -v systemctl >/dev/null 2>&1; then
    $SUDO systemctl enable --now mysql >/dev/null 2>&1 || $SUDO systemctl restart mysql >/dev/null 2>&1 || true
  else
    $SUDO service mysql start >/dev/null 2>&1 || $SUDO service mysql restart >/dev/null 2>&1 || true
  fi

  log "Waiting for MySQL to be ready..."
  for i in $(seq 1 30); do
    if $SUDO mysqladmin ping >/dev/null 2>&1; then
      break
    fi
    sleep 1
  done

  log "Configuring database and user..."
  local SQL
  SQL="CREATE DATABASE IF NOT EXISTS \`${DB_NAME}\` CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;\n"
  SQL+="CREATE USER IF NOT EXISTS '${DB_USER}'@'localhost' IDENTIFIED BY '${DB_PASS}';\n"
  SQL+="CREATE USER IF NOT EXISTS '${DB_USER}'@'%' IDENTIFIED BY '${DB_PASS}';\n"
  SQL+="GRANT ALL PRIVILEGES ON \`${DB_NAME}\`.* TO '${DB_USER}'@'localhost';\n"
  SQL+="GRANT ALL PRIVILEGES ON \`${DB_NAME}\`.* TO '${DB_USER}'@'%';\n"
  SQL+="FLUSH PRIVILEGES;\n"

  if $SUDO mysql -u root -e "$SQL" >/dev/null 2>&1; then
    ok "Database \`${DB_NAME}\` and user '${DB_USER}' ensured"
  else
    warn "Attempt with root unix socket failed; retrying with sudo mysql --protocol=socket..."
    if $SUDO mysql --protocol=socket -u root -e "$SQL" >/dev/null 2>&1; then
      ok "Database and user ensured (socket mode)"
    else
      err "Failed to configure MySQL automatically. Please run the SQL manually as root:" && echo -e "$SQL"
    fi
  fi

  # Verify TCP login for the created user (avoid socket permission issues)
  if mysql -h 127.0.0.1 -P 3306 -u "${DB_USER}" -p"${DB_PASS}" -e "SHOW DATABASES LIKE '${DB_NAME}';" >/dev/null 2>&1; then
    ok "Verified MySQL login for user '${DB_USER}' via TCP"
  else
    warn "Could not verify TCP login for user '${DB_USER}'. Ensure MySQL is listening on 127.0.0.1:3306 and try: mysql -h 127.0.0.1 -u ${DB_USER} -p${DB_PASS}"
  fi
}

install_gemini_cli() {
  if ! command -v npm >/dev/null 2>&1; then
    warn "npm not found. Skipping gemini-cli install. Install Node.js (>=18) and re-run to get @google/gemini-cli."
    return 0
  fi

  if command -v gemini >/dev/null 2>&1; then
    ok "gemini CLI already installed ($(gemini --version 2>/dev/null || echo 'unknown'))"
    return 0
  fi

  log "Installing @google/gemini-cli globally via npm..."
  npm install -g @google/gemini-cli >/dev/null 2>&1 && ok "gemini CLI installed" || warn "Failed to install gemini CLI. You can try: npm install -g @google/gemini-cli"
}

main() {
  log "Starting X-Judge environment setup..."
  ensure_packages
  install_zsh_and_ohmyzsh
  install_sdkman_and_java
  install_mysql_and_setup_db
  install_gemini_cli

  echo
  ok "Setup complete. Summary:"
  echo "- Shell: zsh with Oh My Zsh + plugins (git, autosuggestions, syntax-highlighting, completions)"
  echo "- Java: $(java -version 2>&1 | head -n 1)"
  echo "- MySQL: schema '${DB_NAME}', user '${DB_USER}' with password '${DB_PASS}'"
  echo "- gemini CLI: $(command -v gemini >/dev/null 2>&1 && gemini --version 2>/dev/null || echo 'not installed')"
  echo
  echo "Tip: Open a new terminal to start using zsh. If default shell didn't change, run: chsh -s \"$(command -v zsh)\" $USER"
}

main "$@"
