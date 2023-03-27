package com.cursosandroidant.stores

import androidx.room.*

/* ====================== CLASE DAO =======================================================
* Las clases Dao se utilizan para obtener consultar informacion a la base de datos,\
Aqui se encontraran todas las consultas sql

Para marcar una interface o clase como Dao se coloca la etiqueta @Dao

Para los metodos que hagan consultas, inserciones, etc se colcoan diferentes etiquetas:
* @Query(): Para indicar la consulta sql que se va a realizar
  * Para indicar un parametro se coloca : seguido del nombre del parametro el cual debe coincidir con el que esta recibiendo la funcion
* @Insert: Para las funciones que insertan informacion a la tabla
* @Update: Para las funciones que actualizan informacion a la tabla
* @Delete: Para las funciones que eliminan informacion de la tabla
* */
@Dao
interface StoreDao {
    @Query("SELECT * FROM StoreEntity")
    fun getAllStores() : MutableList<StoreEntity>

    @Query("SELECT * FROM StoreEntity where id = :id")
    fun getStoreById(id: Long): StoreEntity

    @Insert
    fun addStore(storeEntity: StoreEntity): Long

    @Update
    fun updateStore(storeEntity: StoreEntity)

    @Delete
    fun deleteStore(storeEntity: StoreEntity)
}