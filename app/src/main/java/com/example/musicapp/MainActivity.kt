package com.example.musicapp

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.databinding.ActivitySongBinding
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingSong: ActivitySongBinding
    private lateinit var songArrayList: ArrayList<Song>

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingSong = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val imageIdArr = intArrayOf(
            R.drawable.ariana, R.drawable.bieber, R.drawable.dragons,
            R.drawable.lsd, R.drawable.natking, R.drawable.sia,
            R.drawable.smith
        )

        val songNameArray = arrayOf(
            "7 Rings",
            "Mount",
            "Bones",
            "LS",
            "Boy",
            "S",
            "Unholy"
        )

        val artistNameArray = arrayOf(
            "Ariana Grande",
            "SM & JB",
            "Imagine Dragons",
            "LSD",
            "Nat King",
            "Sia",
            "Sam Smith"
        )

        val songIdArray = intArrayOf(
            R.raw.rings, R.raw.monster, R.raw.bones,
            R.raw.genius, R.raw.natureboy, R.raw.snowman,
            R.raw.unholy
        )

        songArrayList = ArrayList()

        for (i in songNameArray.indices) {

            mediaPlayer = MediaPlayer.create(this, songIdArray[i])
            val duration = mediaPlayer.duration
            val durationLong = duration.toLong()
            val song = Song(
                songNameArray[i],
                artistNameArray[i],
                imageIdArr[i],
                songIdArray[i],
                durationConverter(durationLong)
            )
            songArrayList.add(song)

        }

        binding.listView.isClickable = true
        binding.listView.adapter = MyAdapter(this, songArrayList)

        binding.listView.setOnItemClickListener { parent, view, position, id ->

            val songName = songNameArray[position]
            val artistName = artistNameArray[position]
            val imageId = imageIdArr[position]
            val songId = songIdArray[position]

            mediaPlayer = MediaPlayer.create(this, songId)
            val duration = mediaPlayer.duration
            val durationLong = duration.toLong()

            var i = Intent(this, SongActivity::class.java)
            i.putExtra("songName", songName)
            i.putExtra("artistName", artistName)
            i.putExtra("imageId", imageId)
            i.putExtra("songId", songId)
            i.putExtra("songDuration", durationConverter(durationLong))
            i.putExtra("position", position)

            i.putExtra("imageArray", imageIdArr)
            i.putExtra("songNameArray", songNameArray)
            i.putExtra("artistNameArray", artistNameArray)
            i.putExtra("songIdArray", songIdArray)

            startActivity(i)

        }


    }

    private fun durationConverter(duration: Long): String {
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(duration),
            TimeUnit.MILLISECONDS.toSeconds(duration) -
                    TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(duration)
                    )
        )
    }

}