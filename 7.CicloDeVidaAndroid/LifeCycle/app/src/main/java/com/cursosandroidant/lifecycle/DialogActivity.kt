package com.cursosandroidant.lifecycle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/****
 * Project: Life Cycle
 * From: com.cursosandroidant.lifecycle
 * Created by Alain Nicolás Tello on 28/01/23 at 12:48
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
class DialogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
    }
}