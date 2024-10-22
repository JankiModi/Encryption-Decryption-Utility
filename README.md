# Encryption and Decryption Utility

## Overview

The **Encryption and Decryption Utility** is a Java-based application designed for secure file handling. It allows users to encrypt and decrypt text files with password protection, ensuring sensitive data remains private and secure. The utility includes user authentication features, allowing users to sign up, log in, and manage their files seamlessly.

## Features

- **User Authentication**: Secure sign-up and login functionalities for managing user access.
- **File Management**: Users can create, read, and manage encrypted text files within their respective directories.
- **Password Protection**: Each file is secured with a password, requiring the correct password to decrypt and access the contents.
- **Input Validation**: The application validates user inputs for file names and ensures they conform to the required formats.
- **Error Handling**: Graceful handling of exceptions and user input errors to enhance the user experience.

## Technologies Used

- Java
- BufferedReader and FileReader for file handling
- HashMap for storing user data and file paths
- Regular expressions for input validation

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/JankiModi/Encryption-Decryption-Utility.git
   ```
2. Navigate to the project directory:
   ```bash
   cd Encryption-Decryption-Utility
   ```
3. Compile the Java files:
   ```bash
   javac Project_java.java
   ```
4. Run the application::
   ```bash
   java Project_java
   ```

## Usage:
1. Upon running the application, users are presented with the following options:

  - 1: Sign up for a new account
  - 2: Log in to an existing account
  - 3: Exit the application
2. After signing up or logging in, users can choose to:

  - Add a new encrypted file
  - Read from an existing encrypted file
3. When reading a file, users must provide the correct password to decrypt and view its contents.

## Example:
- Sign up:

  - Input user credentials to create a new account.
- Log in:

  - Enter credentials to access the application.
- Add File:

  - Specify the file path and enter the content, which will be encrypted and saved.
- Read File:

  - Enter the file name and provide the password to decrypt and display the content.
