package com.cursosant.android.fundamentoskotlin.classes

/****
 * Project: Fundamentos Kotlin
 * From: com.cursosant.android.fundamentoskotlin.classes
 * Created by Alain Nicolás Tello on 10/11/20 at 12:21
 * Course: Android Practical with Kotlin from zero.
 * All rights reserved 2021.
 *
 * All my Courses(Only on Udemy):
 * https://www.udemy.com/user/alain-nicolas-tello/
 ***/
open class Phone (protected val number: Int){

    fun call(){
        println("Llamando...")
    }

    open fun showNumber(){
        println("Mi número es: $number")
    }
}