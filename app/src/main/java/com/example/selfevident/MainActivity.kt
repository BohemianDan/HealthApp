package com.example.selfevident

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.selfevident.casedatabase.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val newWordActivityRequestCode = 1
    private lateinit var caseViewModel: CaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val emptyView = findViewById<TextView>(R.id.emptyView)
        emptyView.text = ""


        val adapter = CaseListAdapter(this, this::viewEvent)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        caseViewModel = CaseViewModel(application) //ViewModelProvider(this).get(WordViewModel::class.java)
        caseViewModel.allWords.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.setWords(it) }
        })

        val flag = false
        val empty = false

        //may break if wrong thread??
        GlobalScope.launch {
            if (caseViewModel.getAllCases().isNullOrEmpty()){
                emptyView.text = resources.getString(R.string.empty_db)
            }
        }


        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewCaseActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }



        val tagBtn = findViewById<Button>(R.id.tagsIntentButton)

        tagBtn.setOnClickListener {

            var tagIntent = Intent(this@MainActivity, TagActivity::class.java)
            startActivity(tagIntent)
        }

    }

    private fun viewEvent(id: Int) {
        val intent = Intent(this@MainActivity, ViewActivity::class.java)
        intent.putExtra("id", id)
        //intent.putExtra("item", wordViewModel)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringArrayExtra(NewCaseActivity.EXTRA_REPLY)?.let {
            }
            emptyView.text=""
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}