package com.tgirard12.krecyclerdsl.samples

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_dataclass_adapter.view.*


class ItemDataClassView : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    val image: ImageView by lazy { itemDataClass_image }
    val name: TextView by lazy { itemDataClass_name }
    val description: TextView by lazy { itemDataClass_description }
}