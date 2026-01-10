package com.safekart.safekart.util

object Constants {
    const val APP_NAME = "SafeKart"
    
    // API Configuration
    // For Android Studio Emulator: use 10.0.2.2 which maps to localhost (127.0.0.1)
    // For Genymotion Emulator: use your Mac's actual IP address (found below)
    // For Physical Device: use your computer's IP address
    // To find your IP: Run "ifconfig" on Mac/Linux or "ipconfig" on Windows
    
    // Option 1: Android Studio Emulator (standard)
    // const val API_BASE_URL = "http://10.0.2.2:3000/api/"
    
    // Option 2: Genymotion Emulator (use your Mac's IP)
    const val API_BASE_URL = "http://172.21.103.29:3000/api/"
    
    // Option 3: Physical Device (replace with your actual IP if different)
    // const val API_BASE_URL = "http://172.21.103.29:3000/api/"
    
    const val API_TIMEOUT_SECONDS = 30L
}

