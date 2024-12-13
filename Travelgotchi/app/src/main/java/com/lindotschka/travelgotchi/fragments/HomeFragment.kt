package com.lindotschka.travelgotchi.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.lindotschka.travelgotchi.HomeViewModel
import com.lindotschka.travelgotchi.R
import com.lindotschka.travelgotchi.adapter.CountriesAdapter
import com.lindotschka.travelgotchi.databinding.FragmentHomeBinding
import com.lindotschka.travelgotchi.util.generateCountries

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var europeAdapter: CountriesAdapter
    private lateinit var asiaAdapter: CountriesAdapter
    private lateinit var northamericaAdapter: CountriesAdapter
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        /** initialize variables */

        val europeView = view.findViewById<RecyclerView>(R.id.europeView)
        val asiaView = view.findViewById<RecyclerView>(R.id.asiaView)
        val northamericaView = view.findViewById<RecyclerView>(R.id.northamericaView)

        europeAdapter = CountriesAdapter(ArrayList())
        asiaAdapter = CountriesAdapter(ArrayList())
        northamericaAdapter = CountriesAdapter(ArrayList())

        /** get Data firebase */
        val database = FirebaseDatabase.getInstance().getReference("countries")

        generateCountries(europeView,requireContext(),europeAdapter,viewModel,"europe",database)
        generateCountries(asiaView,requireContext(),asiaAdapter,viewModel,"asia",database)
        generateCountries(northamericaView,requireContext(),northamericaAdapter,viewModel,"northamerica",database)

        viewModel.europeCountries.observe(viewLifecycleOwner) { countryList ->
            europeAdapter.updateData(countryList)
        }

        viewModel.asiaCountries.observe(viewLifecycleOwner) { countryList ->
            asiaAdapter.updateData(countryList)
        }

        viewModel.northamericaCountries.observe(viewLifecycleOwner) { countryList ->
            northamericaAdapter.updateData(countryList)
        }

    }
}
