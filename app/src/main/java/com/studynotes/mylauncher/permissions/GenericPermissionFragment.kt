package com.studynotes.mylauncher.permissions


import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.studynotes.mylauncher.R
import com.studynotes.mylauncher.viewUtils.ViewUtils.setRoundedRectangleBackgroundDrawable

class GenericPermissionFragment : DialogFragment() {

    private var callback: GenericPermissionListener? = null
    private var messageBoxText: String? = null
    private var positiveButtonText: String? = null
    private var image: Int? = null
    private var permissionType: GenericPermissionType? = null
    private var permissionLauncher: ActivityResultLauncher<String>? = null
    private var shouldSendCallbackOnResume: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                callback?.onPermissionGranted()
                dismissAllowingStateLoss()
            } else {
                callback?.onPermissionDenied()
                dismissAllowingStateLoss()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.generic_permission_dialog, container, false)
        dialog?.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpDialog()
        setRoundedRectangleBackgroundDrawable(view, context?.resources?.getColor(R.color.white) ?: Color.BLACK, 40f)
        view.findViewById<View>(R.id.positive_button).setOnClickListener {
            when (permissionType) {

                GenericPermissionType.OVERLAY -> {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE)) {
                        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                        intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME)
                        context?.startActivity(intent)
                        shouldSendCallbackOnResume = true
                    } else {
                        shouldSendCallbackOnResume = true
                        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                        intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME)
                        context?.startActivity(intent)
                    }
                }

                GenericPermissionType.USAGE_STATE -> {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE)) {
                        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                        intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME)
                        context?.startActivity(intent)
                        shouldSendCallbackOnResume = true
                    } else {
                        shouldSendCallbackOnResume = true
                        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                        intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME)
                        context?.startActivity(intent)
                    }
                }

                else -> {
                    Toast.makeText(
                        context,
                        "Requested Permission Unspecified, Please try again!!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        view.findViewById<View>(R.id.negative_button)?.setOnClickListener {
            callback?.onPermissionDenied()
            dismissAllowingStateLoss()
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        val fragmentTransaction = manager.beginTransaction()
        fragmentTransaction.add(this, tag)
        fragmentTransaction.commitAllowingStateLoss()

    }

    private fun setUpDialog() {
        view?.findViewById<TextView>(R.id.title)?.text = messageBoxText
        view?.findViewById<Button>(R.id.positive_button)?.text = positiveButtonText
        view?.findViewById<ImageView>(R.id.image)?.setImageDrawable(
            context?.let { image?.let { it1 -> ContextCompat.getDrawable(it, it1) } }
        )
    }

    companion object {
        private val TAG = "GenericPermission"

        enum class GenericPermissionType {
            OVERLAY, USAGE_STATE
        }

        fun showFragment(
            fragmentManager: FragmentManager,
            listener: GenericPermissionListener,
            messageBoxText: String? = null,
            image: Int? = null,
            positiveButtonText: String? = null,
            permissionType: GenericPermissionType
        ) {
            val fragment = fragmentManager.findFragmentByTag(TAG) as? GenericPermissionFragment
            if (fragment != null) fragment.dismissAllowingStateLoss()
            GenericPermissionFragment().apply {
                this.callback = listener
                this.messageBoxText = messageBoxText
                this.positiveButtonText = positiveButtonText
                this.permissionType = permissionType
                this.image = image
            }.show(fragmentManager, TAG)
        }
    }

    override fun onResume() {
        super.onResume()
        if (shouldSendCallbackOnResume) {
            callback?.onResume()
            shouldSendCallbackOnResume = false
            dismissAllowingStateLoss()
        }
    }

}