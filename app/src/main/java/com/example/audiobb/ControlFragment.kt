package com.example.audiobb

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso

class ControlFragment : Fragment() {

    lateinit var layout: View
    lateinit var label: TextView

    companion object {
        @JvmStatic
        fun newInstance() = ControlFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_control, container, false)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        label = layout.findViewById(R.id.nowPlayingLabel)

        ViewModelProvider(requireActivity())
            .get(BookViewModel::class.java)
            .getSelectedBook()
            .observe(viewLifecycleOwner, {updateLabels()})
    }


    private fun updateLabels() {
        val book = ViewModelProvider(requireActivity())
            .get(BookViewModel::class.java)
            .getSelectedBook()


        label.text = "Now Playing: " + book.value?.name
    }

}