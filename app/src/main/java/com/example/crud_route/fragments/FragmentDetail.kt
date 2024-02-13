package com.example.crud_route.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.crud_route.route.FileAdapter
import com.example.crud_route.R

class FragmentDetail : Fragment() {
    private lateinit var fileAdapter: FileAdapter
    private lateinit var fileUris: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val recyclerView: RecyclerView
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }
}