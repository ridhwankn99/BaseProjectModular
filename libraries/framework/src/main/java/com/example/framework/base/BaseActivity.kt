package com.example.framework.base

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

abstract class BaseActivity : AppCompatActivity() {

    private var coarsePermission = Manifest.permission.ACCESS_COARSE_LOCATION
    private var finePermission = Manifest.permission.ACCESS_FINE_LOCATION
    private var cameraPermission = Manifest.permission.CAMERA
    private var readStoragePermission = Manifest.permission.READ_EXTERNAL_STORAGE
    private var writeStoragePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private var rationaleDialog: AlertDialog? = null
    private var denyDialog: AlertDialog? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onStart() {
        super.onStart()
        rationaleDialog?.dismiss()
        denyDialog?.dismiss()
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        when {
            ActivityCompat.checkSelfPermission(this, coarsePermission) == PackageManager.PERMISSION_GRANTED -> {
                checkCameraPermission()
            }
            shouldShowRequestPermissionRationale(coarsePermission) -> {
                showLocationRationaleDialog()
            }
            else -> {
                requestPermissionLauncher.launch(finePermission)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            checkCameraPermission()
        } else {
            if (shouldShowRequestPermissionRationale(coarsePermission)) {
                showLocationRationaleDialog()
            } else {
                showLocationDeniedDialog()
            }
        }
    }

    private fun showLocationRationaleDialog() {
        rationaleDialog = AlertDialog.Builder(this)
            .setMessage("Penggunaan lokasi harus diizinkan untuk menggunakan aplikasi. Izinkan Penggunaan Lokasi?")
            .setPositiveButton("Ya") { _, _ ->
                requestPermissionLauncher.launch(finePermission)
            }
            .setNegativeButton("Tidak") { _, _ ->
                showLocationDeniedDialog()
            }
            .setCancelable(false)
            .create()
        rationaleDialog?.show()
    }

    private fun showLocationDeniedDialog() {
        denyDialog = AlertDialog.Builder(this)
            .setMessage("Penggunaan lokasi harus diizinkan untuk menggunakan aplikasi.")
            .setPositiveButton("Pengaturan") { _, _ ->
                openPermissionSettings()
            }
            .setNegativeButton("Keluar Aplikasi") { _, _ ->
                finishAffinity()
            }
            .setCancelable(false)
            .create()
        denyDialog?.show()
    }

    private fun checkStoragePermission() {
        val isPermissionGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val readPermissionGranted =
                ContextCompat.checkSelfPermission(this, readStoragePermission) == PackageManager.PERMISSION_GRANTED
            val writePermissionGranted =
                ContextCompat.checkSelfPermission(this, writeStoragePermission) == PackageManager.PERMISSION_GRANTED
            readPermissionGranted && writePermissionGranted
        }

        if (isPermissionGranted) return
        requestStoragePermission()
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                data = Uri.parse("package:${applicationContext.packageName}")
            }
            resultPermissionStorage.launch(intent)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(readStoragePermission, writeStoragePermission),
                2296
            )
        }
    }

    private val resultPermissionStorage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                // Permission granted
            } else {
                showStorageDeniedDialog()
            }
        }
    }

    private fun showStorageDeniedDialog() {
        denyDialog = AlertDialog.Builder(this)
            .setMessage("Penggunaan akses file berkas harus diizinkan untuk menggunakan aplikasi ini.")
            .setPositiveButton("Pengaturan") { _, _ ->
                openPermissionSettings()
            }
            .setNegativeButton("Keluar Aplikasi") { _, _ ->
                finishAffinity()
            }
            .setCancelable(false)
            .create()
        denyDialog?.show()
    }

    private fun checkCameraPermission() {
        when {
            ActivityCompat.checkSelfPermission(this, cameraPermission) == PackageManager.PERMISSION_GRANTED -> {
                checkStoragePermission()
            }
            shouldShowRequestPermissionRationale(cameraPermission) -> {
                showDialogDeniedCameraPermission()
            }
            else -> {
                requestPermissionCameraLauncher.launch(cameraPermission)
            }
        }
    }

    private val requestPermissionCameraLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            showDialogDeniedCameraPermission()
        }
    }

    private fun showDialogDeniedCameraPermission() {
        rationaleDialog = AlertDialog.Builder(this)
            .setMessage("Penggunaan kamera harus diizinkan untuk scan paket. Izinkan Penggunaan kamera?")
            .setPositiveButton("Izinkan") { _, _ ->
                openPermissionSettings()
            }
            .setNegativeButton("Tidak") { _, _ ->
                rationaleDialog?.dismiss()
            }
            .setCancelable(false)
            .create()
        rationaleDialog?.show()
    }

    private fun openPermissionSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        resultPermissionCamera.launch(intent)
    }

    private val resultPermissionCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            checkCameraPermission()
        } else {
            showDialogDeniedCameraPermission()
        }
    }
}
