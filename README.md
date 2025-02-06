# GitHub_AI_Bot

This repository contains a GitHub App written in Python using Flask. The bot listens to GitHub webhook events (push, pull request, and issue comments) and uses OpenAI's GPT-4 to analyze Java code for potential security misuses. It also supports admin commands to update or merge code based on AI analysis.

## Features

- **Automated Pull Requests:**  
  When new branches are pushed that include Java file changes, the bot automatically creates a pull request with an AI-generated summary.

- **Pull Request Analysis:**  
  The bot analyzes Java code changes in pull requests for security issues (e.g., hardcoded keys, weak encryption practices) and posts the results as comments.

- **Issue Comment Commands:**  
  Admin-only commands can be issued in issue or pull request comments. Commands include:
  - `@AIBot analyze repo`: Analyze the entire repository for common security misuses.
  - `@AIBot update code`: Update code in a pull request based on admin instructions.
  - `@AIBot update`: Retrieve and post the current code of a file.
  - `@AIBot merge code`:  
    - For PR comments, merge provided code snippets.
    - For Issue comments, the bot extracts the Java file name from the issue body, retrieves its content, runs analysis (using `analyze_code_no_issue`), and then posts the analysis result.
  - `@AIBot analyze code`:  
    Extracts the Java file name from the issue body (or PR description if applicable), retrieves the file content from the repository, analyzes it using GPT-4, and posts the analysis result.

## How to Install the GitHub App

Follow these steps to install the GitHub AI Bot on your repositories:

  - Step 1: Go to the Installation URL

    Navigate to the installation page for the GitHub App:
  
    [https://github.com/apps/GitHub_AI_Bot](https://github.com/apps/GitHub_AI_Bot)

  - Step 2: Install the App

    On the installation page, click the **Install** (or **Configure**) button. You will be prompted to choose whether to install the app on:

    - All repositories:  
      The app will have access to every repository in your account or organization.
  
    - Selected repositories:  
      You can select the specific repositories you want the app to have access to.

  - Step 3: Grant Permissions

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

## Admin Commands

Only the repository owner (admin) can issue commands. Example commands include:

- **`@AIBot analyze repo`**: Analyze the repository for security misuses.
- **`@AIBot update code`**: Update code in a pull request based on instructions.
- **`@AIBot update`**: Retrieve and display the current code.
- **`@AIBot merge code`**:  
  - **For PR comments:** Merges a provided code snippet.  
  - **For Issue comments:**  
    - Extracts the Java file name from the issue body, retrieves the file content from the repository, and analyzes it using GPT-4.
    - (See the README instructions in the code for more details.)
- **`@AIBot analyze code`**:  
  Extracts the file name from the issue body (or PR description), retrieves the file content, analyzes it using GPT-4, and posts the analysis result.

## Contributing

Contributions, bug fixes, and feature enhancements are welcome. Please fork the repository and submit a pull request with your changes.

## License

This project is licensed under the [MIT License](LICENSE).
