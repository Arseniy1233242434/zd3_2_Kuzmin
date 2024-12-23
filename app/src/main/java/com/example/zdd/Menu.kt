package com.example.zdd

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Menu : AppCompatActivity() {
    lateinit var pref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        pref = getSharedPreferences("PREF", MODE_PRIVATE)
    }

    fun channel1(view: View) {
        val intent = Intent(this, QuestsActivity::class.java)
        val stringArray: Set<String> =
            setOf("Limitless", "Chaplin", "Green Book", "Batman", "Smile", "Avengers")
        pref.edit().putStringSet("Array", stringArray).apply()
        startActivity(intent)
    }
    fun channel2(view: View) {
        val intent = Intent(this, QuestsActivity::class.java)
        val stringArray: Set<String> =
            setOf("Titanik", "Friends", "The Pacifier", "Mongol", "Mummy", "Over The Hedge")
        pref.edit().putStringSet("Array", stringArray).apply()
        startActivity(intent)
    }
    fun channel3(view: View) {
        val intent = Intent(this, QuestsActivity::class.java)
        val stringArray: Set<String> =
            setOf("Halloween", "Annabelle", "Never Let Go", "The Holiday", "The Wild Robot")
        pref.edit().putStringSet("Array", stringArray).apply()
        startActivity(intent)
    }
}