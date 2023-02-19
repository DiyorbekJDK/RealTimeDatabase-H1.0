package org.diyorbek.realtimedatabase_h10.model

data class Problem(
    val uid: String = "",
    val problemName: String = "",
    val isFinished: Boolean = false,
    val problemAddress: String = "",
    val problemDate: String = ""
)
