package com.example.king_bob_nae.features

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.king_bob_nae.R
import com.example.king_bob_nae.base.BaseActivity
import com.example.king_bob_nae.databinding.ActivityHomeBinding
import com.example.king_bob_nae.features.imagepicker.presentation.ImageListViewModel

class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {
    companion object {
        private const val REQUEST_PERMISSION = 1000
        private const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
        private const val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
        private const val CAMERA = Manifest.permission.CAMERA
    }

    private val imageListViewModel: ImageListViewModel by viewModels()
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private val permissions = arrayOf(
        CAMERA,
        READ_EXTERNAL_STORAGE,
        WRITE_EXTERNAL_STORAGE
    )

    private fun navigate(resId: Int) {
        navController.navigate(resId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermission() {
        if (checkPermission(READ_EXTERNAL_STORAGE) && checkPermission(CAMERA)) {
            navigate(R.id.imagePickerFragment)
        } else {
            ActivityCompat.requestPermissions(this@HomeActivity, permissions, REQUEST_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            navigate(R.id.imagePickerFragment)
        } else {
            AlertDialog.Builder(this)
                .setTitle(R.string.alert)
                .setMessage(R.string.alert_message)
                .setNegativeButton(R.string.alert_no) { _, _ -> }
                .setPositiveButton(R.string.alert_yes) { _, _ ->
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName")).apply {
                        addCategory(Intent.CATEGORY_DEFAULT)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }.also { intent ->
                        startActivity(intent)
                    }
                }.show()
        }
    }

    private fun initView() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.home_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        with(binding.bottomNavHome) {
            setupWithNavController(navController)
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.imagePickerFragment -> {
                        checkPermission()
                    }
                    R.id.homeFragment -> {
                        navigate(R.id.homeFragment)
                        imageListViewModel.resetAllData()
                    }
                    R.id.recipeFragment -> {
                        navigate(R.id.recipeFragment)
                        imageListViewModel.resetAllData()
                    }
                }
                true
            }
            itemIconTintList = null
        }

    }
}
