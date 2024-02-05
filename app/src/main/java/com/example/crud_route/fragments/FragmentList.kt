package com.example.crud_route.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import com.example.crud_route.Adapter
import com.example.crud_route.R
import com.example.crud_route.Route
import com.example.crud_route.daoRoute

class FragmentList : Fragment() {
    lateinit var dao: daoRoute
    lateinit var adapter: Adapter
    lateinit var list: ArrayList<Route>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_list, container, false)

        dao = daoRoute(requireContext())
        list = dao.viewAll() ?: ArrayList()

        adapter = Adapter(requireActivity(), list, dao)
        val listView: ListView = rootView.findViewById(R.id.routeListView)
        listView.adapter = adapter

        if (list.isEmpty()) {
            val emptyMessageTextView: TextView = rootView.findViewById(R.id.emptyListMsg)
            emptyMessageTextView.text = "No hay rutas disponibles"
        }

        listView.setOnItemClickListener { _, _, i, _ ->
            // Lógica al hacer clic en un elemento de la lista
        }

        return rootView
    }


}

