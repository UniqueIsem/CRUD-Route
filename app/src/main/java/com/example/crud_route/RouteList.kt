package com.example.crud_route

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import com.example.crud_route.route.Adapter
import com.example.crud_route.route.Route
import com.example.crud_route.route.daoRoute

class RouteList : Fragment() {
    lateinit var dao: daoRoute
    lateinit var adapter: Adapter
    lateinit var list: ArrayList<Route>

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


}

