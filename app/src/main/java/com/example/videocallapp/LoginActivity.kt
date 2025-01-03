package com.example.videocallapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.videocallapp.databinding.ActivityLoginBinding
import com.permissionx.guolindev.PermissionX
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestSystemAlertWindowPermission()

        initView()
    }

    private fun initView(){

        binding.btnLogin.setOnClickListener {
            val userName = binding.edtUserName.text.toString().trim()

            if (userName.isEmpty()) {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig()

            ZegoUIKitPrebuiltCallService.init(
                application,
                Constant.appID,
                Constant.appSign,
                userName,
                userName,
                callInvitationConfig
            )

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("userName", userName)
            startActivity(intent)

        }
    }

    private fun requestSystemAlertWindowPermission() {
        PermissionX.init(this)
            .permissions(android.Manifest.permission.SYSTEM_ALERT_WINDOW)
            .onExplainRequestReason { scope, deniedList ->
                val message =
                    "We need your consent for the following permissions to use the offline call function properly."
                scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny")
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    Toast.makeText(this, "All permissions granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this,
                        "The following permissions are denied: $deniedList",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}