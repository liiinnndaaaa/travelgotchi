package com.lindotschka.travelgotchi.adapter

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.lindotschka.travelgotchi.R
import com.lindotschka.travelgotchi.model.CityData

class ExpandableListAdapter(
    private val context: Context,
    private val dataList: Map<String, List<String>>
) : BaseExpandableListAdapter() {

    private val titleList = dataList.keys.toList()
    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return dataList[titleList[listPosition]]!![expandedListPosition]
    }
    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }
    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        Log.d("ExpandableListAdapter", "getChildView aufgerufen für Gruppe: ${titleList[listPosition]}, Kind: $expandedListPosition")
        val childItem = getChild(listPosition, expandedListPosition)
        val layoutRes = if (childItem is CityData) R.layout.item_city else R.layout.list_info
        val view = convertView ?: LayoutInflater.from(context).inflate(layoutRes, parent, false)

        if (childItem is CityData) {
            val cityNameTextView = view.findViewById<TextView>(R.id.cityName)
            val cityImageView = view.findViewById<ImageView>(R.id.cityImage)
            cityNameTextView.text = childItem.name
            Glide.with(context).load(childItem.imageUrl).into(cityImageView)
        } else if (childItem is String) {
            val infoTextView = view.findViewById<TextView>(R.id.infoText)
            infoTextView.text = childItem
        }
        return view
    }
    override fun getChildrenCount(groupPosition: Int): Int {
        val count = dataList[titleList[groupPosition]]?.size ?: 0
        Log.d("ExpandableListAdapter", "getChildrenCount für ${titleList[groupPosition]}: $count")
        return count
    }
    override fun getGroup(listPosition: Int): Any {
        Log.d("ExpandableListAdapter", "Adapter titleList: ${titleList.joinToString()}")
        return titleList[listPosition]
    }
    override fun getGroupCount(): Int {
        Log.d("ExpandableListAdapter", "Anzahl Gruppen: ${titleList.size}")
        return titleList.size
    }
    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }
    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        Log.d("ExpandableListAdapter", "getGroupView aufgerufen für: ${titleList[listPosition]}")
        val listTitle = getGroup(listPosition) as String
        Log.d("ExpandableListAdapter", "GroupView erstellt: $listTitle")

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_group, parent, false)
        val listTitleTextView = view.findViewById<TextView>(R.id.listGroup)
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle
        return view
    }
    override fun hasStableIds(): Boolean {
        return false
    }
    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}