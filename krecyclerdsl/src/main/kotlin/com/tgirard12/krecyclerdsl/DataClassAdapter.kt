package com.tgirard12.krecyclerdsl

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Create a @DataClassAdapter
 *
 *  ```
 * recyclerView.adapter = dataClassAdapter<ItemView, DataClass>(R.layout.item_view, items) {
 *     onBindViewHolder { view, appSites ->
 *         ...
 *     }
 *     onItemClickListener { view, appSites ->
 *         ...
 *     }
 * }
 * ```
 *
 * To avoid use View.findViewById() in all `onBindViewHolder` call, it is better to use a custom view class ItemView in `R.layout.item_view` :
 * ```
 *  <com.tgirard12.krecyclerdsl.ItemView >
 *      ...
 *  </com.tgirard12.krecyclerdsl.ItemView >
 * ```h
 */
inline fun <reified V : View, reified T> dataClassAdapter(
        @LayoutRes resId: Int, items: List<T>, f: DataClassAdapter<V, T>.() -> Unit): DataClassAdapter<V, T> {
    return DataClassAdapter<V, T>(resId, items).apply { f() }
}

/**
 * RecyclerView.Adapter for data class, use fun [dataClassAdapter] to create it
 */
class DataClassAdapter<out V : View, T>(@LayoutRes private val resId: Int, items: List<T>)
    : RecyclerView.Adapter<DataClassAdapter.DataClassViewHolder>() {

    private val mutableItems = items.toMutableList()

    private var _onBindViewHolder: (view: V, item: T) -> Unit = { _, _ -> }
    private var _onItemClickListener: (view: V, item: T) -> Unit = { _, _ -> }

    // DSL
    fun onBindViewHolder(f: (V, T) -> Unit) {
        _onBindViewHolder = f
    }

    fun onItemClickListener(f: (V, T) -> Unit) {
        _onItemClickListener = f
    }

    // Adapter
    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataClassViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(resId, parent, false)
        val viewHolder = DataClassViewHolder(itemView)
        itemView.setOnClickListener { _onItemClickListener(itemView as V, viewHolder.item as T) }
        return viewHolder
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: DataClassViewHolder, position: Int) {
        val item = mutableItems[position]
        holder.item = item
        _onBindViewHolder(holder.itemView as V, item)
    }

    override fun getItemCount(): Int = mutableItems.count()

    class DataClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item: Any? = null
    }
}
