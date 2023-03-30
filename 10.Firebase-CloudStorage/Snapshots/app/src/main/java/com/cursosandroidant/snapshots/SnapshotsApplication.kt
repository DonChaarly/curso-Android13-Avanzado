package com.cursosandroidant.snapshots

import android.app.Application
import com.google.firebase.auth.FirebaseUser

/****
 * Project: Snapshots
 * From: com.cursosandroidant.snapshots
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
class SnapshotsApplication : Application() {
    companion object {
        const val PATH_SNAPSHOTS = "snapshots"
        const val PROPERTY_LIKE_LIST = "likeList"

        lateinit var currentUser: FirebaseUser
    }
}