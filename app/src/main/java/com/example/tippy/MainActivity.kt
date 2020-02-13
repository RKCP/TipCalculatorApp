package com.example.tippy

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
private val BASE_START = 0

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentage.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT) // starting the app with the initial tip as the description for how good the tip is.
        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                Log.i(TAG, "onProgressChanged $progress") // console log statement
                tvTipPercentage.text = "$progress%" // dollar sign progress, means replace this text with the value given from progress
                updateTipDescription(progress)
                computeTipAndTotal()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


        etBase.addTextChangedListener(object: TextWatcher {

            override fun afterTextChanged(s: Editable?) {

                Log.i(TAG, "afterChanged $s")
                computeTipAndTotal()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun updateTipDescription(tipPercentage: Int) {
        val tipDescription : String

        when(tipPercentage) {
            in 0..9 -> tipDescription = "Poor"
            in 10..14 -> tipDescription = "Acceptable"
            in 15..19 -> tipDescription = "Great"
            in 20..24 -> tipDescription = "Excellent"
            else -> tipDescription = "SUPER SERVICE!"
        }
        tvTipDescription.text = tipDescription
        val color = ArgbEvaluator().evaluate(tipPercentage.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.colorWorstTip),
            ContextCompat.getColor(this, R.color.colorBestTip)) as Int
        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {

        // get the value of the base and tip percentage

        //conditional if the text is empty, e.g. backspace or starts empty
        if (etBase.text.isEmpty()) { // dont have to add toSting method to text to use isEmpty, as it has an isEmpty method already
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            return // set both texts to empty, and then return early so the rest of the method is NOT executed.
        }
        val baseAmount = etBase.text.toString().toDouble()
        val tipPercantage = seekBarTip.progress //already an int returned

        // then calculate the tip amount which is tip % * the base

        tvTipAmount.text = "%.2f".format((baseAmount / 100) * tipPercantage)

        // then calculate the total which is the tip + base

        tvTotalAmount.text = "%.2f".format(tvTipAmount.text.toString().toDouble() + baseAmount)
    }
}
