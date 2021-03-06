# Quboo SonarQube Plugin

# What is Quboo?

[Quboo](https://quboo.io) is a platform that helps you improve your code by using gamification techniques to motivate you and your peers to fix your technical debt.

Go to [quboo.io](https://quboo.io) for more details about how Quboo works and give it a try.

# The plugin

## How does it work?

Our platform uses your code analysis results from SonarQube to extract the game statistics, and also create players based on the existent users in this tool.

This plugin runs after each analysis and collect partial information about your SonarQube users and your existing Issues and send them to Quboo. Then, it computes the new scorecards and badges that you will be able to see next time you access your account via login or public link.

## Installing the Plugin

1. Navigate to [Releases](https://github.com/thepracticaldeveloper/quboo-sonarqube-plugin/releases/latest) and download the latest `.jar` file.
2. Remove any previous version of the plugin and put the new jar in the folder `$SONARQUBE_HOME/extensions/plugins`.
3. Restart your SonarQube server.

## Configuration

After you install the plugin you need Administrator rights to enter your Quboo Access and Secret Keys. The plugin needs these so it can link the data to your account. You can find these values under the section *Settings* when you log in as a user in Quboo.

In SonarQube, you have to enter these keys in the section *Administration -> Configuration -> General Settings -> Quboo (tab on the left)*. 

### Servers with "Force User Authentication" enabled

If your SonarQube server has the property `sonar.forceAuthentication` enabled, the Quboo Plugin can't access the API without a valid _token_. First, generate a token in SonarQube from 'My Account' -> 'Security'. Then, enter the token value in the Quboo Plugin configuration section.

Remember: you **don't** need to provide a token if your server allows anonymous access.

## What information do we send?

We do not need much information for you to play the game, so we collect only some details from Users and Issues. We keep this plugin in an open source repository for transparency so you can see at anytime what is the transferred data:

- **Users**: login, name, active.
- **Issues**: key, rule, severity, componentId, resolution, status, debt, author, creationDate, updateDate, closeDate, type, tags.

As you can see, **we DO NOT send anything related to code to the server**. Not even your component names or comments.

## Quboo: Terms and Conditions

You can read the complete [terms and conditions](https://quboo.io/terms) at the Quboo Website.
