package com.safekart.safekart.ui.presentation.auth.forgotpassword

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.safekart.safekart.ui.presentation.components.AnimatedAppLabel
import com.safekart.safekart.ui.presentation.components.WavyBackground
import com.safekart.safekart.ui.theme.Background
import com.safekart.safekart.ui.theme.SafeKartTheme

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    onBackToLogin: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    ForgotPasswordContent(
        email = uiState.email,
        emailError = uiState.emailError,
        isLoading = uiState.isLoading,
        isEmailSent = uiState.isEmailSent,
        errorMessage = uiState.errorMessage,
        isFormValid = uiState.isFormValid,
        onEmailChange = { viewModel.updateEmail(it) },
        onSendResetEmail = { viewModel.sendPasswordResetEmail() },
        onBackToLogin = {
            viewModel.resetState()
            onBackToLogin()
        }
    )
}

@Composable
fun ForgotPasswordContent(
    email: String,
    emailError: String?,
    isLoading: Boolean,
    isEmailSent: Boolean,
    errorMessage: String?,
    isFormValid: Boolean,
    onEmailChange: (String) -> Unit,
    onSendResetEmail: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val imeInsets = WindowInsets.ime
    val keyboardHeight = with(density) { imeInsets.getBottom(this).toDp() }
    val isKeyboardOpen = keyboardHeight > 0.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Wavy background pattern
        WavyBackground(
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            // Top section - 30% of screen for Logo and Title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Lottie Animation - Always visible, infinite loop
                    val composition by rememberLottieComposition(
                        LottieCompositionSpec.Asset("shopping_girl.json")
                    )
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier.size(100.dp)
                    )
                    
                    // Animated SellMyProduct label - Always show
                    AnimatedAppLabel()
                }
            }

            // Bottom section - 70% of screen for White Box
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
                    .verticalScroll(scrollState),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .padding(top = 8.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Back Button
                    IconButton(
                        onClick = onBackToLogin,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Title
                    Text(
                        text = "Forgot Password?",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Description
                    Text(
                        text = "Don't worry! Enter your email and we'll send you a link to reset your password.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Success Message
                    if (isEmailSent) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "✓ Email Sent!",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "We've sent a password reset link to your email. Please check your inbox and follow the instructions.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    // Error Message
                    if (errorMessage != null && !isEmailSent) {
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    // Email Field (only show if email not sent)
                    if (!isEmailSent) {
                        OutlinedTextField(
                            value = email,
                            onValueChange = onEmailChange,
                            label = { Text("Email") },
                            placeholder = { Text("Enter your email") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "Email",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            singleLine = true,
                            isError = emailError != null,
                            supportingText = emailError?.let { { Text(it) } },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    onSendResetEmail()
                                }
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isLoading,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                errorBorderColor = MaterialTheme.colorScheme.error
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Send Reset Email Button
                        Button(
                            onClick = onSendResetEmail,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            enabled = !isLoading && isFormValid,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Text(
                                    text = "Send Reset Link",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Back to Login Link
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Remember your password? ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Sign in",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { onBackToLogin() }
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Forgot Password Screen",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ForgotPasswordScreenPreview() {
    SafeKartTheme {
        ForgotPasswordContent(
            email = "",
            emailError = null,
            isLoading = false,
            isEmailSent = false,
            errorMessage = null,
            isFormValid = false,
            onEmailChange = {},
            onSendResetEmail = {},
            onBackToLogin = {}
        )
    }
}

@Preview(
    name = "Forgot Password - Email Sent",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ForgotPasswordEmailSentPreview() {
    SafeKartTheme {
        ForgotPasswordContent(
            email = "user@email.com",
            emailError = null,
            isLoading = false,
            isEmailSent = true,
            errorMessage = null,
            isFormValid = true,
            onEmailChange = {},
            onSendResetEmail = {},
            onBackToLogin = {}
        )
    }
}

