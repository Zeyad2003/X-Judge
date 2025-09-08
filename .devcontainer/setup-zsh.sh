#!/bin/bash

# Enhanced zsh setup script for X-Judge dev container
# Sets up zsh with Oh My Zsh and useful plugins for development

set -euo pipefail

# Colors for output
COLOR_BLUE="\033[1;34m"
COLOR_GREEN="\033[1;32m"
COLOR_YELLOW="\033[1;33m"
COLOR_RESET="\033[0m"

log() { echo -e "${COLOR_BLUE}==>${COLOR_RESET} $*"; }
ok() { echo -e "${COLOR_GREEN}✔${COLOR_RESET} $*"; }
warn() { echo -e "${COLOR_YELLOW}⚠${COLOR_RESET} $*"; }

# Determine if we need sudo
if [ "$EUID" -eq 0 ]; then
    SUDO=""
else
    SUDO="sudo"
fi

# Install zsh if not present
if ! command -v zsh >/dev/null 2>&1; then
    log "Installing zsh..."
    $SUDO apt-get update -qq
    $SUDO apt-get install -y zsh curl git
    ok "zsh installed"
else
    ok "zsh already available"
fi

# Install Oh My Zsh for the vscode user
USER_HOME="/home/vscode"
if [ "$USER" = "root" ]; then
    USER_HOME="/root"
fi

if [ ! -d "$USER_HOME/.oh-my-zsh" ]; then
    log "Installing Oh My Zsh..."
    
    # Set environment variables for unattended installation
    export RUNZSH=no
    export CHSH=no
    export KEEP_ZSHRC=yes
    
    # Install Oh My Zsh
    sh -c "$(curl -fsSL https://raw.githubusercontent.com/ohmyzsh/ohmyzsh/master/tools/install.sh)" "" --unattended
    ok "Oh My Zsh installed"
else
    ok "Oh My Zsh already installed"
fi

# Set up zsh plugins
ZSH_CUSTOM="$USER_HOME/.oh-my-zsh/custom"

log "Installing zsh plugins..."

# Install zsh-autosuggestions
if [ ! -d "$ZSH_CUSTOM/plugins/zsh-autosuggestions" ]; then
    git clone --depth=1 https://github.com/zsh-users/zsh-autosuggestions.git "$ZSH_CUSTOM/plugins/zsh-autosuggestions"
fi

# Install zsh-syntax-highlighting
if [ ! -d "$ZSH_CUSTOM/plugins/zsh-syntax-highlighting" ]; then
    git clone --depth=1 https://github.com/zsh-users/zsh-syntax-highlighting.git "$ZSH_CUSTOM/plugins/zsh-syntax-highlighting"
fi

# Install zsh-completions
if [ ! -d "$ZSH_CUSTOM/plugins/zsh-completions" ]; then
    git clone --depth=1 https://github.com/zsh-users/zsh-completions.git "$ZSH_CUSTOM/plugins/zsh-completions"
fi

# Install history-substring-search
if [ ! -d "$ZSH_CUSTOM/plugins/zsh-history-substring-search" ]; then
    git clone --depth=1 https://github.com/zsh-users/zsh-history-substring-search.git "$ZSH_CUSTOM/plugins/zsh-history-substring-search"
fi

ok "zsh plugins installed"

# Configure .zshrc
log "Configuring .zshrc..."

# Ensure .zshrc exists
if [ ! -f "$USER_HOME/.zshrc" ]; then
    touch "$USER_HOME/.zshrc"
fi

# Backup existing .zshrc
cp "$USER_HOME/.zshrc" "$USER_HOME/.zshrc.backup.$(date +%s)" 2>/dev/null || true

# Create a proper .zshrc configuration
cat > "$USER_HOME/.zshrc" << 'EOF'
# Path to your oh-my-zsh installation.
export ZSH="$HOME/.oh-my-zsh"

# Set name of the theme to load (agnoster for powerline-style prompt)
ZSH_THEME="agnoster"

# Uncomment the following line to use case-sensitive completion.
# CASE_SENSITIVE="true"

# Uncomment the following line to use hyphen-insensitive completion.
# Case-sensitive completion must be off. _ and - will be interchangeable.
HYPHEN_INSENSITIVE="true"

# Uncomment the following line to disable bi-weekly auto-update checks.
DISABLE_AUTO_UPDATE="true"

# Uncomment the following line to automatically update without prompting.
# DISABLE_UPDATE_PROMPT="true"

# Uncomment the following line to change how often to auto-update (in days).
# export UPDATE_ZSH_DAYS=13

# Uncomment the following line if pasting URLs and other text is messed up.
# DISABLE_MAGIC_FUNCTIONS="true"

# Uncomment the following line to disable colors in ls.
# DISABLE_LS_COLORS="true"

# Uncomment the following line to disable auto-setting terminal title.
# DISABLE_AUTO_TITLE="true"

# Uncomment the following line to enable command auto-correction.
ENABLE_CORRECTION="true"

# Uncomment the following line to display red dots whilst waiting for completion.
COMPLETION_WAITING_DOTS="true"

# Uncomment the following line if you want to disable marking untracked files
# under VCS as dirty. This makes repository status check for large repositories
# much, much faster.
# DISABLE_UNTRACKED_FILES_DIRTY="true"

# Add zsh-completions to fpath before Oh My Zsh sourcing
fpath+=${ZSH_CUSTOM:-${ZSH:-~/.oh-my-zsh}/custom}/plugins/zsh-completions/src

# Which plugins would you like to load?
# Standard plugins can be found in $ZSH/plugins/
# Custom plugins may be added to $ZSH_CUSTOM/plugins/
plugins=(
    git
    zsh-autosuggestions
    zsh-syntax-highlighting
    zsh-completions
    colored-man-pages
    command-not-found
    gradle
    docker
    docker-compose
    history-substring-search
    sudo
)

# Load Oh My Zsh
source $ZSH/oh-my-zsh.sh

# User configuration

# Preferred editor for local and remote sessions
if [[ -n $SSH_CONNECTION ]]; then
  export EDITOR='vim'
else
  export EDITOR='code'
fi

# Set personal aliases, overriding those provided by oh-my-zsh libs,
# plugins, and themes. Aliases can be placed here, though oh-my-zsh
# users are encouraged to define aliases within the ZSH_CUSTOM folder.

# Navigation aliases
alias ..='cd ..'
alias ...='cd ../..'
alias ....='cd ../../..'
alias .....='cd ../../../..'

# Enhanced ls aliases
alias ll='ls -alF'
alias la='ls -A'
alias l='ls -CF'
alias ls='ls --color=auto'

# System aliases
alias cls='clear'
alias grep='grep --color=auto'
alias fgrep='fgrep --color=auto'
alias egrep='egrep --color=auto'

# Git aliases (additional to oh-my-zsh git plugin)
alias gst='git status'
alias gco='git checkout'
alias gcb='git checkout -b'
alias gaa='git add .'
alias gcm='git commit -m'
alias gp='git push'
alias gl='git pull'

# Gradle aliases
alias gw='./gradlew'
alias gwb='./gradlew build'
alias gwr='./gradlew run'
alias gwt='./gradlew test'
alias gwc='./gradlew clean'
alias gwbt='./gradlew build test'

# Docker aliases
alias dc='docker compose'
alias dcu='docker compose up'
alias dcub='docker compose up --build'
alias dcd='docker compose down'
alias dcl='docker compose logs'
alias dcp='docker compose ps'

# Spring Boot aliases
alias bootrun='./gradlew bootRun'
alias bootjar='./gradlew bootJar'

# Development environment
export PAGER=less
export LANG=en_US.UTF-8

# History configuration
HISTSIZE=50000
SAVEHIST=50000
setopt SHARE_HISTORY
setopt HIST_IGNORE_DUPS
setopt HIST_IGNORE_ALL_DUPS
setopt HIST_IGNORE_SPACE
setopt HIST_VERIFY
setopt EXTENDED_HISTORY

# Enable auto-completion
autoload -U compinit && compinit

# Case-insensitive completion
zstyle ':completion:*' matcher-list 'm:{a-zA-Z}={A-Za-z}' 'r:|=*' 'l:|=* r:|=*'

# Colored completion (if you have GNU ls)
zstyle ':completion:*' list-colors "${(s.:.)LS_COLORS}"

# Menu-driven completion
zstyle ':completion:*' menu select

# Completion for kill command
zstyle ':completion:*:*:kill:*:processes' list-colors '=(#b) #([0-9]#) ([0-9a-z-]#)*=01;34=0=01'
zstyle ':completion:*:*:*:*:processes' command "ps -u $USER -o pid,user,comm -w -w"

# Custom agnoster theme modifications
DEFAULT_USER="vscode"
AGNOSTER_CONTEXT_BG="black"
AGNOSTER_CONTEXT_FG="default"

# # Welcome message for X-Judge development
# echo ""
# echo "🚀 Welcome to X-Judge Development Environment"
# echo "├─ 💻 Container: $(uname -n)"
# echo "├─ 📁 Workspace: $(pwd)"
# if command -v java >/dev/null 2>&1; then
#     echo "├─ ☕ Java: $(java -version 2>&1 | head -n 1 | cut -d'"' -f2)"
# fi
# if command -v gradle >/dev/null 2>&1; then
#     echo "├─ 🐘 Gradle: $(gradle --version | grep Gradle | cut -d' ' -f3)"
# fi
# if command -v docker >/dev/null 2>&1; then
#     echo "├─ 🐳 Docker: $(docker --version | cut -d' ' -f3 | cut -d',' -f1)"
# fi
# echo "└─ 🎨 Theme: Agnoster with enhanced features"
# echo ""
# echo "💡 Quick commands: gw (gradlew), dc (docker compose), cls (clear)"
# echo ""
EOF

# Set proper ownership
if [ "$USER" != "vscode" ] && id -u vscode >/dev/null 2>&1; then
    $SUDO chown -R vscode:vscode "$USER_HOME/.oh-my-zsh" "$USER_HOME/.zshrc" 2>/dev/null || true
fi

ok ".zshrc configured"

# Try to set zsh as default shell
log "Setting zsh as default shell..."
if [ -f /etc/passwd ] && command -v chsh >/dev/null 2>&1; then
    if [ "$USER" = "root" ]; then
        chsh -s "$(which zsh)" || warn "Could not change shell for root"
    elif id -u vscode >/dev/null 2>&1; then
        $SUDO chsh -s "$(which zsh)" vscode || warn "Could not change shell for vscode user"
    fi
    ok "Default shell set to zsh"
else
    warn "Could not set default shell automatically"
fi

log "Creating shell restart helper..."
cat > "$USER_HOME/restart-shell.sh" << 'EOF'
#!/bin/bash
echo "🔄 Restarting shell with zsh..."
exec zsh -l
EOF
chmod +x "$USER_HOME/restart-shell.sh"

echo ""
ok "Zsh setup completed successfully!"
echo ""
echo "📝 Summary:"
echo "   • Oh My Zsh installed with useful plugins"
echo "   • Syntax highlighting and autosuggestions enabled"
echo "   • Git, Gradle, and Docker aliases configured"
echo "   • Development-friendly settings applied"
echo ""
echo "🔄 To start using zsh immediately, run: exec zsh"
echo "   Or restart your terminal to use zsh by default"
echo ""
