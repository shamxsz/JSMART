package com.example.jsmart

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView


class ModuleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_module, container, false)


        val lesson1 = view.findViewById<CardView>(R.id.Lesson1)
        val lesson2 = view.findViewById<CardView>(R.id.Lesson2)
        val lesson3 = view.findViewById<CardView>(R.id.Lesson3)

        lesson1.setOnClickListener { openLessonDetail(1) }
        lesson2.setOnClickListener { openLessonDetail(2) }
        lesson3.setOnClickListener { openLessonDetail(3) }

        return view
    }

    private fun openLessonDetail(lessonId: Int) {
        val intent = Intent(activity, LessonDetailActivity::class.java)
        intent.putExtra("lesson_id", lessonId)
        startActivity(intent)
    }
}
