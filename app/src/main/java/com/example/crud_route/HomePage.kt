package com.example.crud_route

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.crud_route.more.ExplorePage
import com.example.crud_route.profile.Profile
import com.example.crud_route.route.Adapter
import com.example.crud_route.route.CreateRoute
import com.example.crud_route.route.Route
import com.example.crud_route.route.RouteList
import com.example.crud_route.route.daoRoute

class HomePage : AppCompatActivity() {
    private lateinit var dao: daoRoute
    private lateinit var adapter: Adapter
    private lateinit var list: ArrayList<Route>

    private lateinit var btnMore: Button
    private lateinit var btnCreateRoute: Button
    private lateinit var btnProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        dao = daoRoute(this)
        list = ArrayList<Route>()
        adapter = Adapter(this, list, dao)

        val listFragment = RouteList()
        viewAllItems()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, listFragment)
            commit()
        }

        btnMore = findViewById(R.id.btnMore)
        btnCreateRoute = findViewById(R.id.btnCreateRoute)
        btnProfile = findViewById(R.id.btnProfile)

        btnMore.setOnClickListener({
            startActivity(Intent(this, ExplorePage::class.java))
        })
        btnCreateRoute.setOnClickListener({
            startActivity(Intent(this, CreateRoute::class.java))
        })
        btnProfile.setOnClickListener({
            startActivity(Intent(this, Profile::class.java))
        })
    }

    fun viewAllItems() {
        adapter.notifyDataSetChanged()
        list = dao.viewAll()
    }

}
