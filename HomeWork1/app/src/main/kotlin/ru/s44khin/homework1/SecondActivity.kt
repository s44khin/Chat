package ru.s44khin.homework1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.s44khin.homework1.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context) = Intent(context, SecondActivity::class.java)
    }

    private val binding: ActivitySecondBinding by lazy {
        ActivitySecondBinding.inflate(layoutInflater)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val latitude = p1?.getDoubleExtra("LATITUDE", 0.0)
            val longitude = p1?.getDoubleExtra("LONGITUDE", 0.0)

            setResult(
                RESULT_OK,
                Intent()
                    .putExtra("LATITUDE", latitude)
                    .putExtra("LONGITUDE", longitude)
            )

            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonStartService.setOnClickListener {
            val filter = IntentFilter()
            filter.addAction("EXTRA_GEOLOCATION")
            LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter)
            startService(Service.createIntent(this))
        }
    }
}