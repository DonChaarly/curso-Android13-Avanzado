package com.cursosandroidant.stores

/****
 * Project: Stores
 * From: com.cursosandroidant.stores
 * Created by Alain Nicolás Tello on 31/01/23 at 14:13
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
    fun hideFab(isVisible: Boolean = false)

    fun addStore(storeEntity: StoreEntity)
    fun updateStore(storeEntity: StoreEntity)
}