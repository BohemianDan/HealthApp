package com.example.selfevident

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.selfevident.casedatabase.CaseViewModel
import com.example.selfevident.casedatabase.Pattern
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TagActivity : AppCompatActivity() {
    private lateinit var caseViewModel: CaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag)

        caseViewModel =
            CaseViewModel(application) //ViewModelProvider(this).get(WordViewModel::class.java)

        val deleteButton: Button = findViewById<Button>(R.id.deleteTagButton)
        val editButton: Button = findViewById<Button>(R.id.editTagButton)
        val editBox: EditText = findViewById<EditText>(R.id.editTagBox)

        val spinner: Spinner = findViewById<Spinner>(R.id.selectTagSpinner)

        GlobalScope.launch {
            val patterns: List<Pattern> = caseViewModel.getAllPatterns()
            val tags: MutableList<String> = ArrayList()

            tags.add("Select Tag")
            for (itPattern in patterns) {
                tags.add(itPattern.relationship)
            }
            if(patterns.isEmpty()) tags.add("No Tags")
            val arrayAdapter = ArrayAdapter<String>(
                applicationContext,
                android.R.layout.simple_spinner_dropdown_item,
                tags
            )

            spinner.adapter = arrayAdapter
        }

        deleteButton.setOnClickListener {
            if (spinner.selectedItemPosition == 0) {
                Toast.makeText(
                    applicationContext,
                    R.string.no_tag_selected,
                    Toast.LENGTH_LONG
                ).show()
            } else {
                caseViewModel.delete(Pattern(spinner.selectedItem.toString()))
                finish()
                startActivity(intent)
                Toast.makeText(
                    applicationContext,
                    "Tag Deleted",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        editButton.setOnClickListener {
            if (spinner.selectedItemPosition == 0) {
                Toast.makeText(
                    applicationContext,
                    R.string.no_tag_selected,
                    Toast.LENGTH_LONG
                ).show()
            } else if (editBox.text.isEmpty() || editBox.text.isBlank()) {
                Toast.makeText(
                    applicationContext,
                    "Please Enter a New Name",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                caseViewModel.updatePattern(spinner.selectedItem.toString(), editBox.text.toString())
                finish()
                startActivity(intent)
                Toast.makeText(
                    applicationContext,
                    "Tag Changed",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

}