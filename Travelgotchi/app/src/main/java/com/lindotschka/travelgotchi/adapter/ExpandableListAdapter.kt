package com.lindotschka.travelgotchi.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.lindotschka.travelgotchi.R
import com.lindotschka.travelgotchi.model.CityData
import com.lindotschka.travelgotchi.model.SightsData

class ExpandableListAdapter(
    private val context: Context,
    private var dataList: Map<String, List<Any>>
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
        val childItem = getChild(listPosition, expandedListPosition)
        val layoutRes = if (childItem is SightsData) R.layout.item_pic else R.layout.list_info
        val view = convertView ?: LayoutInflater.from(context).inflate(layoutRes, parent, false)

        if (childItem is String) {
            val infoTextView = view.findViewById<TextView>(R.id.infoText)
            infoTextView.text = childItem
        }
        return view
    }
    override fun getChildrenCount(groupPosition: Int): Int {
        return dataList[titleList[groupPosition]]?.size ?: 0
    }
    override fun getGroup(listPosition: Int): Any {
        return titleList[listPosition]
    }
    override fun getGroupCount(): Int {
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
        val listTitle = getGroup(listPosition) as String

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