package com.lindotschka.travelgotchi.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lindotschka.travelgotchi.HomeViewModel
import com.lindotschka.travelgotchi.R
import com.lindotschka.travelgotchi.activities.CountryActivity
import com.lindotschka.travelgotchi.adapter.CountriesAdapter
import com.lindotschka.travelgotchi.databinding.FragmentHomeBinding
import com.lindotschka.travelgotchi.firebase.generateCountries
import com.lindotschka.travelgotchi.firebase.getCountriesData
import com.lindotschka.travelgotchi.model.CountryData

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var europeAdapter: CountriesAdapter
    private lateinit var asiaAdapter: CountriesAdapter
    private lateinit var binding: FragmentHomeBinding

    private lateinit var recyclerView3: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false)

        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        /** initialize variables */

        val europeView = view.findViewById<RecyclerView>(R.id.europeView)
        val asiaView = view.findViewById<RecyclerView>(R.id.asiaView)

        europeAdapter = CountriesAdapter(this, ArrayList())
        asiaAdapter = CountriesAdapter(this, ArrayList())

        /** get Data firebase */
        val database = FirebaseDatabase.getInstance().getReference("countries")

        generateCountries(europeView,requireContext(),europeAdapter,viewModel,"europe",database)
        generateCountries(asiaView,requireContext(),asiaAdapter,viewModel,"asia",database)

        viewModel.europeCountries.observe(viewLifecycleOwner) { countryList ->
            europeAdapter.updateData(countryList)
        }

        viewModel.asiaCountries.observe(viewLifecycleOwner) { countryList ->
            asiaAdapter.updateData(countryList)
        }


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
