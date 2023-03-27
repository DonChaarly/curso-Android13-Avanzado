package com.cursosandroidant.hellokotlin

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() , TextToSpeech.OnInitListener {

    var tts : TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tts = TextToSpeech(this, this)

        findViewById<Button>(R.id.btnPlay)!!.setOnClickListener{speak()}
    }

    private fun speak() {
        var message = findViewById<EditText>(R.id.etMessage).text.toString()

        if (message.isEmpty()) {
            message = "¿Es en serio? Ya pon algo en el edit text"
        }
        tts!!.speak(message, TextToSpeech.QUEUE_FLUSH, null, "")
        /*if (!message.isEmpty()) {
            tts!!.speak(message, TextToSpeech.QUEUE_FLUSH, null, "")
        }*/
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            findViewById<TextView>(R.id.tvStatus).text = "Listo!"
            tts!!.setLanguage(Locale("ES"))
        } else {
            findViewById<TextView>(R.id.tvStatus).text = "No disponible :("
        }
    }

    override fun onDestroy() {
        if (tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }

        super.onDestroy()
    }
}