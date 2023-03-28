# Layout

## Etiquetas con tools

Cuando se tiene una propiedad de un elemento que solo queremos que sea visible en el layout mientras desarrollamos para saber como quedaria la aplicacion\
En vez de colocar la propiedad comenzando por android, se coloca la palabra tools
```xml
<TextView
    ...
    tools:text="1"
/>
```

## Propiedades de las etiquetas

### tools:context
Para establecer el activity al que se relacionara el layout

### layout_margin...
Para Establecer un margin ya sea en todos lados o en un lado en especifico

### android:visibility
Para definir la visibilidad del elemento
Valores aceptables:
* gone
* invisible
* visible

### android:clickable
Para definir si se puede hacer click sobre el elemento

### android:focusable
Para definir si el elemento puede tener el foco

### android:padding
Para definir un padding ya sea en todos lados o en un lado en especifico

### android:background
para definir un color de fondo o una imagen

### android:layout_width
Para establecer el ancho del elemento
Valores aceptables:
* match_parent
* 30dp
* 20sp

### android:layout_height
Para establcer la altura del elemento
Valores aceptables:
* wrap_content
* 30dp
* 20sp

### android:layout_gravity
Para establecer el posicionamiento del elemento con forme al padre

### style
Para establecer un estilo predefinido

### android:src
Para establecer la imagen que tendra el icono o imagen

### app:layout_anchor
Para hacer referencia a otro elemento sobre el cual se posicionara


# Kotlin

## Binding

El Bindign es una forma de recuperar los elementos que tenemos en el layout en nuestras activitys con Kotlin

Para empezar a configurarlo es necesario habilitarlo en el build.gradle(Module:app)
```java

android {
    ...
    buildFeatures {
        viewBinding true
    }
}
```
En el activity a utilizar se realiza la siguiente configuracion
```kotlin
class ScrollingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScrollingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}

```

Para mandar llamar los elementos del layout se hace de la siguiente forma:
```kotlin
binding.idElementoEnLayout
```

Para hacer referencia al layout se utiliza:
```kotlin
binding.root
```

## Propiedades y metodos de los elementos del layout

### .setOnClickListener  
Para establecer un codigo al hacer click sobre el elemento

### .onFocusChangeListener

Este metodo se utiliza en los elementos que pueden tener el foco y se activa cuando se tiene o se pierde el foco\
ejemplo:
```kotlin
binding.content.etUrl.onFocusChangeListener = View.OnFocusChangeListener { view, focused ->

}
```

### .visibility 
Para que se vaya vaya de la pantalla
.visibility = View.GONE 

### .isEnabled
Para los input y diferentes elementos

### .error
Para establecer un mensaje de error a algun input

### .setBackgroundColor
Para establecer el color de fondo del elemento


## Ocultar keyboard

```kotlin
private fun hideKeyboard(){
    val imm = mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(requireView().windowToken, 0)
}
```
