package com.user.canopas.calculator

/**
 * Created by canopas on 29/08/17.
 */

class History {
    var _id: Int = 0
    var equation: String?=null
    var date: String?=null


    constructor(equation: String, date: String) {
        this.equation = equation
        this.date = date

    }

    constructor() {

    }


}
