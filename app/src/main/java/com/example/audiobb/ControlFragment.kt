package com.example.audiobb

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso

class ControlFragment : Fragment() {

    lateinit var layout: View
    lateinit var label: TextView
    lateinit var playButton: Button
    lateinit var pauseButton: Button
    lateinit var stopButton: Button

    companion object {
        @JvmStatic
        fun newInstance() = ControlFragment()
    }

    interface Controller {
        fun play()
        fun pause()
        fun stop()
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
        playButton = layout.findViewById(R.id.playButton)
        pauseButton = layout.findViewById(R.id.pauseButton)
        stopButton = layout.findViewById(R.id.stopButton)

        ViewModelProvider(requireActivity())
            .get(BookViewModel::class.java)
            .getSelectedBook()
            .observe(viewLifecycleOwner, {updateLabels()})

        playButton.setOnClickListener { (requireActivity() as Controller).play() }
        pauseButton.setOnClickListener { (requireActivity() as Controller).pause() }
        stopButton.setOnClickListener { (requireActivity() as Controller).stop() }
    }


    private fun updateLabels() {
        val book = ViewModelProvider(requireActivity())
            .get(BookViewModel::class.java)
            .getSelectedBook()


        label.text = "Now Playing: " + book.value?.name
    }

}