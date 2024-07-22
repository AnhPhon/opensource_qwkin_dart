package com.example.torch_controller.implementation

import android.content.ContentValues.TAG
import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Build
import android.util.Log
import com.example.torch_controller.classes.BaseTorch
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.M)
class Torch(context: Context) : BaseTorch() {
    private var flashLightStatus: Boolean = false

    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    private var cameraId: String? = null

    override fun toggle(): Boolean {
        /// Check android version.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            /// Try get camera id.
            try {
                cameraId = cameraManager.cameraIdList[0]
            } catch (e: Exception) {
                Log.d(TAG, "Could not fetch camera id, the plugin won't work.")
                return false
            }


            /// If cameraId != null then turn on/off flash.
            if (cameraId != null) {
                if (!flashLightStatus) {
                    try {
                        cameraManager.setTorchMode(cameraId!!, true)
                        flashLightStatus = true
                        return this.flashLightStatus
                    } catch (e: Exception) {
                        Log.d(TAG, e.printStackTrace().toString())
                        return false
                    }
                } else {
                    try {
                        cameraManager.setTorchMode(cameraId!!, false)
                        flashLightStatus = false
                        return this.flashLightStatus
                    } catch (e: Exception) {
                        Log.d(TAG, e.printStackTrace().toString())
                        return false
                    }
                }
            } else {
                Log.d(TAG, "Flash cannot be accessed.")
                return false
            }


        } else {
            Log.d(TAG, "The device does not support this feature.")
            return false
        }

    }

    override fun isTorchActive(): Boolean {
        return flashLightStatus
    }

    override fun dispose() {}
}