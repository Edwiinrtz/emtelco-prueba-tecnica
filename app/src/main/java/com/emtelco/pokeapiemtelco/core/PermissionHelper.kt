package com.emtelco.pokeapiemtelco.core

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import javax.inject.Inject


class PermissionHelper @Inject constructor(private val context: Context) {

    fun requestPermission(permission: String, requestPermissionLauncher: ActivityResultLauncher<String>, activity: FragmentActivity) {
        when {
            ContextCompat.checkSelfPermission(
                context, permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i("PermissionHelper", "$permission PERMISSION ALREADY GRANTED")
            }

            activity.shouldShowRequestPermissionRationale(permission) -> {
                // Action to take when permission was denied
                if (permission == Manifest.permission.POST_NOTIFICATIONS) {
                    Toast.makeText(
                        context,
                        "No tenemos permitido enviarte notificaciones :'(",
                        Toast.LENGTH_LONG
                    ).show()
                }
                Log.i("PermissionHelper", "$permission PERMISSION DENIED")
            }
            else -> {
                // Request permission
                requestPermissionLauncher.launch(permission)
            }
        }
    }

}