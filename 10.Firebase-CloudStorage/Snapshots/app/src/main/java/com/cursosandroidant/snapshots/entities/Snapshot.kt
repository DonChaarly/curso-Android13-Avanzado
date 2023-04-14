package com.cursosandroidant.snapshots.entities

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

/* ========================= DataClass para Firebase =====================================
* Se debe comenzar por crear las clases que se estaran utilizando,
* lo que seran basicamente nuestras tablas en Firebase
* Se debe colocar la etiqueta @IgnoreExtraPropoerties de acuerdo a la documentacion de Firebase
* */
@IgnoreExtraProperties
data class Snapshot(@get:Exclude var id: String = "",
                    var ownerUid: String = "",
                    var title: String = "",
                    var photoUrl: String ="",
                    var likeList: Map<String, Boolean> = mutableMapOf())
