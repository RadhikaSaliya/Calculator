package com.user.canopas.calculator

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView

import java.util.ArrayList

class ViewHistory : AppCompatActivity() {
    internal lateinit var list: ListView

    internal var arrayList: ArrayList<History>? = null
    internal var myDB: DataBase? = null
    internal var adptr: Cust_adptr? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_history)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        list = findViewById(R.id.list) as ListView
        myDB = DataBase(this)
        arrayList = ArrayList()

        arrayList = myDB?.allEquation
        adptr = Cust_adptr(this, arrayList!!)
        list.adapter = adptr


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.getItemId()) {
            android.R.id.home -> {
              onBackPressed()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }


    inner class Cust_adptr(internal var c: Context, internal var arrayList: ArrayList<History>) : BaseAdapter() {

        override fun getCount(): Int {
            return arrayList.size
        }

        override fun getItem(i: Int): Any {
            return arrayList[i]
        }

        override fun getItemId(i: Int): Long {
            return i.toLong()
        }

        override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View? {

            val view: View? = LayoutInflater.from(c).inflate(R.layout.rowdesign, viewGroup, false)
            val txt_equation = view?.findViewById<TextView>(R.id.txt_equation)
            val txt_date = view?.findViewById<TextView>(R.id.txt_date)
            val delete = view?.findViewById<ImageView>(R.id.delete)
            txt_equation?.text = arrayList[i].equation
            txt_date?.text = arrayList[i].date
            delete?.setOnClickListener {
                AlertDialog.Builder(c)
                        .setMessage("Delete history")
                        .setPositiveButton("delete") { dialogInterface, `in` ->
                            myDB?.deleteContact(arrayList[i]._id)
                            arrayList.removeAt(i)
                            adptr?.notifyDataSetChanged()
                        }
                        .setNegativeButton("no") { dialogInterface, i -> dialogInterface.dismiss() }
                        .show()
            }
            return view
        }
    }
}
