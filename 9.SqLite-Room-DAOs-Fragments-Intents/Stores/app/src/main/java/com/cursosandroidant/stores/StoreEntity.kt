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

    /*==================== METODOS EQUALS Y HASHCODE ======================
    * La libreria de Room nos crea automaticamente nuestros metodo equals y hashCode que se utilizan para verificar que dos objetos son iguales\
    * Sin embargo estos metodos pueden llegar a fallar o no funcionar como nosotros queremos\
    * Para esto se puede sobreescribir estos metodos y establecer una nueva forma de verificar dos objetos iguales\
    * */
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
