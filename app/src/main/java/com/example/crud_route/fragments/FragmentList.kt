package com.example.crud_route.fragments

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.ParcelableCompat
import com.example.crud_route.HomePage
import com.example.crud_route.route.Adapter
import com.example.crud_route.R
import com.example.crud_route.route.Route
import com.example.crud_route.route.daoRoute

class FragmentList : Fragment() {
    lateinit var dao: daoRoute
    lateinit var adapter: Adapter
    lateinit var list: ArrayList<Route>

    lateinit var homePage: HomePage
    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_list, container, false)

        dao = daoRoute(requireContext())
        list = dao.viewAll() ?: ArrayList()

        adapter =
            Adapter(requireActivity(), list, dao)
        val listView: ListView = rootView.findViewById(R.id.routeListView)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            changeFragment(position)
            homePage?.setItemRoute(adapter.latitudeB, adapter.longitudeB)
        }

        return rootView
    }

    override fun onResume() {
        super.onResume()
        if (list.isEmpty()) {
            val emptyMessageTextView: TextView = rootView.findViewById(R.id.emptyListMsg)
            emptyMessageTextView.text = "No hay rutas disponibles"
        }
        dao.viewAll()
    }

    fun changeFragment(position: Int) {
        try {
            val selectedRoute = list[position]
            val fragmentDetail = FragmentDetail()

            // Pass necessary information to the Fragment
            val bundle = Bundle()
            bundle.putParcelable("route", selectedRoute)
            fragmentDetail.arguments = bundle
            // Replace FragmentList to FragmentDetail
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.flFragment, fragmentDetail)
            transaction.addToBackStack(null)
            transaction.commit()

            Toast.makeText(context, "Item " + position, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Error: " + e.toString(), Toast.LENGTH_LONG).show()
        }
    }

}

