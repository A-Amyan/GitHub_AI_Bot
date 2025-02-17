# GitHub_AI_Bot

This repository contains a GitHub App written in Python using Flask. The bot listens to GitHub webhook events (push, pull request, and issue comments) and uses OpenAI's GPT-4 to analyze Java code for potential security misuses. It also supports admin commands to update or merge code based on AI analysis.

## Features

- **GitHub App Integration & Authentication:**
  - Uses environment variables (`GITHUB_APP_ID`, `GITHUB_PRIVATE_KEY`, `OPENAI_API_KEY`) for secure configuration.
  - Authenticates as a GitHub App using PyGithub and a custom `Auth.AppAuth`.
- **Flask-Based Webhook Server:**
  - Listens for GitHub webhook events on `/webhook` and a health check endpoint `/ping`.
  - Supports the following GitHub events:
    - **Push Events:** Automatically triggers actions on branch pushes.
    - **Pull Request Events:** Analyzes code changes in PRs.
    - **Issue Comment Events:** Responds to chat commands and admin instructions.
- **Push Event Handling:**
  - **Automatic Pull Request Creation:** When a push occurs on a non-default branch, the bot automatically creates a pull request.
  - **Multi-File AI-Generated PR Description:**
    - Collect all changed Java files from the push.
    - Generates a short, security-focused PR description that summarizes all impacted Java files using OpenAI GPT‑4.
- **Pull Request Event Handling:**
  - **Per-File Security Analysis:**
    - Iterates through each changed Java file in the pull request.
    - Retrieves file contents and splits code into logical sections (using structural splitting by class or method definitions).
    - Analyzes each section for potential vulnerabilities, misuses of Java Cryptography Architecture (JCA) APIs, and security weaknesses.
  - **Aggregated Reporting:**
     - Generate a JSON-formatted report for each file.
     - Post individual comments on the pull request with the security analysis for each impacted Java file.
- **Issue Comment Command Handling:**
  - **In-Memory Conversation Context:**
     - Maintains conversation history per repository and issue number to ensure coherent discussions.
  - **Admin Commands (Restricted to Repository Owner):**
    - `@AIBot analyze repo`: Automatically scans the entire repository for Java files, analyzes each for security vulnerabilities, and opens issues with the findings.
    - `@AIBot analyze file`: Analyzes a specific file (extracted from the issue or PR body) for security vulnerabilities.
    - `@AIBot update code` / `@AIBot update`: Uses AI to generate updated code based on user instructions.
    - `@AIBot merge code`: Merges AI-proposed corrections into the repository.
  - **Strict Security-Only Discussion:**
     - The bot’s system instructions enforce that all conversations remain focused on security analysis and vulnerability remediation.
     - If topics fall outside Java security, the bot responds by indicating that only security-related discussion is supported.
- **AI-Powered Analysis & Summarization:**
  - **GPT‑4 Integration:**
    - Generates concise PR descriptions based on Java file snippets.
    - Performs in-depth security analysis on code sections, identifying vulnerabilities, misuses, and suggesting secure alternatives.
    - Merges findings from different sections to avoid duplication
  - **Custom Prompts:**
    - System and user prompts are tailored to focus on security best practices, cryptography, and vulnerability remediation.
- **Code Merging & Updates:**
  - Supports updating code based on admin correction instructions.
  - Fetches the original code from GitHub, applies minimal changes via GPT‑4, and updates the repository file with a new commit.
- **Robust Error Handling & Logging:**
  - Logs errors (set to DEBUG level for troubleshooting) for operations such as file fetching, GitHub API calls, and AI interactions.
  - Ensures graceful handling of missing fields or authentication issues.

## How to Install the GitHub App

Follow these steps to install the GitHub AI Bot on your repositories:
  - **Step 1:** Go to the Installation URL
    - Navigate to the installation page for the GitHub App:
    [https://github.com/apps/GitHub_AI_Bot](https://github.com/apps/GitHub_AI_Bot)
  - **Step 2:** Install the App
    - On the installation page, click the **Install** (or **Configure**) button. You will be prompted to choose whether to install the app on:
      - **All repositories:**
        The app will have access to every repository in your account or organization.
      - **Selected repositories:**
        You can select the specific repositories you want the app to have access to.
  - **Step 3:** Grant Permissions
    After selecting the repositories, you will see a list of permissions that the app requires (for example, read/write access to issues, pull requests, and repository contents). Review and grant the necessary permissions.
    Once you confirm, the app will be installed and will start receiving webhook events from the repositories it has access to.

## What Happens After Installation?

After installation, the GitHub AI Bot will begin processing webhook events from your repositories. It will:
- Analyze code in pull requests and issues.
- Automatically generate pull request descriptions.
- Execute admin commands (when issued by the repository owner) such as code updates, merges, and analysis.

## Deployment

GitHub AI Bot is already deployed on [Render](https://render.com/). Its publicly accessible URL (for webhook events) is provided during installation. 

## Webhook Endpoints

- **`/webhook`**: Main endpoint for receiving GitHub webhook events (push, pull request, issue comment).
- **`/ping`**: Health-check endpoint.

## Contributing

Contributions, bug fixes, and feature enhancements are welcome. Please fork the repository and submit a pull request with your changes.

## License

This project is licensed under the [MIT License](LICENSE).
