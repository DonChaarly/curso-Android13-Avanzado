package com.cursosandroidant.snapshots.entities

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

/****
 * Project: Snapshots
 * From: com.cursosandroidant.snapshots.entities
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
@IgnoreExtraProperties
data class Snapshot(@get:Exclude var id: String = "",
                    var ownerUid: String = "",
                    var title: String = "",
                    var photoUrl: String ="",
                    var likeList: Map<String, Boolean> = mutableMapOf())
