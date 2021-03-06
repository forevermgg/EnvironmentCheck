package com.mgg.environmentcheck

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.fbs.app.main.viewmodel.Click
import com.mgg.checkenv.CheckEnv
import com.mgg.checkenv.callback.EmulatorCheckCallback
import com.mgg.checkenv.emulator.CheckEmulator
import com.mgg.checkenv.magisk.MagiskDetector
import com.mgg.checkenv.rom.RomUtils
import com.mgg.checkenv.rom.isGoogleRom
import com.mgg.checkenv.sign.CheckSign
import com.mgg.checkenv.utils.Base64
import com.mgg.checkenv.utils.ShellUtils
import com.mgg.checkenv.uuid.CheckDevicesUUID
import com.mgg.checkenv.vpn.isUsingVpn
import com.mgg.environmentcheck.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.util.*

class MainActivity : ComponentActivity() {
    private var isDestory: Boolean = false
    private lateinit var binding: ActivityMainBinding
    private var mainViewModel: MainViewModel? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        isDestory = false
        val isEmulator = CheckEmulator.getSingleInstance().readSysProperty(this, null)
        CheckEnv.getSingleInstance().checkIsRunningInEmulator(object : EmulatorCheckCallback {
            override fun findEmulator(emulatorInfo: String?) {
                emulatorInfo?.let {
                    binding.checkEmulator.text =
                        "CheckEmulator: $it isEmulator:$isEmulator$emulatorInfo"
                }
            }
        })
        binding.checkCpu.text = "CheckCpu:" + CheckEnv.getCpuInfo().toString()
        binding.checkDebug.text = "CheckDebug:" + CheckEnv.getDebugInfo().toString()
        binding.checkMultiOpen.text = "CheckMultiOpen:" + CheckEnv.getMoreOpenInfo().toString()
        binding.checkRoot.text = "CheckRoot:" + CheckEnv.isRoot()
        binding.checkVpn.text = "CheckVpn:" + isUsingVpn()
        binding.checkNetwork.text = "CheckNetwork:" + CheckEnv.getNetWorkInfo()
        binding.checkRom.text = "CheckRom:" + RomUtils.getRomInfo().toString() + File.separator + isGoogleRom
        binding.sampleText.text = binding.sampleText.text.toString() +
                ShellUtils.execCmd("getprop", false).toString()
        binding.checkSign.text =
            "CheckSign: \n" + (CheckSign().getSignatureStr(this) == CheckSign().getSignature(
                this
            ))
        CheckSign().checkSignature(this, "FF:BC:87:41:5F:1F:77:37:85:74:77:3B:03:80:D1:6B")
        Timber.tag("CheckDevicesUUID").e(CheckDevicesUUID.toStringChecked())
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                while (isDestory) {
                    stringFromJNI()
                    delay(1000)
                    val temp = hook(1, 2)
                    Timber.tag("hook result").e(temp.toString())
                    Timber.tag("Minhal.string").e(hook("Fuck U!!!!!!!!!"))
                    withContext(Dispatchers.Main) {
                        binding.checkDebug.text = "CheckDebug:" + CheckEnv.getDebugInfo().toString()
                        binding.checkHook.text = "CheckHook:" + CheckEnv.getHookInfo().toString()
                    }
                }
            }
        }
        Math.random()
        Timber.tag("MagiskDetector").e(MagiskDetector.haveMagicMount().toString())
        testToast()

        val orig = "mgg"
        var encoded: String
        var decoded: String
        encoded = Base64.encode(orig)
        decoded = Base64.decode(encoded)
        Timber.tag("Base64").e("decoded=${decoded} orig=${orig}")

        encoded = Base64.encodeWithPadding(orig)
        decoded = Base64.decode(encoded)
        Timber.tag("Base64").e("decoded=${decoded} orig=${orig}")

        encoded = Base64.encodeUrlSafe(orig)
        decoded = Base64.decode(encoded)
        Timber.tag("Base64").e("decoded=${decoded} orig=${orig}")

        encoded = Base64.encodeUrlSafeWithPadding(orig)
        decoded = Base64.decode(encoded)
        Timber.tag("Base64").e("decoded=${decoded} orig=${orig}")
        binding.checkSign.setOnClickListener {
            mainViewModel?.handle(Click.TEST, "")
        }

        /*val input = ("Hello snappy-java! Snappy-java is a JNI-based wrapper of "
                + "Snappy, a fast compresser/decompresser.")
        val compressed = Snappy.compress(input.toByteArray(charset(Charsets.UTF_8.toString())))
        val uncompressed = Snappy.uncompress(compressed)

        val result = String(uncompressed, charset(Charsets.UTF_8.toString()))
        println(result)*/
    }

    private fun hook(a: Int, b: Int): Int {
        Timber.tag("hook").e((a + b).toString())
        return a + b
    }

    fun hook(x: String): String {
        return x.lowercase(Locale.getDefault())
    }

    external fun stringFromJNI(): String

    external fun testToast()

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    override fun onDestroy() {
        isDestory = false
        super.onDestroy()
        Timber.e("onDestroy()")
    }
}
