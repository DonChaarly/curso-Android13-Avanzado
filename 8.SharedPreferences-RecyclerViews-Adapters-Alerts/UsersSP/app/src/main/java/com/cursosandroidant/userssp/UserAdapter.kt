package com.cursosandroidant.userssp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.cursosandroidant.userssp.databinding.ItemUserAltBinding

/*===========================  ADAPTERS ===========================================================
* Para especificar datos al recyclerView es necesario crear una clase Adapter
* la cual extiende de RecyclerView.Adapter<> a la cual se le especifica el ViewHolder de la
* clase que estamos creando que tendra dentro de los <>
*
* Al extender de RecyclerView.Adapter es necesario sobreescribir los metodos que nos marca
* */
class UserAdapter(private val users: MutableList<User>, private val listener: OnClickListener) : RecyclerView.Adapter<UserAdapter.ViewHolder>(){

    /*Se crea una variable Context ya que nos ayuda con el inflate de la vista y
      algunos recuesos externos propios de android
      Este Context se inicializara en el onCreateViewHolder
      */
    private lateinit var context: Context

    /* onCreateViewHolder(): Este metodo esta disenado para crear la vista
    *  Este metodo se ejecuta cuando se crea el ViewHolder
    *  Sirve para inicializar la variable context,
    *  Para hacer el inflate de la vista pasando el context e indicando el layout que se utilizara para la lista
    *  Finalmente se regresa el ViewHolder con el view creado
    * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val view = LayoutInflater.from(context).inflate(R.layout.item_user_alt, parent, false)

        return ViewHolder(view)
    }

    /* onBindViewHolder(): Este metodo eta disenado para rellenar la informacion
    * Este metodo funciona similar a lo que seria un forEach
    * Con nuestra variable binding indicamos los valores que tendran los elementos, agrgamos listeners y demas cosas que queramos hacer con los elementos
    * */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users.get(position)
        val humanPosition = position + 1

        with(holder){
            setListener(user, humanPosition)
            binding.tvOrder.text = (humanPosition).toString()
            binding.tvName.text = user.getFullName()
            Glide.with(context)
                .load(user.url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .circleCrop()
                .into(binding.imgPhoto)
        }
    }

    /* getItemCount(): Este metodo se utiliza para indicar el tamano de la lista
    *  se coloca el tamano de la lista, en este caso con el size de la lista users
    * */
    override fun getItemCount(): Int = users.size

    /* Se pueden crear los metodos que queramos para manejar nuestra lista
    * Despues de modificar la lista se debe colocar el metodo notify... para refrescar la vista del adaptador
    */
    fun remove(position: Int) {
        users.removeAt(position)
        notifyItemRemoved(position)
    }

    fun add(user: User){
        users.add(user);
        notifyDataSetChanged()
    }

    /* Es necesario colocar una inner class llamada ViewHolder la cual extiende de RecyclerView.ViewHolder
    *  Utilizando binding vincularemos nuestra vista de itemUser la cual sera el elmento unitario de las listas
    *  Y es este layout a la que realmente le estaremos vinculando los datos
    * */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ItemUserAltBinding.bind(view)

        fun setListener(user: User, position: Int){
            binding.root.setOnClickListener { listener.onClick(user, position) }
        }
    }
}