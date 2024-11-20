package com.lindotschka.travelgotchi.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lindotschka.travelgotchi.R
import com.lindotschka.travelgotchi.adapter.CountriesAdapter
import com.lindotschka.travelgotchi.firebase.getCountriesData
import com.lindotschka.travelgotchi.model.CountryData

class HomeFragment : Fragment() {

    private lateinit var mDataBase: DatabaseReference
    private lateinit var countryList: ArrayList<CountryData>
    private lateinit var mAdapter: CountriesAdapter
    private lateinit var europeView: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var recyclerView3: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countryList = ArrayList()
        mAdapter = CountriesAdapter(this, countryList)

        europeView = view.findViewById(R.id.europeView)
        europeView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL, // Horizontal ausgerichtet
            false
        )
        europeView.setHasFixedSize(true)
        europeView.adapter = mAdapter

        /**get Data firebase*/
        mDataBase = FirebaseDatabase.getInstance().getReference("countries")
        getCountriesData(mDataBase,countryList,mAdapter,requireContext(),"europe")

        // RecyclerView 2 initialisieren
        recyclerView2 = view.findViewById(R.id.recyclerView2)
        recyclerView2.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView2.adapter = createDummyAdapter()

        // RecyclerView 3 initialisieren
        recyclerView3 = view.findViewById(R.id.recyclerView3)
        recyclerView3.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView3.adapter = createDummyAdapter()
    }

    private fun createDummyAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        return object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val textView = LayoutInflater.from(parent.context)
                    .inflate(android.R.layout.simple_list_item_1, parent, false)
                return object : RecyclerView.ViewHolder(textView) {}
            }

            override fun getItemCount(): Int = 5 // Anzahl der Dummy-Items

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                // Dummy-Text festlegen
                (holder.itemView as TextView).text = "Item $position"
            }
        }
    }
}
