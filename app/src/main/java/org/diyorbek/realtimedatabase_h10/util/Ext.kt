package org.diyorbek.realtimedatabase_h10.util

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

object Ext {


}

fun View.click(action: (View) -> Unit) {
    this.setOnClickListener {
        action(it)
    }
}

fun TextInputEditText.makeText() = this.text.toString().trim()
fun Fragment.toast(text: String) {
    Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT).show()
}