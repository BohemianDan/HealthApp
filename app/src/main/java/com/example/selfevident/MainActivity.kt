package com.example.selfevident

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.selfevident.casedatabase.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private val newWordActivityRequestCode = 1
    private lateinit var caseViewModel: CaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)

        val adapter = CaseListAdapter(this, this::viewEvent)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        caseViewModel =
            CaseViewModel(application) //ViewModelProvider(this).get(WordViewModel::class.java)
        caseViewModel.allWords.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.setWords(it) }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewWordActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }

    }

    private fun viewEvent(id: Int){
        val intent = Intent(this@MainActivity, ViewActivity::class.java)
        intent.putExtra("id", id)
        //intent.putExtra("item", wordViewModel)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringArrayExtra(NewWordActivity.EXTRA_REPLY)?.let {
                val now = Calendar.getInstance()
                val str = "${now.get(Calendar.YEAR)}-${now.get(Calendar.MONTH)}-${now.get(Calendar.DATE)}"
                val aCase = Case(0, it[0], it[1], it[2], str)
                caseViewModel.insert(aCase)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }
}