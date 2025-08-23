package com.emtelco.pokeapiemtelco.core

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity


class AuthHelper (private val context: Context){

    private fun canAuthenticate(): Boolean {
        return BiometricManager.from(context).canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG
                        or BiometricManager.Authenticators.DEVICE_CREDENTIAL
            ) == BiometricManager.BIOMETRIC_SUCCESS

    }

    fun authenticate( activity: FragmentActivity, onSucceeded: () -> Unit, onFailed: () -> Unit) {

        if (canAuthenticate()) {

            val prompt = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación de compra")
                .setAllowedAuthenticators(
                    BiometricManager.Authenticators.BIOMETRIC_STRONG
                            or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                )
                .build()


            val biometricPrompt = BiometricPrompt(
                activity,
                ContextCompat.getMainExecutor(context),
                object : BiometricPrompt.AuthenticationCallback()
                {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult)
                    {
                        super.onAuthenticationSucceeded(result)
                        onSucceeded()
                    }
                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        onFailed()
                    }
                }
            )

            biometricPrompt.authenticate(prompt)
        }
    }

}