# SqLite con Room en android

Para poder implementar una base de datos relacionar como SqLite en android\
Utilizaremos Room la cual es una libreria propuesta por Google

La documentacion oficial de android para implementar Room se encuentra aqui: https://developer.android.com/training/data-storage/room?hl=es-419

## Implementacion de Room

En build.gradle(Module:app)
```java
plugins {
    ...
    id 'kotlin-kapt'
}

...

def room_version = "2.5.0"

dependencies {
    ...
    implementation "androidx.room:room-runtime:$room_version"
    kapt "androidx.room:room-compiler:$room_version"
}
```

## Clases Entity con Room

Para marcar una clase como Entity se coloca la etiqueta @Entity(tableName = 'NombreTabla')\
Normalmente se manejar data class para esto\

@PrimaryKey se utiliza para configurar una llave primaria
* (autoGenerate = true) se utiliza para hacer este campo autoincremental
```kotlin
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
```

## Interfaces Dao

Las clases Dao se utilizan para obtener consultar informacion a la base de datos,\
Aqui se encontraran todas las consultas sql

Para marcar una interface o clase como Dao se coloca la etiqueta @Dao

Para los metodos que hagan consultas, inserciones, etc se colcoan diferentes etiquetas:
* @Query(): Para indicar la consulta sql que se va a realizar
  * Para indicar un parametro se coloca : seguido del nombre del parametro el cual debe coincidir con el que esta recibiendo la funcion
* @Insert: Para las funciones que insertan informacion a la tabla
* @Update: Para las funciones que actualizan informacion a la tabla
* @Delete: Para las funciones que eliminan informacion de la tabla 

```kotlin
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
```

## Clases Database

Las clases Database se utilizan para finalmente acceder a la base de datos
Se extiende de la clase RoomDatabase
Esta clase tiene que ser abstracta
Se coloca la etiqueta @Database y se pasa un array de las clase entidad que tenemos y la version, esta ultima la definimos nosotros
Finalmente se configuran las clases Dao tambien como funciones abastractas

```kotlin
@Database(entities = arrayOf(StoreEntity::class), version = 2)
abstract class StoreDatabase : RoomDatabase() {
    abstract fun storeDao(): StoreDao
}
```

## Clases Application (opcional)

Estas clases hacen mas accesible la forma de llamar a las clases Dao
Ya no se tendran que hacer instancias de los Dao en cada activity que se necesiten, esto se configura directamente en esta clase,
Se exitende de la clase Application
Se inicializa una variable database dentro de un companion object para que sea como una variable static
La variable database se inicializa con ayuda del metodo Room.databaseBuilder, pasandole la clase Database y el nombre de la clase;

```kotlin

class StoreApplication : Application() {
    companion object{
        lateinit var database: StoreDatabase
    }

    override fun onCreate() {
        super.onCreate()

        val MIGRATION_1_2 = object : Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE StoreEntity ADD COLUMN photoUrl TEXT NOT NULL DEFAULT ''")
            }
        }

        database = Room.databaseBuilder(this,
            StoreDatabase::class.java,
            "StoreDatabase")
            .addMigrations(MIGRATION_1_2)
            .build()
    }
}

```

Una vez que se tiene la clase Application se vincula la clase con la aplicacion agregando la propiedad name al manifest y colocando el nombre de nuestra clase\
con esto se podra acceder a la clase Application desde cualquier punto del proyecto

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET"/>

    <application
        android:name=".StoreApplication"
        ...
        >
        <activity>
            ...
        </activity>
    </application>


</manifest>


```

## Acceder a los metodos dao desde las Activities

Para acceder a los metodos del Dao se tiene o bien que crear una instancia de la clase Database que creamos o \
si implementamos la parte anterior simplemente mandamos llamar nuestra clase Application sin necesidad de instanciarla

Es necesario realizar esta accion dentro de un hilo secundario, ya que estas acciones pueden congelar nuestra aplicacion si lo manejamos dentro del hilo principal\
Para esto se coloca un bloque Thread{...}.start()

```kotlin
Thread {
    StoreApplication.database.storeDao().addStore(mStoreEntity!!)
    queue.add(mStoreEntity)
}.start()
```

Para recuperar datos de la base de datos a parte de crear un hilo secundario, hay que crear una cola para esperar la respuesta
Se crea un queue mandando llamar la clase LinkedBlockingQueue y se le pasa el tipo de valor que se recibira
despues se iguala o guarda el valor de nuestro procedimiento asincrono en la cola
despues con el metodo queue.take() se obtiene el valor despues de esperar a la respuesta

```kotlin
val queue = LinkedBlockingQueue<MutableList<StoreEntity>>()
Thread{
    val stores = StoreApplication.database.storeDao().getAllStores()
    queue.add(stores)
}.start()

mAdapter.setStores(queue.take())
```

# Fragments

## Ciclo de vida de un Fragment

El ciclo de vida de un Fragment es muy similar al de una activity

![Ciclo de vida de un fragment](https://developer.android.com/static/images/fragment_lifecycle.png?hl=es-419)

### OnAttach()
Se ejecuta cuando se añade a nuestra activity

### OnCreate()
Se ejecuta cuandos se comienza a crear el fragment

### OnCreateView()
Es donde se puede vincular la vista

### OnActivityCreated()
Es donde ya esta lista nuestra activity para ser ocupada

### OnStart()
Cuando comienza la activity

### OnResume()
Cuando se reanuda la activity

### Fragment is Active
En este punto el fragmento esta cargado, listo y se visualiza en la pantalla

### OnPause()
Cuando se hace pausa en la aplicacion como cuando se va al menu del telefono

### OnStop()
Cuando se hace una parada de la aplicacion ya sea por mucho tiempo con la aplicacion cerrada

### OnDestroyView()
Se puede dar el caso en que se conserve el fragmento pero no la vista,
Este puede que se recupere y regrese a ONCreateView

### OnDestroy
Cuando se elimina el Fragment

### OnDettach()
Cuando se separa de nuestra activity

## Implementacion de un Fragment


Para crear un frament se crea como sigue\
![](/9.SqLite-Room-DAOs-Fragments-Intents/Imagenes/Captura%20de%20pantalla%20de%202023-03-24%2016-14-09.png)

Se creara tanto el layout como el activity con codigo inicial\
Se tendra un codigo plantilla inicial en el activity pero no sirve de mucho, se puede comenzar con el codigo de esta forma\
Agregada de una vez la configuracion de Binding
```kotlin
class BlankFragment : Fragment() {

    private lateinit var mBinding: FragmentBlankBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mBinding = FragmentBlankBinding.inflate(inflater, container, false)
        return mBinding.root
        
    }


}
```
El layout puede ser como se quiera con el layout que queramos,
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.core.widget.NestedScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".BlankFragment">

    <!-- TODO: Update blank fragment layout -->
    <TextView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:text="@string/hello_blank_fragment" />

</androidx.core.widget.NestedScrollView>
```

## Funciones utilies de los fragments

En la clase del Fragment se pueden utilizar los siguientes metodos

### Activity en donde se aloja el fragment
Se puede conseguir la activity en la que se esta alojando este fragmento de la siguiente forma
Esto se puede hacer a partir de onViewCreated()
```kotlin
mActivity = activity as? MainActivity
```

### ActionBar en Fragment
Para mostrar una flecha de retroceso en el ActionBar
```kotlin
mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
```
Para Agregar un titulo al ActionBar
```kotlin
mActivity?.supportActionBar?.title = if (mIsEditMode) getString(R.string.edit_store_title_edit)
```
Para quitar el boton de retroceso
```kotlin
mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
```

### Menus en Framents
Para crear un menu se hace lo siguiente\
![](/9.SqLite-Room-DAOs-Fragments-Intents/Imagenes/CreacionMenu.png)
![](/9.SqLite-Room-DAOs-Fragments-Intents/Imagenes/NombramientoMenu.png)

Este sera el esqueleto basico de un menu:\
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <item
        android:id="@+id/action_save"
        android:title="@string/menu_save"
        android:icon="@drawable/ic_check"
        app:showAsAction="ifRoom"/>

</menu>
```

Se coloca el siguiente metodo dentro de onViewCreated o algun metodo:\
```kotlin
setHasOptionsMenu(true)
```

Se sobreescriben los siguientes metodos:
  * onCreateOptionsMenu(): Se utiliza para establecer el menu que se utilizara
  * onOptionsItemSelected(): Se utiliza para establecer los metodos que se ejecutaran al oprimir los botones del menu

```kotlin

override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    /* Establecemos el layout de menu que utilizaremos*/
    inflater.inflate(R.menu.menu_save, menu)
    super.onCreateOptionsMenu(menu, inflater)
}


override fun onOptionsItemSelected(item: MenuItem): Boolean {
    /*Con un when se puede ejecutar una accion dependiendo del id del item o boton oprimido*/
    return when(item.itemId){
        android.R.id.home -> {
            requireActivity().onBackPressedDispatcher.onBackPressed()
            true
        }
        R.id.action_save -> {
            ...
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
```

### Metodo onDestroy

Se puede utilizar el metodo onDestroy para quitar configuraciones del ActionBar o cosas que ocultamos al mostrar el fragment
```kotlin
override fun onDestroy() {
    mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    mActivity?.supportActionBar?.title = getString(R.string.app_name)
    mActivity?.hideFab(true)

    setHasOptionsMenu(false)
    super.onDestroy()
}
```

## Lanzar un fragment desde la mainActivity

Se puede crear un metodo que se encargue de lanzar nuestro fragment,\
esta funcion la podremos mandar a llamar desde donde lo queramos

Lo primero Se crea una instancia de nuestro fragmento\
Se tiene que implementar dos cosas:
* FragmentManger: Es el gestor de android para controlar los fragmentos
* FragmentTransaction: Es el que va a decidir como se va a ejecutar



```kotlin
private fun launchEditFragment(args: Bundle? = null) {
    val fragment = EditStoreFragment() //Instancia de nuestro fragment
    if (args != null) fragment.arguments = args

    val fragmentManager = supportFragmentManager //Instancia del fragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction() //Instancia del framgentTransaction

    fragmentTransaction.add(R.id.containerMain, fragment)
    fragmentTransaction.addToBackStack(null)
    fragmentTransaction.commit()

    hideFab()
}
```