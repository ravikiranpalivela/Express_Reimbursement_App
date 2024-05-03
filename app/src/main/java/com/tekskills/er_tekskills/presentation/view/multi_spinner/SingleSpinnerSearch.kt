package com.tekskills.er_tekskills.presentation.view.multi_spinner

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import com.tekskills.er_tekskills.R
import java.util.Locale


class SingleSpinnerSearch : AppCompatSpinner, DialogInterface.OnCancelListener {
    var adapter: MyAdapter? = null
    private var items: List<KeyPairBoolData>? = null
    private var defaultText: String? = ""
    private var spinnerTitle: String? = ""
    private var emptyTitle = "Not Found!"
    private var searchHint = "Type to search"
    private var listener: SingleSpinnerListener? = null
    var isColorseparation = false
    var isSearchEnabled = true

    constructor(context: Context?) : super(context!!) {}
    constructor(arg0: Context, arg1: AttributeSet?) : super(arg0, arg1) {
        val a = arg0.obtainStyledAttributes(arg1, R.styleable.SingleSpinnerSearch)
        val N = a.indexCount
        for (i in 0 until N) {
            val attr = a.getIndex(i)
            if (attr == R.styleable.MultiSpinnerSearch_hintText) {
                spinnerTitle = a.getString(attr)
                defaultText = spinnerTitle
                break
            }
        }
        Log.i(TAG, "spinnerTitle: $spinnerTitle")
        a.recycle()
    }

    constructor(arg0: Context?, arg1: AttributeSet?, arg2: Int) : super(
        arg0!!, arg1, arg2
    ) {
    }

    val selectedItems: List<KeyPairBoolData>
        get() {
            val selectedItems: MutableList<KeyPairBoolData> = ArrayList()
            for (item in items!!) {
                if (item.isSelected) {
                    selectedItems.add(item)
                }
            }
            return selectedItems
        }
    val selectedIds: List<Long>
        get() {
            val selectedItemsIds: MutableList<Long> = ArrayList()
            for (item in items!!) {
                if (item.isSelected) {
                    selectedItemsIds.add(item.id)
                }
            }
            return selectedItemsIds
        }

    override fun onCancel(dialog: DialogInterface) {
        // refresh text on spinner
        var spinnerText: String? = null
        var selectedItem: KeyPairBoolData? = null
        for (i in items!!.indices) {
            if (items!![i].isSelected) {
                selectedItem = items!![i]
                spinnerText = selectedItem.name
                break
            }
        }
        if (spinnerText == null) {
            spinnerText = defaultText
        }
        val adapterSpinner = ArrayAdapter<String?>(
            context,
            R.layout.item_spinner, R.id.tvCountry,
            arrayOf<String?>(spinnerText)
        )
        setAdapter(adapterSpinner)
        if (adapter != null) adapter!!.notifyDataSetChanged()
        listener!!.onItemsSelected(selectedItem)
    }

    override fun performClick(): Boolean {
        super.performClick()
        builder = AlertDialog.Builder(context)
        builder!!.setTitle(spinnerTitle)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.alert_dialog_listview_search, null)
        builder!!.setView(view)
        val listView = view.findViewById<ListView>(R.id.alertSearchListView)
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE
        listView.isFastScrollEnabled = false
        adapter = MyAdapter(context, items)
        listView.adapter = adapter
        for (i in items!!.indices) {
            if (items!![i].isSelected) {
                listView.setSelection(i)
                break
            }
        }
        val emptyText = view.findViewById<TextView>(R.id.empty)
        emptyText.text = emptyTitle
        listView.emptyView = emptyText
        val editText = view.findViewById<EditText>(R.id.alertSearchEditText)
        if (isSearchEnabled) {
            editText.visibility = VISIBLE
            editText.hint = searchHint
            editText.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    adapter!!.filter.filter(s.toString())
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {}
            })
        } else {
            editText.visibility = GONE
        }
        builder!!.setPositiveButton(
            "Clear"
        ) { dialog: DialogInterface, which: Int ->
            for (i in items!!.indices) {
                items!![i].isSelected = false
            }
            val adapterSpinner = ArrayAdapter<String?>(
                context,
                R.layout.item_spinner, R.id.tvCountry,
                arrayOf<String?>(defaultText)
            )
            setAdapter(adapterSpinner)
            if (adapter != null) adapter!!.notifyDataSetChanged()
            listener!!.onClear()
            dialog.dismiss()
        }

        //builder.setOnCancelListener(this);
        ad = builder!!.show()
        return true
    }

    fun setItems(items: List<KeyPairBoolData>, listener: SingleSpinnerListener?) {
        this.items = items
        this.listener = listener
        for (item in items) {
            if (item.isSelected) {
                defaultText = item.name
                break
            }
        }
        val adapterSpinner = ArrayAdapter<String?>(
            context,
            R.layout.item_spinner, R.id.tvCountry,
            arrayOf<String?>(defaultText)
        )
        setAdapter(adapterSpinner)
    }

    fun setEmptyTitle(emptyTitle: String) {
        this.emptyTitle = emptyTitle
    }

    fun setSearchHint(searchHint: String) {
        this.searchHint = searchHint
    }

    //Adapter Class
    inner class MyAdapter(context: Context?, var arrayList: List<KeyPairBoolData>?) :
        BaseAdapter(), Filterable {
        val inflater: LayoutInflater
        var mOriginalValues // Original Values
                : List<KeyPairBoolData>? = null

        init {
            inflater = LayoutInflater.from(context)
        }

        override fun getCount(): Int {
            return arrayList!!.size
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
            var convertView = convertView
            Log.i(TAG, "getView() enter")
            val holder: ViewHolder
            val data = arrayList!![position]
            if (convertView == null) {
                holder = ViewHolder()
                convertView = inflater.inflate(R.layout.item_listview_single, parent, false)
                holder.textView = convertView.findViewById<TextView>(R.id.alertTextView)
                convertView.tag = holder
            } else {
                holder = convertView.tag as ViewHolder
            }
            holder.textView!!.text = data.name
            var color = R.color.white
            if (isColorseparation) {
                val backgroundColor: Int =
                    if (position % 2 == 0) R.color.list_even else R.color.list_odd
                color = backgroundColor
                convertView.setBackgroundColor(ContextCompat.getColor(context, backgroundColor))
            }
            if (data.isSelected) {
                holder.textView!!.setTypeface(null, Typeface.BOLD)
                holder.textView!!.setTextColor(Color.WHITE)
                convertView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimary
                    )
                )
            } else {
                holder.textView!!.setTextColor(Color.DKGRAY)
                holder.textView!!.setTypeface(null, Typeface.NORMAL)
                convertView.setBackgroundColor(ContextCompat.getColor(context, color))
            }
            convertView.setOnClickListener { v: View? ->
                val selectedName = arrayList!![position].name
                for (i in items!!.indices) {
                    items!![i].isSelected = false
                    if (items!![i].name.equals(selectedName, ignoreCase = true)) {
                        items!![i].isSelected = true
                    }
                }
                ad!!.dismiss()
                onCancel(ad!!)
            }
            return convertView
        }

        @SuppressLint("DefaultLocale")
        override fun getFilter(): Filter {
            return object : Filter() {
                override fun publishResults(constraint: CharSequence, results: FilterResults) {
                    arrayList = results.values as List<KeyPairBoolData> // has the filtered values
                    notifyDataSetChanged() // notifies the data with new filtered values
                }

                override fun performFiltering(constraint: CharSequence): FilterResults {
                    var constraint: CharSequence? = constraint
                    val results =
                        FilterResults() // Holds the results of a filtering operation in values
                    val FilteredArrList: MutableList<KeyPairBoolData> = ArrayList()
                    if (mOriginalValues == null) {
                        mOriginalValues =
                            ArrayList(arrayList) // saves the original data in mOriginalValues
                    }

                    /*
					 *
					 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
					 *  else does the Filtering and returns FilteredArrList(Filtered)
					 *
					 ********/if (constraint == null || constraint.length == 0) {

                        // set the Original result to return
                        results.count = mOriginalValues!!.size
                        results.values = mOriginalValues
                    } else {
                        constraint = constraint.toString().lowercase(Locale.getDefault())
                        for (i in mOriginalValues!!.indices) {
                            Log.i(
                                TAG,
                                "Filter : " + mOriginalValues!![i].name + " -> " + mOriginalValues!![i].isSelected
                            )
                            val data = mOriginalValues!![i].name
                            if (data.lowercase(Locale.getDefault())
                                    .contains(constraint.toString())
                            ) {
                                FilteredArrList.add(mOriginalValues!![i])
                            }
                        }
                        // set the Filtered result to return
                        results.count = FilteredArrList.size
                        results.values = FilteredArrList
                    }
                    return results
                }
            }
        }

        private inner class ViewHolder {
            var textView: TextView? = null
        }
    }

    companion object {
        private val TAG = SingleSpinnerSearch::class.java.simpleName
        var builder: AlertDialog.Builder? = null
        var ad: AlertDialog? = null
    }
}