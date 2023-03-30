package com.cursosandroidant.snapshots.utils

import com.google.android.material.snackbar.Snackbar

/****
 * Project: Snapshots
 * From: com.cursosandroidant.snapshots.utils
 * Created by Alain Nicolás Tello on 01/02/23 at 11:10
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
interface MainAux {
    fun showMessage(resId: Int, duration: Int = Snackbar.LENGTH_SHORT)
}