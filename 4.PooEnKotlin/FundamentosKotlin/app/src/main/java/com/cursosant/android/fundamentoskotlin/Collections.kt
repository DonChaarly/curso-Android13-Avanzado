package com.cursosant.android.fundamentoskotlin

import com.cursosant.android.fundamentoskotlin.classes.Group
import com.cursosant.android.fundamentoskotlin.classes.User

/****
 * Project: Fundamentos Kotlin
 * From: com.cursosant.android.fundamentoskotlin
 * Created by Alain Nicolás Tello on 11/11/20 at 10:05
 * Course: Android Practical with Kotlin from zero.
 * All rights reserved 2021.
 *
 * All my Courses(Only on Udemy):
 * https://www.udemy.com/user/alain-nicolas-tello/
 ***/
fun main(){
    newTopic("Colecciones")
    newTopic("Solo lectura")
    val frutaList = listOf("Manzana", "Banana", "Uva", "Lima")
    println(frutaList.get((0..frutaList.size-1).random()))
    println("Index de Uva es ${frutaList.indexOf("Uva")}")

    newTopic("Mutable List")

    val myUser = User(0, "Alain", "Nicolás", Group.FAMILY.ordinal)
    val bro = myUser.copy(1, "Ivan")
    val friend = bro.copy(id = 2, group = Group.FRIEND.ordinal)

    val usersList = mutableListOf(myUser, bro)
    println(usersList)
    usersList.add(friend)
    println(usersList)
    //usersList.removeAt(1)
    usersList.remove(bro)
    println(usersList)

    val usersSelectedList = mutableListOf<User>()
    println(usersSelectedList)
    //usersSelectedList.addAll(usersList)
    usersSelectedList.add(myUser)
    println(usersSelectedList)
    usersSelectedList.set(0, bro)
    println(usersSelectedList)
    usersSelectedList.add(myUser)
    usersSelectedList.add(myUser)
    println(usersSelectedList)

    newTopic("Map")
    val usersMap = mutableMapOf<Int, User>()
    println(usersMap)
    usersMap.put(myUser.id.toInt(), myUser)
    usersMap.put(myUser.id.toInt(), myUser)
    println(usersMap)
    usersMap.put(friend.id.toInt(), friend)
    println(usersMap)
    usersMap.remove(2)
    println(usersMap)
    println(usersMap.isEmpty())
    println(usersMap.containsKey(0))
    usersMap.put(bro.id.toInt(), bro)
    usersMap.put(friend.id.toInt(), friend)
    println(usersMap)
    println(usersMap.keys)
    println(usersMap.values)
}