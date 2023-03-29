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

### requireView()
Se utiliza para recuperar la view y verificar que no sea null

## Ocultar keyboard

```kotlin
private fun hideKeyboard(){
    val imm = mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(requireView().windowToken, 0)
}
```
## Validar inputs de formularios
La funcion recibe uno o muchos (El numero de argumentos que se quiera mandar, esto por el vararg) TextInputLayout,
Los cuales itera y verifica que no sean vacios, pero se puede modificar este codigo para que verifique mas cosas,
```kotlin
private fun validateFields(vararg textFields: TextInputLayout): Boolean{
    var isValid = true

    for (textField in textFields){
        if (textField.editText?.text.toString().trim().isEmpty()){
            textField.error = getString(R.string.helper_required)
            isValid = false
        } else textField.error = null
    }

    if (!isValid) Snackbar.make(mBinding.root,
        R.string.edit_store_message_valid,
        Snackbar.LENGTH_SHORT).show()

    return isValid
}
```

## Obtener recursos como String, colores, Arrays, etc.

con el siguiete codigo se recuperar de la carpeta res/values, lo que se especifique\
Se pasa el identificador con el que se tiene guardado:
```kotlin
resources.getStringArray(R.array.array_options_item)
```
Un ejemplo del un recurso de tipo array es el siguiente:
```xml
<resources>
    <string-array name="array_options_item">
        <item>@string/dialog_delete_confirm</item>
        <item>@string/options_item_call</item>
        <item>@string/options_item_website</item>
    </string-array>
</resources>
```

# Creacion de archivos

## Crear archivos de String de diferentes idiomas

Para los string se tiene un archivo string.xml que es el que se utiliza por default\
Pero se pueden crear mas archivos con diferentes idiomas.\
Para un nuevo archivo String se hace lo siguiente:
1. Se crea un nuevo Android Resource File:\
   ![](/9.SqLite-Room-DAOs-Fragments-Intents/Imagenes/NuevoAndroidResourceFile.png) 
2. Se selecciona Locale para archivos String en diferentes idiomas\
   ![](/9.SqLite-Room-DAOs-Fragments-Intents/Imagenes/SeleccionLocale.png) 
3. Finalmente se selecciona el idioma que tendra el archivo, opcionalmente se puede selecionar una region\
   ![](/9.SqLite-Room-DAOs-Fragments-Intents/Imagenes/EscogerIdioma.png) 


## Crear layout de Menus

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

## Archivo de Themes y colors

En el archivo de colors se deben especificar todos lo colores que se quieran utilizar en nuestra aplicacion\
Se ve algo como esto:
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="purple_200">#FFBB86FC</color>
    <color name="purple_500">#FF6200EE</color>
    <color name="white">#FFFFFFFF</color>

    <color name="color_white_t">#CCFFFFFF</color>
    <color name="color_link">#0D47A1</color>

    <color name="primaryColor">#009688</color>
    <color name="primaryDarkColor">#00675b</color>
    <color name="secondaryColor">#ff1744</color>
    <color name="secondaryDarkColor">#c4001d</color>
</resources>
```

En el archivo de themes se configuran los colores principales que tendra nuestra aplicacion\
y que tomaran por default nuestro elementos del layout\
De este archivo normalmente se tiene dos variantes, uno para el modo oscuro y otro para el claro
```xml
<resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Base application theme. -->
    <style name="Theme.Stores" parent="Theme.MaterialComponents.DayNight.DarkActionBar">
        <!-- Primary brand color. -->
        <item name="colorPrimary">@color/purple_200</item>
        <item name="colorPrimaryVariant">@color/purple_700</item>
        <item name="colorOnPrimary">@color/black</item>
        <!-- Secondary brand color. -->
        <item name="colorSecondary">@color/teal_200</item>
        <item name="colorSecondaryVariant">@color/teal_200</item>
        <item name="colorOnSecondary">@color/black</item>
        <!-- Status bar color. -->
        <item name="android:statusBarColor">?attr/colorPrimaryVariant</item>
        <!-- Customize your theme here. -->
    </style>
</resources>
```