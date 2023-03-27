
# RecyclerView

El recycler view es como un map en react o un for para iterar elementos en el layout\
Este componente optimiza el renderizado de una gran cantidad de componentes

Primero hay que crear un layout tipo fragment el cual seran los items que se renderizaran en el RecyclerView
```xml
<?xml version="1.0" encoding="utf-8"?>
<com.google.android.material.card.MaterialCardView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    ...>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="@dimen/card_height">

        <TextView
            android:id="@+id/tvOrder"
            .../>
        
        ...

    </androidx.constraintlayout.widget.ConstraintLayout>

</com.google.android.material.card.MaterialCardView>
``` 

Despues hay que crear una layout en el que se utilizara el componente RecyclerView, al que se le especificara en la propiedad\
**tools:listitem** la plantilla que utilizara para los item del listado
```xml
<androidx.constraintlayout.widget.ConstraintLayout 
    ...
    tools:context=".MainActivity">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        tools:listitem="@layout/item_user"
        ...
    />

</androidx.constraintlayout.widget.ConstraintLayout>

```

Tambien es necesario crear una clase adaptador para especificar los datos que tendran los items

## Adapters

Para especificar datos al recyclerView es necesario crear una clase Adapter la cual extiende de RecyclerView.ViewHolder

```kotlin
/* Se crea una clase Adapter y se extiende de RecyclerView.Adapter<> especificando el ViewHolder a utilizar
* */
class UserAdapter(private val users: MutableList<User>, private val listener: OnClickListener) : RecyclerView.Adapter<UserAdapter.ViewHolder>(){

    /*Se crea una variable Context ya que nos ayuda con el inflate de la vista y
      algunos recuesos externos propios de android*/
    private lateinit var context: Context

    /* onCreateViewHolder(): Este metodo esta disenado para crear la vista */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val view = LayoutInflater.from(context).inflate(R.layout.item_user_alt, parent, false)

        return ViewHolder(view)
    }

    /* onBindViewHolder(): Este metodo eta disenado para rellenar la informacion */
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

    /* getItemCount(): Este metodo se utiliza para indicar el tamano de la lista */
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

```

## Configuracion del RecyclerView en la MainActivity

Finalmente se hace la configuracion del RecyclerView en la MainActivity
Al RecyclerView se le deben configurar dos cosas, el adaptador y el tipo de layout a utilizar
    Se puede utilizar cualquier tipo de layout, LinearLayout, GridLayout, etc

```kotlin
class MainActivity : AppCompatActivity(), OnClickListener {


    /* Creamos una instancia de nuestro adaptador
    *  Creamos una instancia de la clase RecyclerView.LayoutManager
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

        ....

    }

    ...

}
```


# SharedPreferences

SharedPreferences es una herramienta que nos ayuda a persistir datos, en este podemos guardar preferencias o valores, pero nunca datos sencibles
como contrasenas o cuentas de banco
A pesar de matar la aplicacin, estos valores podran ser recuperados de las SharedPreferences

Las SharedPreferences funcionanan en base a clave y valor

Para iniciar se utiliza la funcion getPreferences, y se configura un modo como por ejemplo de tipo PRIVATE
```kotlin
val preferences = getPreferences(Context.MODE_PRIVATE)
```

Para insertar un dato para almacenarlo dentro de nuestras SharedPreferences\
Utilizamos el metodo edit() seguido de un metodo put\
al que se le pasa la clave con la que queremos guardar este valor y el valor que guardaremos\

```kotlin
preferences.edit().putBoolean('isFirsttime', false).apply()
```

Para recuperar un valor de las SharedPreferences
se utiliza nuestra variable que creamos preferences y utilizamos alguno de sus metodos get\
Se pasa la clave del valor que estamos buscando,\
Tambien se tiene que pasar un valor por default en caso de que no se encuentre la clave\

```kotlin
val isFirstTime = preferences.getBoolean('isFirsttime', true)
```