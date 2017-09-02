package com.user.canopas.calculator

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import java.util.ArrayList



class DataBase(context: Context) : SQLiteOpenHelper(context, DATABASE, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE ($KEY_ID INTEGER PRIMARY KEY, $KEY_EQUATION TEXT, $KEY_DATE TEXT )")

    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        db.execSQL("DROP TABLE $TABLE IF EXITS")
        onCreate(db)
    }

    fun insert(history: History) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(KEY_EQUATION, history.equation)
        values.put(KEY_DATE, history.date)

        db.insert(TABLE, null, values)
        db.close()
    }

    val allEquation: ArrayList<History>
        get() {
            val arr = ArrayList<History>()

            val db = readableDatabase
            val cursor = db.rawQuery("SELECT * FROM " + TABLE, null)
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    val history = History()
                    history._id = Integer.parseInt(cursor.getString(0))
                    history.equation = cursor.getString(1)
                    history.date = cursor.getString(2)
                    arr.add(history)
                }
            }
            return arr
        }

    fun deleteContact(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE, KEY_ID + " = ?",
                arrayOf(id.toString()))
        db.close()
    }

    companion object {
        private val DATABASE = "Equation.db"
        private val TABLE = "Equation"
        private val KEY_ID = "_id"
        private val KEY_EQUATION = "Equation"
        private val KEY_DATE = "Date"
    }
}
