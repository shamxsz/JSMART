package com.example.jsmart

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream

class LessonDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson_detail)

        val tvLessonTitle = findViewById<TextView>(R.id.tvLessonTitle)
        val tvLessonContent = findViewById<TextView>(R.id.tvLessonContent)


        val lessonId = intent.getIntExtra("lesson_id", 0)


        val jsonArray = loadJSONFromAssets()


        val lesson = jsonArray?.let { getLessonById(it, lessonId) }


        if (lesson != null) {
            tvLessonTitle.text = lesson.first
            tvLessonContent.text = lesson.second
        } else {
            tvLessonTitle.text = "Lesson Not Found"
            tvLessonContent.text = "No content available for this lesson."
        }
    }


    private fun loadJSONFromAssets(): JSONArray? {
        return try {
            val inputStream: InputStream = assets.open("lessons.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val jsonString = String(buffer, Charsets.UTF_8)


            JSONObject(jsonString).getJSONArray("lessons")
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }


    private fun getLessonById(jsonArray: JSONArray, id: Int): Pair<String, String>? {
        for (i in 0 until jsonArray.length()) {
            val lesson = jsonArray.getJSONObject(i)
            if (lesson.getInt("id") == id) {
                return Pair(lesson.getString("title"), lesson.getString("content"))
            }
        }
        return null
    }
}
