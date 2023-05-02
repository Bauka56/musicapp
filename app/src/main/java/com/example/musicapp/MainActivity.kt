package com.example.musicapp

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.databinding.ActivitySongBinding
import java.util.concurrent.TimeUnit


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingSong: ActivitySongBinding
    private lateinit var songArrayList: ArrayList<Song>

    private lateinit var mediaPlayer: MediaPlayer

    private var statusPlaying: Boolean = true

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingSong = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)



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


            mediaPlayer.stop()
            mediaPlayer = MediaPlayer.create(this, songArrayList[position].songID)
            openSong(position, mediaPlayer, statusPlaying)

        }


        /*

         */

    }

    private fun playSong(){
        if (!mediaPlayer!!.isPlaying) {
            mediaPlayer!!.start()
            binding.play.setBackgroundResource(R.drawable.baseline_pause_24)
            statusPlaying = true
        }
        else{
            mediaPlayer!!.pause()
            binding.play.setBackgroundResource(R.drawable.baseline_play_arrow_24)
            statusPlaying = false
        }
    }

    private fun openSong(position : Int, mediaPlayer: MediaPlayer, statusPlaying : Boolean){

        val duration = mediaPlayer.duration
        val durationLong = duration.toLong()


        val songName = songNameArray[position]
        val artistName = artistNameArray[position]
        val imageId = imageIdArr[position]
        val songId = songIdArray[position]


        var i = Intent(this, SongActivity::class.java)
        i.putExtra("songName", songName)
        i.putExtra("artistName", artistName)
        i.putExtra("imageId", imageId)
        i.putExtra("songId", songId)
        i.putExtra("songDuration", durationConverter(durationLong))
        i.putExtra("position", position)
        i.putExtra("result", mediaPlayer!!.currentPosition)

        i.putExtra("imageArray", imageIdArr)
        i.putExtra("songNameArray", songNameArray)
        i.putExtra("artistNameArray", artistNameArray)
        i.putExtra("songIdArray", songIdArray)

        i.putExtra("statusPlaying", statusPlaying)

        startActivityForResult(i, 0)
        if(mediaPlayer.isPlaying){
            mediaPlayer.stop()
            mediaPlayer.reset()
        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

                val newPosition = data!!.getIntExtra("newPosition", R.drawable.ariana)                // Get the result from intent
                val result = data!!.getIntExtra("result", R.drawable.ariana)
                statusPlaying = data!!.getBooleanExtra("isPlaying", true)

                mediaPlayer = MediaPlayer.create(this, songArrayList[newPosition].songID)
                mediaPlayer.seekTo(result)
                statusPlaying = if(statusPlaying){
                    mediaPlayer.start()
                    binding.play.setBackgroundResource(R.drawable.baseline_pause_24)
                    true
                }else{
                    binding.play.setBackgroundResource(R.drawable.baseline_play_arrow_24)
                    false
                }
                binding.musController.visibility = View.VISIBLE
                binding.imageId.setImageResource(songArrayList[newPosition].imageID)

                binding.musController.isClickable = true

                binding.play.setOnClickListener {
                    playSong()
                }

                binding.musController.setOnClickListener {
                    openSong(newPosition, mediaPlayer, statusPlaying)
                }
            }
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