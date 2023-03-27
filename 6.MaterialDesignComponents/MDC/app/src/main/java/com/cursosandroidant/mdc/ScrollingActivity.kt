package com.cursosandroidant.mdc

import android.graphics.Color
import android.os.Bundle
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.URLUtil
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.cursosandroidant.mdc.databinding.ActivityScrollingBinding
import com.google.android.material.bottomappbar.BottomAppBar

/****
 * Project: MDC
 * From: com.cursosandroidant.mdc
 * Created by Alain Nicolás Tello on 27/01/23 at 14:45
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
class ScrollingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScrollingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* ================================= FloatingActionButton ===========================
        * setOnClickListener: Se utiliza para establecer alguna funcion al hacer click sobre el boton
        * */

        binding.fab.setOnClickListener {
            /*  ============================= BottomAppBar ==============================
            *  Debido a que el BottomAppBar tiene relacionado un FloatingActionButton, este puede interactuar con el
            *  fabAlignmentMode: Modo de aliniamiento del FloatingActionButton que tiene relacionado
            *  setNavigationOnClickListener: Se dispara cuando se hace click sobre el boton de navegacion del bottomAppBar
            *  FAB_ALIGNMENT_MODE_CENTER: aliniamiento en el centro FloatingActionButton que tiene relacionado
            *  FAB_ALIGNMENT_MODE_END: Aliniamiento al final del FloatingActionButton que tiene relacionado
            * */
            if (binding.bottomAppBar.fabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER){
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
            } else {
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
            }
        }

        binding.bottomAppBar.setNavigationOnClickListener {
            /* ============================= Snackbar =============================
            *  El Snackbar se utiliza para mostrar un mensaje y/o ejecutar alguna accion
            *  El SnackBar solo se puede crear dentro del activity con kotlin, no en el layout
            *
            *  Con 'make' se coloca el contexto en donde se mostrar el snackbar, el mensaje a mostrar y la duracion que tendra el snackbar
            *  Con 'setAnchorView' se utiliza para evitar problemas en componentes que esten en la parte de abajo del activity
            *  Con 'setAction' se muestra un boton en el SnackBar que al apretar ejecuta algun codigo que se especifique
            *  Con 'show()' Finalmente se utiliza para mostrar el snackBar
            * */
            Snackbar.make(binding.root, R.string.message_action_success, Snackbar.LENGTH_LONG)
                .setAnchorView(binding.fab)
                .show()
        }

        /* ========================================== MaterialButton =======================
        * Con los botones lo principal que se hace es colocar ClickListeners para ejecutar acciones
        * */
        binding.content.btnSkip.setOnClickListener { binding.content.cvAd.visibility = View.GONE }

        binding.content.btnBuy.setOnClickListener {
            Snackbar.make(it, R.string.card_buying, Snackbar.LENGTH_LONG)
                .setAnchorView(binding.fab)
                .setAction(R.string.card_to_go) {
                    Toast.makeText(this, R.string.card_historial, Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        loadImage()

        /* ======================================= MaterialCheckBox =============================
        *Con el checkBox tambien lo principal que se hace es colocar un CheckListener y verificar si se marco o desmarco
        * isChecked: Esto nos devuelve true si se ha marcado el checkbox, tambien se puede modificar con esta propiedad
        * */
        binding.content.cbEnablePass.setOnClickListener{
            binding.content.tilPassword.isEnabled = !binding.content.tilPassword.isEnabled
        }

        binding.content.cbEnablePass.isChecked

        /* ======================================== TextInputEditText ===========================
        * Con el edit text podemos recuperar lo que escribe el usuario en el, cuando el usuario pone el foco sobre el, etc
        * onFocusChangeListener: Para establecer que hacer cuando se tiene el foco o ser pierde el foco
        * text: Para recuperar o modificar el texto del editText
        * */
        binding.content.etUrl.onFocusChangeListener = View.OnFocusChangeListener { _, focused ->
            var errorStr: String? = null
            val url = binding.content.etUrl.text.toString()

            if (!focused){
                when {
                    url.isEmpty() -> {
                        //errorStr = getString(R.string.card_required)
                        binding.content.tilUrl.error = getString(R.string.card_required)
                    }
                    URLUtil.isValidUrl(url) -> {
                        loadImage(url)
                    }
                    else -> {
                        errorStr = getString(R.string.card_invalid_url)
                    }
                }
            }

            /* ============================== TextInputLayout ============================
            *  El TextInputLayout es el que contiene a los inputs
            *  error: para mostrar un error debajo del input
            *  isEnabled: Para habilitar o deshabilitar el input
            * */

            binding.content.tilUrl.error = errorStr
        }

        /*================================ MaterialButtonToggleGroup =======================
        * El ButtonToggleGrupo tiene un conjunto de botones de los cuales solo se puede seleccionar uno a la vez
        * Para recuperar el elemento que fue seleccionado se utiliza el argumento checkedId
        * addOnButtonCheckedListener: Este evento se ejecuta cuando el usuario ha oprimido alguno de los botones
        * */
        binding.content.toogleButton.addOnButtonCheckedListener { _, checkedId, _ ->
            binding.content.root.setBackgroundColor(
                when(checkedId){
                    R.id.btnRed -> Color.RED
                    R.id.btnBlue -> Color.BLUE
                    else -> Color.GREEN
                }
            )
        }

        /*================================= SwitchMaterial =====================================
        * El switch nos devuelve un valor booleano el cual sera false o true dependiendo de si el usuario lo marco o desmarco
        * setOnCheckedChangeListener: Este evento se ejecuta cuando se ha hecho un cambio en el switch
        *
        * */
        binding.content.swFab.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked){
                button.text = getString(R.string.card_hide_fab)
                binding.fab.show()
            } else {
                button.text = getString(R.string.card_show_fab)
                binding.fab.hide()
            }
        }

        /* ================================= Slider =========================================
        * addOnChangeListener: Este evento se activa cada que un usuario ha cambiado el valor del slider
        * slider: es el elemento slider
        * value: es el valor final en el que el usuari dejo el slider
        * fromUser: Es un booleano que nos indica si el valor fue modificado por el usuario o no
        * */
        binding.content.sldVol.addOnChangeListener { slider, value, fromUser ->
            binding.content.tvSubtitle.text = "Vol: $value"
        }

        /*================================= Chip ===============================================
        * setOnCheckedChangeListener: Este evento se ejecuta cuando se ha hecho un cambio en el chip
        * chip: El elemento chip al que se esta haciendo refrencia
        * isChecked: Devuelve un bool de si el chip esta seleccionado o no
        * */
        binding.content.cpEmail.setOnCheckedChangeListener { chip, isChecked ->
            if (isChecked){
                Toast.makeText(this, "${chip.text}", Toast.LENGTH_SHORT).show()
            }
        }
        binding.content.cpEmail.setOnCloseIconClickListener {
            binding.content.cpEmail.visibility = View.GONE
        }
    }

    private fun loadImage(url: String = "https://i.ytimg.com/vi/uNhAHzUpsXQ/hqdefault.jpg"){
        /*============================= GLIDE =======================================
        * Glide es muy util a la hora de cargar imagenes, especialmente de internet
        * load: Con load se especifica el url de la imagen a cargar
        * into: Se especifica el elemento en el que se cargara la imagen
        * diskCacheStrategy: Se especifica la estrategia para almacenar la cache
        * centerCrop: Para centrar la imagen
        * */
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(binding.content.imgCover)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}