package com.example.crud_route

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.crud_route.more.ExplorePage
import com.example.crud_route.more.Inclination
import com.example.crud_route.profile.Profile
import com.example.crud_route.route.Adapter
import com.example.crud_route.route.CreateRoute
import com.example.crud_route.route.Route
import com.example.crud_route.route.RouteList
import com.example.crud_route.route.daoRoute

class HomePage : AppCompatActivity() {
    private val REQUEST_CODE_PERMISSIONS = 1001
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private lateinit var dao: daoRoute
    private lateinit var adapter: Adapter
    private lateinit var list: ArrayList<Route>

    private lateinit var btnLoadRoutes: Button
    private lateinit var btnMore: Button
    private lateinit var btnCreateRoute: Button
    private lateinit var btnProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        if (!allPermissionsGranted()) {
            // Solicitar permisos si no están otorgados
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        dao = daoRoute(this)
        list = ArrayList<Route>()
        adapter = Adapter(this, list, dao)

        val listFragment = RouteList()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, listFragment)
            commit()
        }


        btnLoadRoutes = findViewById(R.id.btnLoadRoutes)
        btnMore = findViewById(R.id.btnMore)
        btnCreateRoute = findViewById(R.id.btnCreateRoute)
        btnProfile = findViewById(R.id.btnProfile)

        btnLoadRoutes.setOnClickListener({
            loadList()
        })
        btnMore.setOnClickListener({
            startActivity(Intent(this, Inclination::class.java))
        })
        btnCreateRoute.setOnClickListener({
            startActivity(Intent(this, CreateRoute::class.java))
        })
        btnProfile.setOnClickListener({
            startActivity(Intent(this, Profile::class.java))
        })
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                // Manejar el caso en que el usuario niegue los permisos
                // Puedes mostrar un mensaje explicativo o solicitar los permisos nuevamente
            }
        }
    }

    private fun loadList() {
        recreate()
    }

}
