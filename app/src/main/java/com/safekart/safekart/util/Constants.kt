package com.safekart.safekart.util

import com.safekart.safekart.BuildConfig

object Constants {
    const val APP_NAME = "SafeKart"

    /**
     * API base URL. Default: 10.0.2.2 (Android emulator → host localhost).
     * Override: in project root gradle.properties add:
     *   API_BASE_URL=http://YOUR_IP:3000/api/v1/
     * Use your machine's IP (e.g. from ifconfig/ipconfig) for physical device or when 10.0.2.2 fails.
     */
    val API_BASE_URL: String = BuildConfig.API_BASE_URL

    const val API_TIMEOUT_SECONDS = 45L
}

