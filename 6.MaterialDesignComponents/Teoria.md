# Material Design Components (MDC)

Los componentes de Material Design son componentes que ha creado Google para que nosotros los utilicemos \
Estos componentes a parte de ya tener una funcionalidad especifica, tiene un diseno mejorado.

## Margenes con lineamientos de Google

Medidas que se ocuparan seguido es util definirlas en un archivo dentro de values que puede llamarse dimens.xml y tendra la siguiente estructura
```xml
<resources>
    <dimen name="app_bar_height">180dp</dimen>
    <dimen name="fab_margin">16dp</dimen>
    <dimen name="text_margin">16dp</dimen>

    <dimen name="card_img_size">88dp</dimen>
    <dimen name="common_padding_min">8dp</dimen>
    <dimen name="common_padding_default">16dp</dimen>
    <dimen name="card_img_cover_height">160dp</dimen>
    <dimen name="common_padding_middle">24dp</dimen>
</resources>
```

### Margenes minimos

Los margenes minimos son de **8dp**

### Margenes por default

Los margenes de **16dp** son los mas utilizados dentro de las aplicaciones android

## NestedScrollView

Este compoente es igual a un ScrollView normal pero esta optimizado para trabajar con los MDC


## BottomAppBar
![](/6.MaterialDesignComponents/Imagenes/BottomAppBar.png)\

Es una barra de acciones en la parte inferior de la pantalla\
Esta pensada para las aplicaciones en las que se hace un scroll y se quiere ofrecer un menu o acciones de forma accesible al usuario

## FloatingActionButton

Este es un boton flotante que se mantiene por encima de todos los demas elementos del layout

## MaterialCardView
![](/6.MaterialDesignComponents/Imagenes/MaterialCardView.jpg)\

Los MaterialCardView son otro tipo de contenedor pero especializado en mostrar informacion en areas mas pequenas a modo de tarjeta 

## MaterialButton


Los MaterialButton son igual que los botones pero mas apegados a los lineamientos de google

## UI Para TextView 

Con las siguientes propiedades se les puede dar una apariencia mas acorde a los estandares de google
    PROPIEDADES: 
textApparence: con la libreria ?attr/ se puede seleccionar apariencias predefinidas
maxLines: Para indicar el maximo de lineas que seran visibles
ellipsize: para mostrar puntos suspensivos e indicar que el texto sigue

## ImageView

Este elemento se utiliza para mostrar una imagen

## TextInputLayout

Este es un contenedor que se utiliza para los TextInputEditText, y nos ofrece muchas mejoras para el usuario
como que el placeHolder o hint al escribir algo no desaparezca sino se vaya a la parte de arriba con una animacino
o poder colocar alguna instruccion debajo del input como una instruccion
Tambien poder especificar un icono al final del input como un ojo para mostar la contrasena

## TextInputEditText

Este elementos es muy similar a un EditText, sin embargo tiene mejoras para ofrecer una mejor experiencia al usuario

## MaterialCheckBox

Este elemento es un checkBox mejorado, se utiliza para marcar opciones o desmarcarlas

## MaterialButtonToggleGroup
![](/6.MaterialDesignComponents/Imagenes/MaterialButtonToggleGroup.png)\
Este elemento se utiliza para agrupar botones como opciones
Este elemento tomara control sobre los botones que tenga dentro

## SnackBar
![](/6.MaterialDesignComponents/Imagenes/SnackBar.png)\

El Snackbar se utiliza para mostrar un mensaje y/o ejecutar alguna accion\
El SnackBar solo se puede crear dentro del activity con kotlin, no en el layout

## Glide

Glide es muy util a la hora de cargar imagenes, especialmente de internet\

Para poder acceder a archivos que estan en internet necesitamos agregar este permiso en el AndroidManifest.xml
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET"/>
    

</manifest>
```

Es necesario importar las dependencias de esta libreria para poder utilizarla dentro de build.gradle(Module:app)
```java
plugins {
    id 'kotlin-kapt'
}

dependencies {
    ...

    //Glide
    implementation 'com.github.bumptech.glide:glide:4.14.2'
    kapt 'com.github.bumptech.glide:compiler:4.14.2'

}
```

## SwitchMaterial

El SwitchMaterial es un elemento booleano que manda true o false si el usuario lo oprime\
Es como un tipo de CheckBox

## Slider

El Slider es una herramienta que nos ayuda a representar rangos
como un rango de volumen

## Chip
![](/6.MaterialDesignComponents/Imagenes/Chip.png)\

Los Chip nos sirven para representar opciones que se pueden eliminar debido a su boton x
se utilizan para por ejemplo representar gustos
Al hacer click sobre ellos, aparece un icono de que fue seleccionado

## MaterialAlertDialogBuilder
![](/6.MaterialDesignComponents/Imagenes/MaterialAlertDialogBuilder.png)\
Es un alerta que nos sirve para mostrar mensajes al usuario\
Se establece desde kotlin,
con setView se puede establecer un layout especifico que se quiera mostrar en vez del que se tiene por defecto
con setPositiveButton se coloca el texto que tendra el boton y el evento que se ejecutara al oprimirlo, esto se puede omitir y establecerlo mastarde con el metodo getButton
con setNegativeButton se coloca el texto que tendra el boton de cancelar y el evento que queramos que se ejecute al oprimirlo
con setCancelable establecemos que la alerta no se pueda cancelar

```kotlin
val dialogView = layoutInflater.inflate(R.layout.dialog_register, null)
val dialog = MaterialAlertDialogBuilder(this)
    .setTitle("Titulo de la alerta")
    .setView(dialogView)
    .setCancelable(false)
    .setPositiveButton('Aceptar') { _, _ -> }
    .setNegativeButton('Cancelar', null)
    .create()

dialog.show()

dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
    ...
}
```

Para establecer varias opciones como un tipo menu:
```kotlin
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
```

## BottomNavigationView
![](/6.MaterialDesignComponents/Imagenes/bottomNavigationBar.png)\
Este componente se utiliza para agregar un menu de navegacion en la parte inferior,

Para implementarlo:
1. Se crea un layout menu que tendra los items:
   ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <menu xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto">
        <item
            android:id="@+id/action_profile"
            android:title="@string/home_profile"
            android:icon="@drawable/ic_account_circle"
            app:showAsAction="never"/>
        ...
    </menu>
   ```
2. Se coloca en la Activity_main o en el layout que se quiera colocar la etiquta del BottomNavigationView
   ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
        ...>

        <com.google.android.material.bottomnavigation.BottomNavigationView
            android:id="@+id/bottomNav"
            app:menu="@menu/bottom_nav_menu"
        ...
        />

    </androidx.constraintlayout.widget.ConstraintLayout>
   ```
3. En el codigo de kotlin se puede acceder al botonBar de la siguente forma:
   ```kotlin
   mBinding.bottomNav.setOnItemSelectedListener {
        when (it.itemId) {
            R.id.action_home -> {
                fragmentManager.beginTransaction().hide(mActiveFragment).show(homeFragment).commit()
                mActiveFragment = homeFragment
                true
            }
            else -> false
        }
    }

    mBinding.bottomNav.setOnItemReselectedListener {
        when (it.itemId) {
            R.id.action_home -> (homeFragment as FragmentAux).refresh()
        }
    }
   ```


