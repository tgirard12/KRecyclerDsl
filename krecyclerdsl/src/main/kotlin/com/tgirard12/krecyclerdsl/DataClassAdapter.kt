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
 * recyclerView.adapter = dataClassAdapter<DataClass, ItemView>(items, R.layout.item_view) {
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
 * ```
 */
inline fun <reified T, reified V : View> dataClassAdapter(
        items: List<T>, @LayoutRes resId: Int,
        f: DataClassAdapter<T, V>.() -> Unit): DataClassAdapter<T, V> {
    return DataClassAdapter<T, V>(items, resId).apply { f() }
}

/**
 * RecyclerView.Adapter for data class, use fun [dataClassAdapter] to create it
 */
class DataClassAdapter<out T, out V : View>(private val items: List<T>, @LayoutRes val resId: Int)
    : RecyclerView.Adapter<DataClassAdapter.DataClassViewHolder>() {

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
        val item = items[position]
        holder.item = item
        _onBindViewHolder(holder.itemView as V, item)
    }

    override fun getItemCount(): Int = items.count()

    class DataClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item: Any? = null
    }
}
