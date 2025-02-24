package com.seifmortada.applications.quran.core.ui.custom_views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.seifmortada.applications.quran.R

class CustomToast(context: Context, message: String) : Toast(context) {

    init {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val layout: View = inflater.inflate(R.layout.custom_toast, null, false)

        val textView: TextView = layout.findViewById(R.id.toast_text)
        textView.text = message

        view = layout
        duration = LENGTH_SHORT
    }

    override fun setText(s: CharSequence?) {
        val textView: TextView? = view?.findViewById(R.id.toast_text)
        textView?.text = s
        super.setText(s)
    }
    companion object {
        fun makeText(context: Context, message: String, duration: Int = LENGTH_SHORT): CustomToast {
            return CustomToast(context, message).apply {
                this.duration = duration
            }
        }
    }
}