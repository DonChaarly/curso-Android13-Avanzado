package com.cursosant.android.fundamentoskotlin.classes

import com.cursosant.android.fundamentoskotlin.newTopic

/****
 * Project: Fundamentos Kotlin
 * From: com.cursosant.android.fundamentoskotlin.classes
 * Created by Alain Nicolás Tello on 10/11/20 at 12:23
 * Course: Android Practical with Kotlin from zero.
 * All rights reserved 2021.
 *
 * All my Courses(Only on Udemy):
 * https://www.udemy.com/user/alain-nicolas-tello/
 ***/
fun main(){
    newTopic("Clases")
    val phone: Phone = Phone(1234567)
    phone.call()
    phone.showNumber()
    //println(phone.number)

    newTopic("Herencia")
    val smartphone = Smartphone(3456723, true)
    smartphone.call()

    newTopic("Sobreescritura")
    smartphone.showNumber()
    println("Privado? ${smartphone.isPrivate}")

    newTopic("Data Classes")
    val myUser = User(0, "Alain", "Nicolás", Group.FAMILY.ordinal)
    val bro = myUser.copy(1, "Ivan")
    val friend = bro.copy(id = 2, group = Group.FRIEND.ordinal)

    println(myUser.component3())
    println(myUser)
    println(bro)
    println(friend)

    newTopic("Funciones de alcance")
    with(smartphone){
        println("Privado? $isPrivate")
        call()
    }

    /*friend.group = Group.WORK.ordinal
    friend.name = "Juan"
    friend.lastName = "Tellez"*/
    friend.apply {
        group = Group.WORK.ordinal
        name = "Juan"
        lastName = "Tellez"
    }
    println(friend)
}