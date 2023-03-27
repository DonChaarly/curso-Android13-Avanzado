package com.cursosant.android.fundamentoskotlin

/****
 * Project: Fundamentos Kotlin
 * From: com.cursosant.android.fundamentoskotlin
 * Created by Alain Nicolás Tello on 10/11/20 at 11:18
 * Course: Android Practical with Kotlin from zero.
 * All rights reserved 2021.
 *
 * All my Courses(Only on Udemy):
 * https://www.udemy.com/user/alain-nicolas-tello/
 ***/
fun main(){
    newTopic("Bucles")
    showPersons("Angel", "Mary", "Fany")
    showPersons("Angel", "Mary", "Sergio", "Alex", "Carla")
}

fun showPersons(p1: String, p2: String, p3: String){
    println(p1)
    println(p2)
    println(p3)
}

fun showPersons(vararg persons: String){
    newTopic("For")
    for (person in persons) println(person)

    newTopic("While")
    var index = 0
    while (index < persons.size){
        if (persons[index] == "Mary") println("Es Mary!")
        println(persons[index])
        index++
    }

    newTopic("When")
    index = (0..persons.size-1).random()
    println(index)
    when (persons[index]){
        "Angel" -> println("Es Angel!")
        "Mary" -> {
            println("Ir a otra pantalla")
            println(2+4)
        }
        else -> println(persons[index])
    }
}