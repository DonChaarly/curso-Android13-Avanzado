package com.cursosandroidant.userssp

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cursosandroidant.userssp.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity(), OnClickListener {


    /* ============================== RecyclerView ======================================
        Para utilizar nuestro RecyclerView
    * Creamos una instancia de nuestro adaptador
    * Creamos una instancia de la clase RecyclerView.LayoutManager
    * Al RecyclerView se le deben configurar dos cosas, el adaptador y el tipo de layout a utilizar
    * Se puede utilizar cualquier tipo de layout, LinearLayout, GridLayout, etc
    * */
    private lateinit var userAdapter: UserAdapter
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*Se inicializand las variables Adapter y LinearLayoutManager pasandoles el contexto*/
        userAdapter = UserAdapter(getUsers(), this)
        linearLayoutManager = LinearLayoutManager(this)

        /*Para terminar de configurar nusetro RecyclerView
        * Se le especifica el layoutManager y el adaptador que utilizara
        * */
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = userAdapter
        }

        /* Se puede colocar un gesto de swip hacia un lado para por ejemplo eliminar un elmento de la lista
        *  Esto se hace con la clase ItemTouchHelper y le pasamos un callback
        *  llamando a la clase SimpleCallback
        *  en el primer parametro se coloca el numero de elementos que se desplazara el item, por siquieremos ocupar este swipe para reordenar la lista
        *  en el segundo parametro se colocan las direcciones hacia las que se hara el swipe
        *
        *  Se deben implementar los metodos de la interface onMove y onSwiped, aqui establecemos el codigo que queremos que se ejecute cuando se activen estos metodos
        * */
        val swipeHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                userAdapter.remove(viewHolder.adapterPosition)
            }
        })
        //Finalmente se coloca el siguiente codigo para indicar el recyclerView que se esta utilizando
        swipeHelper.attachToRecyclerView(binding.recyclerView)

        /* ============================= SharedPreferences ==============================
        * SharedPreferences es una herramienta que nos ayuda a persistir datos,
        * A pesar de matar la aplicacin, estos valores podran ser recuperados de las SharedPreferences
        * Las SharedPreferences funcionanan en base a clave y valor
        *
        * Para iniciar se utiliza la funcion getPreferences, y se configura un modo como por ejemplo de tipo PRIVATE
        * */
        val preferences = getPreferences(Context.MODE_PRIVATE)

        /* Para recuperar un valor de las SharedPreferences
        * se utiliza nuestra variable que creamos preferences y utilizamos alguno de sus metodos get
        * Se pasa la clave del valor que estamos buscando,
        * Tambien se tiene que pasar un valor por default en caso de que no se encuentre la clave
        * */
        val isFirstTime = preferences.getBoolean(getString(R.string.sp_first_time), true)
        Log.i("SP", "${getString(R.string.sp_first_time)} = $isFirstTime")

        if (isFirstTime) {
            val dialogView = layoutInflater.inflate(R.layout.dialog_register, null)
            val dialog = MaterialAlertDialogBuilder(this)
                .setTitle(R.string.dialog_title)
                .setView(dialogView)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_confirm) { _, _ -> }
                .create()

            dialog.show()

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val username = dialogView.findViewById<TextInputEditText>(R.id.etUsername)
                    .text.toString()
                if (username.isBlank()){
                    Toast.makeText(this, R.string.register_invalid, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    /*Para insertar un dato para almacenarlo dentro de nuestras SharedPreferences
                    *Utilizamos el metodo edit() seguido de un metodo put
                    * al que se le pasa la clave con la que queremos guardar este valor
                    * y el valor que guardaremos
                    * Finalmente se utliza ya se la funcion commit o apply para aplicar los cambios
                    *   commit: El almacenamiento se ejecuta rapidamente pero si son muchos datos la
                    *           aplicacion puede alentarse, tambien retorna un bool dependiendo de
                    *           si ha habido un error
                    *   apply:
                    * */
                    with(preferences.edit()) {
                        putBoolean(getString(R.string.sp_first_time), false)
                        putString(getString(R.string.sp_username), username)
                            .apply()
                    }
                    Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT)
                        .show()
                    dialog.dismiss()
                }
            }
        } else {
            val username = preferences.getString(
                getString(R.string.sp_username),
                getString(R.string.hint_username)
            )
            Toast.makeText(this, "Bienvenido $username", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getUsers(): MutableList<User>{
        val users = mutableListOf<User>()

        val alain = User(1, "Alain", "Nicolás", "https://frogames.es/wp-content/uploads/2020/09/alain-1.jpg")
        val samanta = User(2, "Samanta", "Meza", "https://upload.wikimedia.org/wikipedia/commons/b/b2/Samanta_villar.jpg")
        val javier = User(3, "Javier", "Gómez", "https://live.staticflickr.com/974/42098804942_b9ce35b1c8_b.jpg")
        val emma = User(4, "Emma", "Cruz", "https://upload.wikimedia.org/wikipedia/commons/d/d9/Emma_Wortelboer_%282018%29.jpg")
        val alex = User(1, "Alex", "Nicolás", "https://upload.wikimedia.org/wikipedia/en/9/96/Howes%2C_Alex.jpg")
        val samara = User(2, "Samara", "Meza", "https://imagenes.razon.com.mx/files/image_940_470/uploads/2020/12/03/5fc9bc274d469.jpeg")
        val joel = User(3, "Joel", "Gómez", "https://www.topdoctors.mx/files/Doctor/profile/5adbfdc9-5768-4d10-84db-64af8e2cd470.jpeg")
        val evelyn = User(4, "Evelyn", "Cruz", "https://upload.wikimedia.org/wikipedia/commons/6/6c/Evelyn_Salgado.jpg")

        users.add(alain)
        users.add(samanta)
        users.add(javier)
        users.add(emma)
        users.add(alex)
        users.add(samara)
        users.add(joel)
        users.add(evelyn)
        users.add(alain)
        users.add(samanta)
        users.add(javier)
        users.add(emma)
        users.add(alex)
        users.add(samara)
        users.add(joel)
        users.add(evelyn)
        users.add(alain)
        users.add(samanta)
        users.add(javier)
        users.add(emma)

        return users
    }

    override fun onClick(user: User, position: Int) {
        Toast.makeText(this, "$position: ${user.getFullName()}" , Toast.LENGTH_SHORT).show()
    }
}