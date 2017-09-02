package com.user.canopas.calculator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView

import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar

class MainActivity : AppCompatActivity(), View.OnClickListener {
    //    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btndot, btnsum, btnsub, btndiv, btnmul, btnPlMin,btnans;
    internal var txtans: TextView?=null
    internal var txtscreen: TextView?=null
    internal var lastDot: Boolean? = true
    internal var lastNum: Boolean? = false
    internal var onEqual: Boolean? = false
    private val numericButtons = intArrayOf(R.id.btnZero, R.id.btnOne, R.id.btnTwo, R.id.btnThree, R.id.btnFour, R.id.btnFive, R.id.btnSix, R.id.btnSeven, R.id.btnEight, R.id.btnNine, R.id.btnDot, R.id.btnDoubleZero)
    private val operatorButtons = intArrayOf(R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide, R.id.btnAC, R.id.btnEqual, R.id.btnPluseMin, R.id.btnBack)
    private val valueOne = java.lang.Double.NaN
    private val valueTwo: Double = 0.toDouble()
    internal var Store_ans: String=""
    internal var action: String? = null
    internal var decimalFormat: DecimalFormat?=null
    internal var myDB: DataBase?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtans = findViewById(R.id.txtans) as TextView
        txtans?.text = "0"
        txtscreen = findViewById(R.id.txtScreen) as TextView
        BindView()
        myDB = DataBase(this)
        decimalFormat = DecimalFormat("#.##########")

        test()

    }

    private fun test() {
        var str="Hello"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.history -> startActivity(Intent(this@MainActivity,ViewHistory::class.java))
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
            R.id.btnBack -> {
                val str = txtans?.text.toString()
                if (str != "0") {

                    val length = str.length

                    if (str[length - 1].toString() == ".") {
                        txtans?.text=str.subSequence(0, length - 1)

                        lastDot = true
                    }
                    txtans?.setText(str.subSequence(0, length - 1))
                }
            }
            R.id.btnDoubleZero -> if (txtans?.text.toString() != "0")
                getNumber("00")
            R.id.btnPluseMin -> if (txtans?.text.toString() != "0" && lastNum!!) {

            }
            R.id.btnDot -> {
                if (lastDot!!) {
                    if (txtans?.text == ".")
                        txtans?.text = "0." + txtans?.text
                    else
                        txtans?.text = txtans?.text.toString() + "."

                }
                lastDot = false
            }
            R.id.btnAdd -> getAction("+")
            R.id.btnSubtract -> getAction("-")
            R.id.btnDivide -> getAction("/")
            R.id.btnMultiply -> getAction("*")
            R.id.btnAC -> {
                txtans?.text = "0"
                lastNum = false
                lastDot = true
                onEqual = false
            }
            R.id.btnEqual -> if (txtans?.text.toString() != "0") {
                val Store_equation = onEqual()
                val df = SimpleDateFormat("EEE, d MMM yyyy ,h:mm a")
                val date = df.format(Calendar.getInstance().time)
                myDB?.insert(History(Store_equation, date))
            }
        }
    }

    private fun getAction(action: String) {
        lastDot = true
        if (onEqual!!) {
            txtans?.text = "0"
            onEqual = false
        }
        if (txtans?.text.toString() != "0" && lastNum!!)

            txtans?.text = txtans?.text.toString() + action
        lastNum = false
    }

    private fun onEqual(): String {

        if (lastNum!!) {
            onEqual = true
            val txt = txtans?.text.toString()
            val expression = ExpressionBuilder(txt).build()
            try {
                // Calculate the result and display
                val result = expression.evaluate()
                txtans?.text = java.lang.Double.toString(result)
                lastDot = true // Result contains a dot

                Store_ans = txt + "=" + java.lang.Double.toString(result)
                Log.e("ans", Store_ans)
            } catch (ex: ArithmeticException) {
                // Display an error message
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
