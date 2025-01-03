package com.example.videocallapp


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.videocallapp.databinding.ActivityMainBinding
import com.permissionx.guolindev.PermissionX
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import java.util.Collections


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestSystemAlertWindowPermission()
        initView()
    }

    private fun initView(){

       binding.tvCurrentUserName.text = "Hello " + intent.getStringExtra("userName")

        binding.edtTargetUserName.addTextChangedListener {
            val targetUsername = binding.edtTargetUserName.text.toString()
            setUpVoiceCall(targetUsername)
            setUpVideoCall(targetUsername)
        }

       // setupCallButtons()

    }

    private fun setupCallButtons() {
        binding.edtTargetUserName.addTextChangedListener {
            val targetUserName = it.toString().trim()
            if (targetUserName.isNotEmpty()) {
                setUpVoiceCall(targetUserName)
                setUpVideoCall(targetUserName)
            }
        }
    }

    private fun setUpVoiceCall(userName: String){
        binding.btnVoiceCall.setIsVideoCall(false)
        binding.btnVoiceCall.setResourceID("zego_uikit_call")
        binding.btnVoiceCall.setInvitees(Collections.singletonList(ZegoUIKitUser(userName, userName)))
        //binding.btnVoiceCall.setInvitees(listOf(ZegoUIKitUser(userName, userName)))

    }

    private fun setUpVideoCall(userName: String){
        binding.btnVideoCall.setIsVideoCall(true)
        binding.btnVideoCall.setResourceID("zego_uikit_call")
        binding.btnVideoCall.setInvitees(Collections.singletonList(ZegoUIKitUser(userName, userName)))
       // binding.btnVideoCall.setInvitees(listOf(ZegoUIKitUser(userName, userName)))

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