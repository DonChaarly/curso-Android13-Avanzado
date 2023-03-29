package com.cursosandroidant.stores

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.cursosandroidant.stores.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.concurrent.LinkedBlockingQueue

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
class MainActivity : AppCompatActivity(), OnClickListener, MainAux {

    private lateinit var mBinding: ActivityMainBinding

    private lateinit var mAdapter: StoreAdapter
    private lateinit var mGridLayout: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.fab.setOnClickListener { launchEditFragment() }

        setupRecyclerView()
    }

    /* =============== LANZAMIENTO DE FRAGMENT =================================
    * Se puede crear un metodo que se encargue de lanzar nuestro fragment,\
    * esta funcion la podremos mandar a llamar desde donde lo queramos
    * */
    private fun launchEditFragment(args: Bundle? = null) {
        /*Se crea una instancia de nuestro fragmento*/
        val fragment = EditStoreFragment()
        /*Se agregan los argumentos con fragment.argumentos y se iguala a los argumentos que se tengan si es que se tienen*/
        if (args != null) fragment.arguments = args

        /*FragmentManger: Es el gestor de android para controlar los fragmentos*/
        val fragmentManager = supportFragmentManager
        /*FragmentTransaction: Es el que va a decidir como se va a ejecutar*/
        val fragmentTransaction = fragmentManager.beginTransaction()

        /*Se especifica el contenedor en donde se cargara el fragmento,
        * Esto enviara al frente de nuestro layout el fragmento, hay que colocarle un color
        * de backgroun si queremos que el fondo no se vea transparente
        * */
        fragmentTransaction.add(R.id.containerMain, fragment)
        /*La funcion addToBackStack se utiliza para que cuando se haga un retroceso o back, no se
        cierre la aplicacion o se vaya a la pagina anterior sino que se cierre el fragment*/
        fragmentTransaction.addToBackStack(null)
        /*Se utiliza la funcion commit() para que se apliquen los cambios*/
        fragmentTransaction.commit()

        hideFab()
    }

    private fun setupRecyclerView() {
        mAdapter = StoreAdapter(mutableListOf(), this)
        mGridLayout = GridLayoutManager(this, resources.getInteger(R.integer.main_columns))
        getStores()

        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mGridLayout
            adapter = mAdapter
        }
    }

    /* =================================== Acceder a los metodos dao desde las Activities ============================
    * Para recuperar datos de la base de datos a parte de crear un hilo secundario, hay que crear una cola para esperar la respuesta
    *  Se crea un queue mandando llamar la clase LinkedBlockingQueue y se le pasa el tipo de valor que se recibira
    *  despues se iguala o guarda el valor de nuestro procedimiento asincrono en la cola
    *  despues con el metodo queue.take() se obtiene el valor despues de esperar a la respuesta
    * */
    private fun getStores() {
        val queue = LinkedBlockingQueue<MutableList<StoreEntity>>()
        Thread{
            val stores = StoreApplication.database.storeDao().getAllStores()
            queue.add(stores)
        }.start()

        mAdapter.setStores(queue.take())
    }

    /*====================== PASAR ARGUMENTOS A UN FRAMENT ==============================
    * Para pasar argumentos a un fragment hay que crear una variable Bundle()
    * A esta, como en las SharedPreferences hay que agregar los argumentos con un esquema de llave valor
    * Se utilizan los metodos para colocar el tipo de dato que se agrega
    * Finalmente se lanza el fragment pero mandandole los argumentos
    * */
    override fun onClick(storeId: Long) {
        val args = Bundle()
        args.putLong(getString(R.string.arg_id), storeId)

        launchEditFragment(args)
    }

    override fun onFavoriteStore(storeEntity: StoreEntity) {
        storeEntity.isFavorite = !storeEntity.isFavorite
        val queue = LinkedBlockingQueue<StoreEntity>()
        Thread{
            StoreApplication.database.storeDao().updateStore(storeEntity)
            queue.add(storeEntity)
        }.start()
        updateStore(queue.take())
    }

    override fun onDeleteStore(storeEntity: StoreEntity) {
        val items = resources.getStringArray(R.array.array_options_item)

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_options_title)
            .setItems(items) { _, i ->
                when(i){
                    0 -> confirmDelete(storeEntity)

                    1 -> dial(storeEntity.phone)

                    2 -> goToWebsite(storeEntity.website)
                }
            }
            .show()
    }

    private fun confirmDelete(storeEntity: StoreEntity){
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_delete_title)
            .setPositiveButton(R.string.dialog_delete_confirm) { _, _ ->
                val queue = LinkedBlockingQueue<StoreEntity>()
                Thread {
                    StoreApplication.database.storeDao().deleteStore(storeEntity)
                    queue.add(storeEntity)
                }.start()
                mAdapter.delete(queue.take())
            }
            .setNegativeButton(R.string.dialog_delete_cancel, null)
            .show()
    }

    /*============================ INTENTS ========================================
    * Los intents nos sirven para mandar llamar activitys, sin embargo no solo funciona con activities que esten en nuestra aplicacion,\
    * sino que tambien con aquellas de otras aplicaciones, como por ejemplo la aplicacion de contactos o llamadas, etc.
    * */
    private fun dial(phone: String){
        val callIntent = Intent().apply {
            action = Intent.ACTION_DIAL
            data = Uri.parse("tel:$phone")
        }

        startIntent(callIntent)
    }

    /*Se utiliza una intent.resolveActivity(packageManager) para verificar que la aplicacion que se quiere abrir es compatible*/
    private fun startIntent(intent: Intent){
        if (intent.resolveActivity(packageManager) != null)
            startActivity(intent)
        else
            Toast.makeText(this, R.string.main_error_no_resolve, Toast.LENGTH_LONG).show()
    }

    private fun goToWebsite(website: String){
        if (website.isEmpty()){
            Toast.makeText(this, R.string.main_error_no_website, Toast.LENGTH_LONG).show()
        } else {
            val websiteIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(website)
            }

            startIntent(websiteIntent)
        }
    }

    /*
    * MainAux
    * */
    override fun hideFab(isVisible: Boolean) {
        if (isVisible) mBinding.fab.show() else mBinding.fab.hide()
    }

    override fun addStore(storeEntity: StoreEntity) {
        mAdapter.add(storeEntity)
    }

    override fun updateStore(storeEntity: StoreEntity) {
        mAdapter.update(storeEntity)
    }
}