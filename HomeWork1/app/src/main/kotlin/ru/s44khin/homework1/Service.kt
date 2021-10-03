package ru.s44khin.homework1

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class Service : Service() {

    companion object {
        fun createIntent(context: Context) = Intent(context, Service::class.java)
    }

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onBind(intent: Intent): IBinder? = null

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, getString(R.string.hello_service), Toast.LENGTH_SHORT).show()

        fusedLocationClient.lastLocation.addOnCompleteListener { taskLocation ->
            if (taskLocation.isSuccessful && taskLocation.result != null) {
                val location = taskLocation.result

                val broadcastManagerIntent = Intent("EXTRA_GEOLOCATION")
                    .putExtra("LATITUDE", location.latitude)
                    .putExtra("LONGITUDE", location.longitude)

                LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastManagerIntent)
            } else {
                Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}
