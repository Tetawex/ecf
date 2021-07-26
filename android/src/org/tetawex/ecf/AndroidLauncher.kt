package org.tetawex.ecf

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import org.tetawex.ecf.core.ActionResolver
import org.tetawex.ecf.core.ECFGame

class AndroidLauncher : AndroidApplication(), ActionResolver {
    private var backPressedListener: (() -> Boolean)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        config.useImmersiveMode = true
        initialize(ECFGame(this), config)
    }

    override fun onBackPressed() {
        Gdx.app.postRunnable {
            if (backPressedListener != null) {
                val didHandle = backPressedListener?.invoke() ?: false
                if (!didHandle) {
                    this.runOnUiThread { super.onBackPressed() }
                }
            }
        }
    }

    override fun setBackPressedListener(listener: () -> Boolean) {
        backPressedListener = listener
    }

    override fun externalStorageAccessible(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                return false
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                return false
            }
        }
        return true
    }

    override fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No browser installed", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    companion object {
        private const val REQUEST_CHOOSER = 1
    }
}