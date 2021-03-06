package com.user.canopas.calculator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView

import net.objecthunter.exp4j.ExpressionBuilder

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar

class MainActivity : AppCompatActivity(), View.OnClickListener {

    internal var txtans: TextView? = null

    internal var isLastDot: Boolean? = true
    internal var lastNum: Boolean? = false
    internal var onEqual: Boolean? = false
    private val numericButtons = intArrayOf(R.id.btnZero, R.id.btnOne, R.id.btnTwo, R.id.btnThree, R.id.btnFour, R.id.btnFive, R.id.btnSix, R.id.btnSeven, R.id.btnEight, R.id.btnNine, R.id.btnDot, R.id.btnDoubleZero)
    private val operatorButtons = intArrayOf(R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide, R.id.btnAC, R.id.btnEqual, R.id.btnPluseMin, R.id.btnBack)
    internal var Store_ans: String = ""
    internal var decimalFormat: DecimalFormat? = null
    internal var myDB: DataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtans = findViewById(R.id.txtans) as TextView
        txtans?.text = "0"

        BindView()
        myDB = DataBase(this)
        decimalFormat = DecimalFormat("#.##########")

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.history -> startActivity(Intent(this@MainActivity, ViewHistory::class.java))
        }
        return true
    }

    private fun BindView() {
        for (id in numericButtons) {
            findViewById(id).setOnClickListener(this)
        }
        for (id in operatorButtons) {
            findViewById(id).setOnClickListener(this)
        }

    }
//tetsting
    override fun onClick(view: View) {
        val id = view.id
        when (id) {
            R.id.btnOne -> getNumber("1")
            R.id.btnTwo -> getNumber("2")
            R.id.btnThree -> getNumber("3")
            R.id.btnFour -> getNumber("4")
            R.id.btnFive -> getNumber("5")
            R.id.btnSix -> getNumber("6")
            R.id.btnSeven -> getNumber("7")
            R.id.btnEight -> getNumber("8")
            R.id.btnNine -> getNumber("9")
            R.id.btnZero -> getNumber("0")
            R.id.btnBack -> BackButton();
            R.id.btnDot -> DotButton();
            R.id.btnAdd -> getAction("+")
            R.id.btnSubtract -> getAction("-")
            R.id.btnDivide -> getAction("/")
            R.id.btnMultiply -> getAction("*")
            R.id.btnAC -> {

                ClearButton()
            }
            R.id.btnEqual -> Calculate();
            R.id.btnDoubleZero -> if (txtans?.text.toString() != "0")
                getNumber("00")
        }
    }

    private fun Calculate() {
        if (txtans?.text.toString() != "0") {

            val Store_equation = getEqation()
            StoreInDB(Store_equation)

        }
    }

    private fun StoreInDB(store_equation: String) {

        val df = SimpleDateFormat("EEE, d MMM yyyy ,h:mm a")
        val date = df.format(Calendar.getInstance().time)
        myDB?.insert(History(store_equation!!, date))
        isLastDot = true
        onEqual = false
    }

    private fun DotButton() {
        lastNum = false
        if (onEqual!!) {
            txtans?.text = "0"
            onEqual = false
        }
        if (isLastDot!!) {
            if (txtans?.text == ".") {

                txtans?.text = "0." + txtans?.text
            } else
                txtans?.text = txtans?.text.toString() + "."


        }

        isLastDot = false
    }

    private fun ClearButton() {
        txtans?.text = "0"
        lastNum = false
        isLastDot = true
        onEqual = false
    }

    private fun BackButton() {
        val str = txtans?.text.toString()
        if (str != "0") {

            val length = str.length

            if (str[length - 1].toString() == ".") {
                txtans?.text = str.subSequence(0, length - 1)

                isLastDot = true
            }
            txtans?.setText(str.subSequence(0, length - 1))
        }
    }
    //1,2,3.......
// reset branch
    private fun getAction(action: String) {
        isLastDot = true
        if (onEqual!!) {
            txtans?.text = "0"
            onEqual = false
        }
        if (txtans?.text.toString() != "0" && lastNum!!)

            txtans?.text = txtans?.text.toString() + action
        lastNum = false
    }

    private fun getEqation(): String {

        if (lastNum!!) {

            onEqual = true
            val txt = txtans?.text.toString()
            val expression = ExpressionBuilder(txt).build()
            try {
                val result = expression.evaluate()
                txtans?.text = java.lang.Double.toString(result)
                isLastDot = true
                Store_ans = txt + "=" + java.lang.Double.toString(result)
                Log.e("ans", Store_ans)
            } catch (ex: Exception) {

                txtans?.text = "Error"
                Store_ans = "ERROR"
            }

        }
        return Store_ans

    }

    private fun getNumber(no: String) {
        lastNum = true
        if (onEqual!!) {
            txtans?.text = "0"
            onEqual = false
        }
        if (txtans?.text == "0") {
            txtans?.text = no

        } else {
            txtans?.text = txtans?.text.toString() + no
        }

    }
}
