package com.example.selfevident

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner

/**
 * Activity for entering a word.
 */

class NewCaseActivity : AppCompatActivity() {

    private lateinit var editEmotionSpinner: Spinner
    private lateinit var editEmotionView: EditText
    private lateinit var editSummaryView: EditText
    private lateinit var editStoryView: EditText


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_word)

        editEmotionSpinner = findViewById(R.id.edit_emotion_spinner)
        editEmotionView = findViewById(R.id.edit_emotion)
        editSummaryView = findViewById(R.id.edit_summary)
        editStoryView = findViewById(R.id.edit_story)

        //val emotions = resources.getStringArray(R.array.emotion_array)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (editEmotionSpinner.selectedItemPosition == 0 || TextUtils.isEmpty(editSummaryView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                var emotion = editEmotionView.text.toString()
                var rating = editEmotionSpinner.selectedItemPosition-1

                var summary = editSummaryView.text.toString()
                var story = editStoryView.text.toString()

                val data = arrayOf(emotion, rating.toString(), summary, story)

                replyIntent.putExtra(EXTRA_REPLY, data)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.caselistsql.REPLY"
    }
}