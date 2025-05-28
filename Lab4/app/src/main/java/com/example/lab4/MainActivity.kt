package com.example.lab4

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var videoView: VideoView
    private lateinit var audioSeekBar: SeekBar
    private lateinit var urlInput: TextInputEditText

    private var isAudio = true
    private var mediaUri: Uri? = null

    private val handler = android.os.Handler()
    private var updateSeekBarRunnable: Runnable? = null

    private val filePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            mediaUri = it
            playMedia(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        videoView = findViewById(R.id.videoView)
        audioSeekBar = findViewById(R.id.audioSeekBar)
        urlInput = findViewById(R.id.urlInput)

        val audioRadio = findViewById<RadioButton>(R.id.audioRadio)
        val videoRadio = findViewById<RadioButton>(R.id.videoRadio)
        val chooseFileBtn = findViewById<MaterialButton>(R.id.chooseFileBtn)
        val loadFromUrlBtn = findViewById<MaterialButton>(R.id.loadFromUrlBtn)
        val playBtn = findViewById<MaterialButton>(R.id.playBtn)
        val pauseBtn = findViewById<MaterialButton>(R.id.pauseBtn)
        val stopBtn = findViewById<MaterialButton>(R.id.stopBtn)

        audioRadio.setOnClickListener {
            isAudio = true
            videoView.visibility = View.GONE
            audioSeekBar.visibility = View.VISIBLE
        }

        videoRadio.setOnClickListener {
            isAudio = false
            videoView.visibility = View.VISIBLE
            audioSeekBar.visibility = View.GONE
        }

        chooseFileBtn.setOnClickListener {
            val type = if (isAudio) "audio/*" else "video/*"
            filePicker.launch(type)
        }

        loadFromUrlBtn.setOnClickListener {
            val url = urlInput.text.toString()
            if (url.isNotBlank()) {
                val uri = Uri.parse(url)
                mediaUri = uri
                playMedia(uri)
            }
        }

        playBtn.setOnClickListener {
            if (isAudio && ::mediaPlayer.isInitialized) {
                mediaPlayer.start()
                startUpdatingSeekBar()
            } else if (!isAudio && mediaUri != null) {
                videoView.start()
            }
        }

        pauseBtn.setOnClickListener {
            if (isAudio && ::mediaPlayer.isInitialized) {
                mediaPlayer.pause()
            } else if (!isAudio && videoView.isPlaying) {
                videoView.pause()
            }
        }

        stopBtn.setOnClickListener {
            if (isAudio && ::mediaPlayer.isInitialized) {
                mediaPlayer.stop()
                mediaPlayer.release()
                handler.removeCallbacks(updateSeekBarRunnable!!)
                audioSeekBar.progress = 0
            } else if (!isAudio && videoView.isPlaying) {
                videoView.stopPlayback()
            }
        }
    }

    private fun playMedia(uri: Uri) {
        if (isAudio) {
            videoView.visibility = View.GONE
            audioSeekBar.visibility = View.VISIBLE

            if (::mediaPlayer.isInitialized) {
                mediaPlayer.release()
            }

            mediaPlayer = MediaPlayer().apply {
                setDataSource(this@MainActivity, uri)
                prepare()
                start()
                audioSeekBar.max = duration
            }

            startUpdatingSeekBar()

            audioSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) mediaPlayer.seekTo(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

        } else {
            if (::mediaPlayer.isInitialized) {
                mediaPlayer.stop()
                mediaPlayer.release()
            }

            audioSeekBar.visibility = View.GONE
            videoView.visibility = View.VISIBLE

            videoView.stopPlayback()
            videoView.setVideoURI(uri)
            videoView.setOnPreparedListener {
                videoView.start()
            }
        }
    }


    private fun startUpdatingSeekBar() {
        updateSeekBarRunnable = object : Runnable {
            override fun run() {
                if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
                    audioSeekBar.progress = mediaPlayer.currentPosition
                    handler.postDelayed(this, 500)
                }
            }
        }
        handler.post(updateSeekBarRunnable!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
        handler.removeCallbacks(updateSeekBarRunnable ?: Runnable {})
    }
}
