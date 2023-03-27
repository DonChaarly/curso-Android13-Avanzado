package com.cursosandroidant.stores

import androidx.room.Entity
import androidx.room.PrimaryKey

/* ====================== ENTITYS CON ROOM ==============================================
* Para marcar una clase como Entity se coloca la etiqueta @Entity(tableName = 'NombreTabla')
* Normalmente se manejar data class para esto
*
* @PrimaryKey se utiliza para configurar una llave primaria
*   (autoGenerate = true) se utiliza para hacer este campo autoincremental
* */
@Entity(tableName = "StoreEntity")
data class StoreEntity(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                       var name: String,
                       var phone: String = "",
                       var website: String = "",
                       var photoUrl: String = "",
                       var isFavorite: Boolean = false){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StoreEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
