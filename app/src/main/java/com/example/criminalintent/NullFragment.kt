package com.example.criminalintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

class NullFragment(crime : Crime) : Fragment() {

    var c = crime
    private lateinit var inview: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_null, container, false)

        inview = view

        return view
    }

    override fun onStart() {
        super.onStart()

        if (c.isSolved){
            check(inview)
        }
    }

    fun check(view: View) {
        Snackbar.make(view, "Checked", Snackbar.LENGTH_LONG).show()
    }
}