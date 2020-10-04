package com.example.selfevident

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.selfevident.casedatabase.Case
import com.example.selfevident.casedatabase.CaseViewModel
import com.example.selfevident.casedatabase.Pattern
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.LabelFormatter
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.helper.StaticLabelsFormatter
import com.jjoe64.graphview.series.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue
import kotlin.math.sin

class TagActivity : AppCompatActivity() {
    private lateinit var caseViewModel: CaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag)

        caseViewModel =
            CaseViewModel(application) //ViewModelProvider(this).get(WordViewModel::class.java)

        val deleteButton: Button = findViewById(R.id.deleteTagButton)
        val editButton: Button = findViewById(R.id.editTagButton)
        val editBox: EditText = findViewById(R.id.editTagBox)
        val graphView: GraphView = findViewById(R.id.tagGraph)
        val spinner: Spinner = findViewById(R.id.selectTagSpinner)
        val statView: TextView = findViewById(R.id.tagStatView)

        GlobalScope.launch {
            val patterns: List<Pattern> = caseViewModel.getAllPatterns()
            val tags: MutableList<String> = ArrayList()

            if (patterns.isEmpty()) tags.add("No Tags")
            else {
                tags.add("Select Tag")
                for (itPattern in patterns) {
                    tags.add(itPattern.relationship)
                }
            }


            val arrayAdapter = ArrayAdapter<String>(
                applicationContext,
                android.R.layout.simple_spinner_dropdown_item,
                tags
            )

            spinner.adapter = arrayAdapter
        }


        graphView.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(applicationContext)


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val patternString: String = spinner.selectedItem.toString()

                if(patternString == "Select Tag") return

                GlobalScope.launch {
                    val crosses = caseViewModel.getCrossesByPattern(patternString)
                    val series = LineGraphSeries<DataPoint>()

                    var aveRating = 0
                    var aveIntensity = 0

                    var n = 0.0

                    for (cross in crosses) {
                        n+=1
                        val aCase: Case = caseViewModel.getCaseById(cross.cid)[0]
                        val tempDate = Date(aCase.datetime!!.time)
                        series.appendData(DataPoint(tempDate, aCase.rating.toDouble()), true, crosses.size)
                        aveRating += aCase.rating
                        aveIntensity += (aCase.rating-5).absoluteValue
                    }


                    graphView.removeAllSeries()
                    graphView.addSeries(series)
                    statView.text = patternString+": Average of ${aveRating.toDouble()/crosses.size}/10" + if (crosses.size<=1) ", Only One Item Tagged" else ""
                    graphView.title = "Average Intensity of ${aveIntensity.toDouble()/crosses.size}/5"

                    graphView.viewport.isYAxisBoundsManual = true
                    graphView.viewport.setMinY(0.0)
                    graphView.viewport.setMaxY(10.0)
                    graphView.viewport.isScalable = true
                    graphView.viewport.isScrollable = true

                    graphView.viewport.isXAxisBoundsManual = true

                    graphView.gridLabelRenderer.numHorizontalLabels =3
                    graphView.viewport.setMinX(series.lowestValueX)
                    graphView.viewport.setMaxX(series.highestValueX)

                   //graphView.gridLabelRenderer.isHumanRounding= false

                }
            }
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