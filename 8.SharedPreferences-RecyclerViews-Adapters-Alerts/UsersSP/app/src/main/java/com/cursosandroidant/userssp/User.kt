package com.cursosandroidant.userssp

/****
 * Project: Users SP
 * From: com.cursosandroidant.userssp
 * Created by Alain Nicolás Tello on 28/01/23 at 13:11
 * All rights reserved 2023.
 *
 * All my Udemy Courses:
 * https://www.udemy.com/user/alain-nicolas-tello/
 * And Frogames formación:
 * https://cursos.frogamesformacion.com/pages/instructor-alain-nicolas
 *
 * Coupons on my Website:
 * www.alainnicolastello.com
 ***/
data class User (val id: Long, var name: String, var lastName: String, var url: String) {

    fun getFullName(): String = "$name $lastName"
}