package com.example.audiobb

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class ControlFragment : Fragment() {

    lateinit var layout: View
    lateinit var label: TextView
    lateinit var playButton: Button
    lateinit var stopButton: Button
    lateinit var seekBar: SeekBar
    var bookID = -1

    var playing = false
    var stopped = true

    companion object {
        @JvmStatic
        fun newInstance(): ControlFragment {

            val frag = ControlFragment().apply {
                arguments = Bundle()
            }
            return frag
        }
    }

    interface Controller {
        fun play()
        fun pause()
        fun stop(button: Boolean)
        fun seek(progress: Int)
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
        stopButton = layout.findViewById(R.id.stopButton)
        seekBar = layout.findViewById(R.id.seekBar)

        arguments?.let {

            playing = it.getBoolean("playing")
            stopped = it.getBoolean("stopped")
            bookID = it.getInt("bookID")

            Log.i("Playing: ", "$playing")
            Log.i("Stopped: ", "$stopped")

            if (playing) {
                playButton.text = "Pause"
            } else {
                playButton.text = "Play"
            }
        }

        ViewModelProvider(requireActivity())
            .get(BookViewModel::class.java)
            .getSelectedBook()
            .observe(viewLifecycleOwner, {updateLabels()})

        ViewModelProvider(requireActivity())
            .get(BookViewModel::class.java)
            .getBookProgress()
            .observe(viewLifecycleOwner, {updateSeekBar()})

        playButton.setOnClickListener {

            if (stopped) {
                (requireActivity() as Controller).play()

                playButton.text = "Pause"
                stopped = false
                playing = true
            } else {
                (requireActivity() as Controller).pause()

                if (playing) {
                    playing = false
                    playButton.text = "Play"
                } else {
                    playing = true
                    playButton.text = "Pause"
                }
            }

            arguments?.apply {
                putBoolean("playing", playing)
                putBoolean("stopped", stopped)
            }
        }

        stopButton.setOnClickListener {
            (requireActivity() as Controller).stop(true)
            stopped = true
            playing = false
            playButton.text = "Play"

            arguments?.apply {
                putBoolean("playing", playing)
                putBoolean("stopped", stopped)
            }
        }

        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(bar: SeekBar?, progress: Int, user: Boolean) {
                if (user) {
                    (requireActivity() as Controller).seek(progress)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }


    private fun updateLabels() {
        val book = ViewModelProvider(requireActivity())
            .get(BookViewModel::class.java)
            .getSelectedBook()

        label.text = "Now Playing: " + book.value?.name
        seekBar.max = book.value?.duration!!

        if (book.value?.id != bookID) {
            bookID = book.value?.id!!

            (requireActivity() as Controller).stop(false)
            (requireActivity() as Controller).seek(0)
            playing = false
            playButton.text = "Play"
            stopped = true

            ViewModelProvider(requireActivity())
                .get(BookViewModel::class.java)
                .setBookProgress(0)

            arguments?.apply {
                putBoolean("playing", playing)
                putBoolean("stopped", stopped)
                putInt("bookID", bookID)
            }
        }

        val progress = requireActivity().getSharedPreferences(book.value?.name, MODE_PRIVATE)
            .getInt("Progress", 0)

        seekBar.progress = progress

    }

    private fun updateSeekBar() {
        val progress = ViewModelProvider(requireActivity())
            .get(BookViewModel::class.java)
            .getBookProgress()

        seekBar.progress = progress.value!!
    }

}