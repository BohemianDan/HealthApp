package com.example.selfevident

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.selfevident.casedatabase.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ViewActivity : AppCompatActivity() {

    private lateinit var caseViewModel: CaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)


        //view specific journal entry
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            val id: Int = bundle.get("id") as Int
            val title = findViewById<TextView>(R.id.titleView)
            val summary = findViewById<TextView>(R.id.summaryView)
            val story = findViewById<TextView>(R.id.storyView)
            val pattern = findViewById<TextView>(R.id.patternView)
            /* wordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)
            var result = wordViewModel.allWords
            var result2 = result.value
             */
            caseViewModel =
                CaseViewModel(application) //ViewModelProvider(this).get(WordViewModel::class.java)
            //wordViewModel.insert(Case(0, "Pissed", "why won't this work Right?"))
            GlobalScope.launch {
                var all = caseViewModel.get(id)
                var important = if (all.isNotEmpty()) all[0] else Case(
                    0,
                    "Error, None found",
                    "Error, None found",
                    "Error, None found",
                    "Error, None found"
                )
                title.text = "${important.emotion} on ${important.datetime}" // result[0].summary
                summary.text = important.summary
                story.text = important.story

            }
        } else {
            //error code
        }

    }
}