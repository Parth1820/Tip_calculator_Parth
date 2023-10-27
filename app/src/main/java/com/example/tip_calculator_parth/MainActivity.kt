package com.example.tip_calculator_parth

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.tip_calculator_parth.databinding.ActivityMainBinding

import java.text.NumberFormat
import java.util.*

/**
 * Created by Techpass Master.
 * Website - https://techpassmaster.com/
 * Email id - hello@techpassmaster.com
 */

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var countPerson = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edtBill.requestFocus()
        onViewClick()
    }

    private fun onViewClick() {

        with(binding) {
            btnCalculateTip.setOnClickListener {
                calculateTip()
            }

            btnPersonPlus.setOnClickListener {
                countPerson++
                tvPersonCount.text = countPerson.toString()
            }

            btnPersonMinus.setOnClickListener {
                if (countPerson > 1) {
                    countPerson--
                    tvPersonCount.text = countPerson.toString()
                }
            }

            imgCopyTip.setOnClickListener {
                val tipFormatted = "${tvTotalTip.text}\n" +
                        "${tvTotalBillPlusTip.text}\n" +
                        "${tvTipPerPerson.text}\n" +
                        "${tvTotalPerPerson.text}"

                copyTipResult(tipFormatted)
            }

            imgShareTip.setOnClickListener {
                val tipFormatted = "${tvTotalTip.text}\n" +
                        "${tvTotalBillPlusTip.text}\n" +
                        "${tvTipPerPerson.text}\n" +
                        "${tvTotalPerPerson.text}"

                shareTipResult(tipFormatted)
            }
        }
    }

    private fun calculateTip() {
        //      get bill cost from EditText.
        //      set total bill amount in the cost.
        val totalBill: String = binding.edtBill.text.toString()
        val cost = totalBill.toDoubleOrNull()

        if (totalBill.isNotEmpty()) {
            binding.imgCopyTip.visibility = View.VISIBLE
            binding.imgShareTip.visibility = View.VISIBLE

            //      get selected tip percentage
            val tipPercentage = when (binding.rgTipOptions.checkedRadioButtonId) {
                R.id.rbTipTwentyPercent -> 0.20
                R.id.rbTipFifteenPercent -> 0.15
                else -> 0.10
            }

            //      Calculate the tip according to percentage and set per person tip
            val tip = tipPercentage * cost!!
            showTotalTipResult(tip)

            //      Calculate & set the per person tip
            val perPersonTip = (tip / countPerson).toString()
            showPerPersonTipResult(perPersonTip.toDouble())

            //      Calculate & set total bill + tip
            val billPlusTip = cost.plus(tip)
            showTotalBillPlusTipResult(billPlusTip)

            //      Calculate & set per person total
            val totalPerPerson = (billPlusTip / countPerson).toString()
            showTotalPerPersonResult(totalPerPerson.toDouble())
        } else {
            Toast.makeText(this, "Please enter bill Amount", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showTotalTipResult(tip: Double) {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("en", "in")).format(tip)
        binding.tvTotalTip.text = ("Total Tip: $numberFormat")
    }

    private fun showTotalBillPlusTipResult(tip: Double) {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("en", "in")).format(tip)
        binding.tvTotalBillPlusTip.text = ("Total Amount (Bill+Tip): $numberFormat")
    }

    private fun showPerPersonTipResult(tip: Double) {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("en", "in")).format(tip)
        binding.tvTipPerPerson.text = ("Per Person Tip: $numberFormat")

        if (countPerson == 1) {
            binding.tvTipPerPerson.text = ("Tip: $numberFormat")
        } else {
            binding.tvTipPerPerson.text = ("Per Person Tip: $numberFormat")
        }
    }

    private fun showTotalPerPersonResult(tip: Double) {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("en", "in")).format(tip)
        if (countPerson == 1) {
            binding.tvTotalPerPerson.text = ("Total: $numberFormat")
        } else {
            binding.tvTotalPerPerson.text = ("Total Per Person: $numberFormat")
        }
    }

    private fun copyTipResult(copyTipResultText : String){
        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", copyTipResultText)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, "Tip copied", Toast.LENGTH_LONG).show()
    }

    private fun shareTipResult(shareTipResultText :String){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,shareTipResultText )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}