package com.example.selfevident

import android.app.Dialog
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.selfevident.casedatabase.Case
import com.example.selfevident.casedatabase.CaseViewModel
import com.example.selfevident.casedatabase.Cross
import com.example.selfevident.casedatabase.Pattern
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
            val caseId: Int = bundle.get("id") as Int
            val feelingView = findViewById<TextView>(R.id.feelingView)
            val summaryView = findViewById<TextView>(R.id.summaryView)
            val storyView = findViewById<TextView>(R.id.storyView)
            val patternView = findViewById<TextView>(R.id.patternView)
            feelingView.movementMethod = ScrollingMovementMethod()
            summaryView.movementMethod = ScrollingMovementMethod()
            storyView.movementMethod = ScrollingMovementMethod()
            patternView.movementMethod = ScrollingMovementMethod()

            /* wordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)
            var result = wordViewModel.allWords
            var result2 = result.value
             */
            caseViewModel =
                CaseViewModel(application) //ViewModelProvider(this).get(WordViewModel::class.java)

            //may break if wrong thread??
            GlobalScope.launch {
                val all = caseViewModel.getCaseById(caseId)
                val important = if (all.isNotEmpty()) all[0] else Case(
                    0,
                    "Error, None found",
                    0,
                    "Error, None found",
                    "Error, None found",
                    null
                )
                title = "${important.datetime}: Rating of ${important.rating}"
                feelingView.text = important.emotion // result[0].summary
                summaryView.text = important.summary
                storyView.text = important.story
                val patterns = caseViewModel.getPatternsByCase(cid = caseId)
                var str = ""
                for(pattern in patterns){
                    str += pattern.relationship + ", "
                }
                str = str.slice(0 until str.length-2)
                patternView.text = str

            }

            val addButton = findViewById<Button>(R.id.addPatternButton)
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
                    val new : String = newTag.text.toString()

                    if(new.isNotEmpty() and new.isNotBlank()){
                        val newPattern = Pattern( new)
                        caseViewModel.insert(newPattern)
                        val cross = Cross(cid = caseId, pid = newPattern.relationship)
                        caseViewModel.insert(cross)
                    }

                    if(tagSpinner.selectedItemPosition!=0){
                        val newPattern = Pattern(tagSpinner.selectedItem.toString())
                        //caseViewModel.insert(newPattern)
                        val cross = Cross(cid = caseId, pid = newPattern.relationship)
                        caseViewModel.insert(cross)
                    }

                    dialog.dismiss()
                    GlobalScope.launch{
                        val patterns = caseViewModel.getPatternsByCase(cid = caseId)
                        var str = ""
                        for(pattern in patterns){
                            str += pattern.relationship + ", "
                        }
                        str = str.slice(0 until str.length-2)
                        patternView.text = str
                    }
                }
                noBtn.setOnClickListener { dialog.dismiss() }
                dialog.show()

            }

            val removeButton = findViewById<Button>(R.id.removePatternButton)
            removeButton.setOnClickListener {
                val dialog = Dialog(this)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.remove_dialog)
                val tagSpinner = dialog.findViewById<Spinner>(R.id.removeTagSpinner)
                val yesBtn = dialog.findViewById(R.id.removeTagBtn) as Button
                val noBtn = dialog.findViewById(R.id.noRemoveBtn) as TextView

                GlobalScope.launch {
                    val patterns: List<Pattern> = caseViewModel.getPatternsByCase(caseId)
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
                    if(tagSpinner.selectedItemPosition!=0){
                        val newPattern = Pattern(tagSpinner.selectedItem.toString())
                        val cross = Cross(cid = caseId, pid = newPattern.relationship)
                        caseViewModel.delete(cross)
                    }
                    dialog.dismiss()
                    GlobalScope.launch{
                        val patterns = caseViewModel.getPatternsByCase(cid = caseId)
                        var str = ""
                        for(pattern in patterns){
                            str += pattern.relationship + ", "
                        }
                        str = str.slice(0 until str.length-2)
                        patternView.text = str
                    }
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