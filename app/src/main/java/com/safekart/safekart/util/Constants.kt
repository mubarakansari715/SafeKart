package com.safekart.safekart.util

object Constants {
    const val APP_NAME = "SafeKart"
    
    // API Configuration
    // For Android Studio Emulator: use 10.0.2.2 which maps to localhost (127.0.0.1)
    // For Genymotion Emulator: use your Mac's actual IP address (found below)
    // For Physical Device: use your computer's IP address
    // To find your IP: Run "ifconfig" on Mac/Linux or "ipconfig" on Windows
    
    // API Base URL - Update based on your setup:
    // Option 1: Android Studio Emulator (standard) - use 10.0.2.2 for localhost
    // Option 2: Genymotion Emulator - use your Mac's IP address
    // Option 3: Physical Device - use your computer's IP address
    // To find your IP: Run "ifconfig" on Mac/Linux or "ipconfig" on Windows
    
    // Default: Android Studio Emulator
    const val API_BASE_URL = "http://10.0.2.2:3000/api/v1/"
    
    // Alternative: For Genymotion or Physical Device (uncomment and update IP)
    // const val API_BASE_URL = "http://YOUR_IP_ADDRESS:3000/api/v1/"
    
    const val API_TIMEOUT_SECONDS = 30L
}

