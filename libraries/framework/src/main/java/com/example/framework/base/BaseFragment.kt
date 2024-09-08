package com.example.framework.base

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

abstract class BaseCameraFragment : Fragment() {
    private var cameraPermission = Manifest.permission.CAMERA
    private var rationaleDialog: AlertDialog? = null
    private var denyDialog: AlertDialog? = null

    override fun onStart() {
        super.onStart()
        rationaleDialog?.dismiss()
        denyDialog?.dismiss()
        checkCameraPermission()
    }

    private var resultPermissionCamera: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            when (result?.resultCode == Activity.RESULT_OK) {
                else -> {
                    requireActivity().finish()
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

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkCameraPermission() {
        when{
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                cameraPermission
            ) == PackageManager.PERMISSION_GRANTED -> {

            }
            shouldShowRequestPermissionRationale(cameraPermission) -> showDialogDeniedCameraPermission()
            else -> requestPermissionCameraLauncher.launch(cameraPermission)
        }
    }

    private fun showDialogDeniedCameraPermission() {
        if (rationaleDialog == null) {
            rationaleDialog = AlertDialog.Builder(requireActivity())
                .setMessage("Penggunaan kamera harus diizinkan untuk scan paket. Izinkan Penggunaan kamera?")
                .setPositiveButton("Izinkan") { _, _ ->
                    openPermissionSetting()
                }
                .setNegativeButton("Tidak ") { _, _ ->
                    rationaleDialog?.dismiss()
                }
                .setCancelable(false)
                .create()
        }
        if (rationaleDialog?.isShowing == false) {
            rationaleDialog?.show()
        }
    }

    private fun openPermissionSetting() {
        val intentSetting = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireContext().packageName, null)
        )
        intentSetting.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        resultPermissionCamera.launch(intentSetting)
    }
}