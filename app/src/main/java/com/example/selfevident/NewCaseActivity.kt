package com.example.selfevident

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.view.Window
import android.widget.*
import com.example.selfevident.casedatabase.Case
import com.example.selfevident.casedatabase.CaseViewModel
import com.example.selfevident.casedatabase.Cross
import com.example.selfevident.casedatabase.Pattern
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

/**
 * Activity for entering a word.
 */

class NewCaseActivity : AppCompatActivity() {

    private lateinit var editEmotionSpinner: Spinner
    private lateinit var editEmotionView: EditText
    private lateinit var editSummaryView: EditText
    private lateinit var editStoryView: EditText
    private lateinit var tagListView: TextView

    private lateinit var caseViewModel: CaseViewModel


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_word)

        editEmotionSpinner = findViewById(R.id.edit_emotion_spinner)
        editEmotionView = findViewById(R.id.edit_emotion)
        editSummaryView = findViewById(R.id.edit_summary)
        editStoryView = findViewById(R.id.edit_story)
        caseViewModel =
            CaseViewModel(application) //ViewModelProvider(this).get(WordViewModel::class.java)

        tagListView = findViewById(R.id.patternViewNewCase)

        val finalTags: MutableList<String> = ArrayList()


        //val emotions = resources.getStringArray(R.array.emotion_array)

        val addButton = findViewById<Button>(R.id.addPatternButtonNewCase)
        addButton.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.add_dialog)
            val newTag = dialog.findViewById<EditText>(R.id.newPatternEdit)
            val tagSpinner = dialog.findViewById<Spinner>(R.id.addTagSpinner)
            val yesBtn = dialog.findViewById(R.id.addTagBtn) as Button
            val noBtn = dialog.findViewById(R.id.noBtn) as TextView

            GlobalScope.launch {
                val patterns: List<Pattern> = caseViewModel.getAllPatterns()
                val tags: MutableList<String> = ArrayList()

                tags.add("Select Tag")
                for (itPattern in patterns) {
                    tags.add(itPattern.relationship)
                }
                val arrayAdapter = ArrayAdapter<String>(
                    applicationContext,
                    android.R.layout.simple_spinner_dropdown_item,
                    tags
                )

                tagSpinner.adapter = arrayAdapter

            }

            yesBtn.setOnClickListener {
                val new: String = newTag.text.toString()

                if (new.isNotEmpty() and new.isNotBlank()) {
                    val newPattern = Pattern(new)
                    caseViewModel.insert(newPattern)
                    finalTags.add(new)
                }

                if (tagSpinner.selectedItemPosition != 0) {
                    val newPattern = Pattern(tagSpinner.selectedItem.toString())
                    finalTags.add(newPattern.relationship)
                }

                dialog.dismiss()
                val patterns = finalTags
                var str = ""
                for (pattern in patterns) {
                    str += "$pattern, "
                }
                str = str.slice(0 until str.length - 2)
                tagListView.text = str

            }
            noBtn.setOnClickListener { dialog.dismiss() }
            dialog.show()

        }

        val removeButton = findViewById<Button>(R.id.removePatternButtonNewCase)
        removeButton.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.remove_dialog)
            val tagSpinner = dialog.findViewById<Spinner>(R.id.removeTagSpinner)
            val yesBtn = dialog.findViewById(R.id.removeTagBtn) as Button
            val noBtn = dialog.findViewById(R.id.noRemoveBtn) as TextView

            GlobalScope.launch {
                val patterns = finalTags
                val tags: MutableList<String> = ArrayList()

                tags.add("Select Tag")
                for (itPattern in patterns) {
                    tags.add(itPattern)
                }
                val arrayAdapter = ArrayAdapter<String>(
                    applicationContext,
                    android.R.layout.simple_spinner_dropdown_item,
                    tags
                )

                tagSpinner.adapter = arrayAdapter

            }

            yesBtn.setOnClickListener {
                if (tagSpinner.selectedItemPosition != 0) {
                    val newPattern = Pattern(tagSpinner.selectedItem.toString())
                    finalTags.remove(newPattern.relationship)
                }
                dialog.dismiss()
                val patterns = finalTags
                var str = ""
                for (pattern in patterns) {
                    str += "$pattern, "
                }
                str = str.slice(0 until str.length - 2)
                tagListView.text = str

            }
            noBtn.setOnClickListener { dialog.dismiss() }
            dialog.show()

        }


        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (editEmotionSpinner.selectedItemPosition == 0 || TextUtils.isEmpty(editSummaryView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                var emotion = editEmotionView.text.toString()
                var rating = editEmotionSpinner.selectedItemPosition - 1

                var summary = editSummaryView.text.toString()
                var story = editStoryView.text.toString()

                val today = Date(System.currentTimeMillis())
                val aCase = Case(0, emotion, rating, summary, story, today)
                GlobalScope.launch {
                    val cid = caseViewModel.insert(aCase).toInt()

                    for (tag in finalTags) {
                        // val newPattern = Pattern(tag)
                        val cross = Cross(cid = cid, pid = tag)
                        caseViewModel.insert(cross)
                    }
                }

                replyIntent.putExtra(EXTRA_REPLY, arrayOf("data"))
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.caselistsql.REPLY"
    }
}