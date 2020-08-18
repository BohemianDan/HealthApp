package com.example.selfevident

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.*
import com.example.selfevident.casedatabase.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ViewActivity : AppCompatActivity() {

    private lateinit var caseViewModel: CaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)
        title = "hello"

        //view specific journal entry
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            val case_id: Int = bundle.get("id") as Int
            val feeling = findViewById<TextView>(R.id.feelingView)
            val summary = findViewById<TextView>(R.id.summaryView)
            val story = findViewById<TextView>(R.id.storyView)
            val patternView = findViewById<TextView>(R.id.patternView)
            /* wordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)
            var result = wordViewModel.allWords
            var result2 = result.value
             */
            caseViewModel =
                CaseViewModel(application) //ViewModelProvider(this).get(WordViewModel::class.java)
            GlobalScope.launch {
                val all = caseViewModel.getCaseById(case_id)
                val important = if (all.isNotEmpty()) all[0] else Case(
                    0,
                    "Error, None found",
                    0,
                    "Error, None found",
                    "Error, None found",
                    null
                )
                title = "${important.datetime}: Rating of ${important.rating}"
                feeling.text = important.emotion // result[0].summary
                summary.text = important.summary
                story.text = important.story
                val patterns = caseViewModel.getPatternsByCase(cid = case_id)
                var str = ""
                for(pattern in patterns){
                    str+= pattern.relationship
                }
                patternView.text = str

            }

            val button = findViewById<Button>(R.id.addPatternButton)
            button.setOnClickListener {
                val dialog = Dialog(this)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.fragment_dialog)
                val newTag = dialog.findViewById<EditText>(R.id.newPatternEdit)
                val tagSpinner = dialog.findViewById<Spinner>(R.id.tagsSpinner)
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
                    val new : String = newTag.text.toString()

                    if(new.isNotEmpty() and new.isNotBlank()){
                        val newPattern = Pattern( new)
                        caseViewModel.insert(newPattern)
                        val cross = Cross(cid = case_id, pid = newPattern.relationship)
                        caseViewModel.insert(cross)
                    }

                    if(tagSpinner.selectedItemPosition!=0){
                        val newPattern = Pattern(tagSpinner.selectedItem.toString())
                        caseViewModel.insert(newPattern)
                        val cross = Cross(cid = case_id, pid = newPattern.relationship)
                        caseViewModel.insert(cross)
                    }

                    dialog.dismiss()
                    finish()
                    startActivity(intent)
                }
                noBtn.setOnClickListener { dialog.dismiss() }
                dialog.show()

            }


        } else {
            val dialog = Dialog(this)
            dialog.setTitle("ERROR")
            dialog.setCancelable(false)

            dialog.show()

        }


    }
}