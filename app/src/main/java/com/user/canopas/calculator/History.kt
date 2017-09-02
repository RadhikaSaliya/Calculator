package com.user.canopas.calculator


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
