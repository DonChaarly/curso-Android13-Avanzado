package com.cursosant.android.fundamentoskotlin

/****
 * Project: Fundamentos Kotlin
 * From: com.cursosant.android.fundamentoskotlin
 * Created by Alain Nicolás Tello on 09/11/20 at 11:19
 * Course: Android Practical with Kotlin from zero.
 * All rights reserved 2021.
 *
 * All my Courses(Only on Udemy):
 * https://www.udemy.com/user/alain-nicolas-tello/
 ***/

fun main(){
    //print("Hola Kotlin")
    newTopic("Hola Kotlin")

    newTopic("Variables y Constantes")
    //val a = 2
    //val a = "Hola"
    val a = true
    println("a = $a")

    //var b = 2
    var b: Int
    b = 5
    println("b = $b")

    var objNull: Any?
    objNull = null
    objNull = "Hi"

    println(objNull)
}

fun newTopic(topic: String){
    /*println()
    print("==================== ")
    print(topic)
    print(" ====================")
    println()*/

    print("\n==================== $topic ====================\n")
}