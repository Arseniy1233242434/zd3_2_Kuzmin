package com.example.zdd

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.json.JSONObject

class QuestsActivity : AppCompatActivity() {
    lateinit var stringArray: Set<String>
    lateinit var spinner: Spinner
    lateinit var gridLayout: GridLayout
    lateinit var view: View
    lateinit var edit: EditText
    lateinit var pref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quests)
        gridLayout = findViewById(R.id.gridLayout)
        view = findViewById(R.id.questsView)
        edit = findViewById(R.id.editSearch)
        pref = getSharedPreferences("PREF", MODE_PRIVATE)
        stringArray = pref.getStringSet("Array", setOf())!!
        val items = arrayOf("Мультфильмы", "Хорроры", "Фентези")
        val spinner: Spinner = findViewById(R.id.my_spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        return
                    }
                    1 -> {
                        val stringArray: Set<String> = setOf("Smile", "Annabelle", "The Mare", "The Mummy")
                        pref.edit().putStringSet("Array", stringArray).apply()
                        startActivity(intent)
                    }
                    2 -> {
                        val stringArray: Set<String> = setOf("Batman","Avengers")
                        pref.edit().putStringSet("Array", stringArray).apply()
                        startActivity(intent)
                    }
                }
                val intent = Intent(this@QuestsActivity, QuestsActivity::class.java) // Создайте новый Intent



                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@QuestsActivity, "Выбрано: $selectedItem", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        val itemWidth = 1800 / 3

        var rows: Int = 0
        var columns: Int = 0

        if (stringArray != null) {
            for (item in stringArray) {
                var url = "https://www.omdbapi.com/?apikey=8424b5c9&t=" + item

                val queue = Volley.newRequestQueue(this)
                val stringRequest = StringRequest(
                    com.android.volley.Request.Method.GET,
                    url,
                    { responce ->
                        val obj = JSONObject(responce)

                        var poster = obj.getString("Poster")
                        var title = obj.getString("Title")
                        var year = obj.getString("Year")

                        val linearLayout = LinearLayout(this@QuestsActivity).apply {
                            orientation = LinearLayout.VERTICAL
                            layoutParams = GridLayout.LayoutParams().apply {
                                rowSpec = GridLayout.spec(rows)
                                columnSpec = GridLayout.spec(columns)
                            }
                        }

                        val titleTextView = TextView(this@QuestsActivity).apply {
                            text = title
                            textSize = 20F
                            layoutParams = LayoutParams(itemWidth, LayoutParams.WRAP_CONTENT)
                            gravity = Gravity.CENTER
                        }
                        titleTextView.setTextColor(getResources().getColor(R.color.black))

                        val yearTextView = TextView(this@QuestsActivity).apply {
                            text = year
                            textSize = 20F
                            layoutParams = LayoutParams(itemWidth, LayoutParams.WRAP_CONTENT)
                            gravity = Gravity.CENTER
                        }
                        yearTextView.setTextColor(getResources().getColor(R.color.black))

                        linearLayout.addView(titleTextView)
                        linearLayout.addView(yearTextView)
                        val imageView = ImageView(this@QuestsActivity).apply {
                            var params =
                                LinearLayout.LayoutParams(itemWidth, LayoutParams.MATCH_PARENT)
                            params.setMargins(20, 10, 20, 10)
                            layoutParams = params
                            scaleType = ImageView.ScaleType.CENTER_CROP
                            Picasso.get()
                                .load(poster)
                                .placeholder(R.drawable.holder)
                                .into(this)
                            adjustViewBounds = true
                        }
                        linearLayout.addView(imageView)

                        gridLayout.addView(linearLayout)
                        columns += 1
                        if (columns == 3) {
                            rows += 1
                            columns = 0
                        }
                    },
                    {
                        Snackbar.make(view, "Не удалось выполнить запрос", Snackbar.LENGTH_LONG)
                            .show()
                    }
                )
                queue.add(stringRequest)
            }
        }
    }
    fun Search(view: View) {
        if (edit.text.toString().isNotEmpty() && edit.text.toString() != null) {
            var key = "136c658a"
            var url =
                "https://www.omdbapi.com/?i=tt3896198&apikey=" + key + "&t=" + edit.text.toString()
            val queue = Volley.newRequestQueue(this)
            val stringRequest = StringRequest(
                Request.Method.GET,
                url,
                { response ->
                    val obj = JSONObject(response)

                    val filmName = obj.getString("Title")

                    val filmYear = obj.getInt("Year")

                    val filmRuntime = obj.getString("Runtime")

                    val filmGenre = obj.getString("Genre")

                    val imageUrl = obj.getString("Poster")

                    pref.edit().putString("poster", imageUrl).apply()
                    pref.edit().putString("title", filmName).apply()
                    pref.edit().putString("year", filmYear.toString()).apply()
                    pref.edit().putString("genre", filmGenre).apply()
                    pref.edit().putString("plot", filmRuntime).apply()

                    val intent = Intent(this, Result::class.java)
                    startActivity(intent)
                },
                {

                    val snackbar = Snackbar.make(view, "Фильм не найден!", Snackbar.LENGTH_LONG)
                    snackbar.show()
                }
            )
            queue.add(stringRequest)
        } else {

            val snackbar = Snackbar.make(view, "Введите название фильма", Snackbar.LENGTH_LONG)
            snackbar.show()
        }
    }
    fun goBack(view: View) {
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
    }
}