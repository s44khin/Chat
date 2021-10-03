package ru.s44khin.homework1

import android.Manifest
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import ru.s44khin.homework1.databinding.ActivityFirstBinding

class FirstActivity : AppCompatActivity() {

    private val binding: ActivityFirstBinding by lazy {
        ActivityFirstBinding.inflate(layoutInflater)
    }

    private val permissions = registerForActivityResult(RequestPermission()) { granted ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                granted -> {
                    binding.buttonGetGeolocation.setOnClickListener {
                        location.launch(SecondActivity.createIntent(this))
                    }
                }
                else -> {
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private val location = registerForActivityResult(StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.longitude.text = it.data!!.getDoubleExtra("LONGITUDE", 0.0).toString()
            binding.latitude.text = it.data!!.getDoubleExtra("LATITUDE", 0.0).toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        permissions.launch(Manifest.permission.ACCESS_COARSE_LOCATION)

        binding.buttonReset.setOnClickListener {
            binding.latitude.text = "0"
            binding.longitude.text = "0"
        }
    }
}