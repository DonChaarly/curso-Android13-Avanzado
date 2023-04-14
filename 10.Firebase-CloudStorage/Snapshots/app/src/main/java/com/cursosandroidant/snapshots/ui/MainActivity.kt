package com.cursosandroidant.snapshots.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.cursosandroidant.snapshots.R
import com.cursosandroidant.snapshots.SnapshotsApplication
import com.cursosandroidant.snapshots.databinding.ActivityMainBinding
import com.cursosandroidant.snapshots.ui.fragments.AddFragment
import com.cursosandroidant.snapshots.ui.fragments.HomeFragment
import com.cursosandroidant.snapshots.ui.fragments.ProfileFragment
import com.cursosandroidant.snapshots.utils.FragmentAux
import com.cursosandroidant.snapshots.utils.MainAux
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

/****
 * Project: Snapshots
 * From: com.cursosandroidant.snapshots.ui
 * Created by Alain Nicolás Tello on 01/02/23 at 11:12
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
class MainActivity : AppCompatActivity(), MainAux {

    private lateinit var mBinding: ActivityMainBinding

    private lateinit var mActiveFragment: Fragment
    private var mFragmentManager: FragmentManager? = null

    /* ============================= Autenticacion con Firebase =================================
    * Se declaran dos variables FirebaseAuth
    * */
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private var mFirebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupAuth()
    }

    /* Se configura un objeto registerforActivityResult para saber si el usuario pudo o no inciar sesion y regreso a esta pantalla*/
    private val authResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK) {
            Toast.makeText(this, R.string.main_auth_welcome, Toast.LENGTH_SHORT).show()
        } else {
            //Aqui se verifica que se ha oprimido el boton de atras, si es el caso simplemente fializamos la actividad, para que no siga tratndo de iniciar sesion y se lance la actividad
            if (IdpResponse.fromResultIntent(it.data) == null) {
                finish()
            }
        }
    }

    // Inicializacion nuestras variables FirebaseAuth y obtenemos el usuario logueado en caso de haber y sino se lanza la activity para loguearse
    private fun setupAuth() {
        // Inicializamos nuestra variable mFirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance()
        // Inicializamos nuestra variable mAuthListener indicandole lo que hara cuando trate de autenticarse el usuario al entra a la aplicacion
        mAuthListener = FirebaseAuth.AuthStateListener { it ->
            // con it.currentUser obtenemos el usuario activo
            if (it.currentUser == null) {
                //Si no se encuentra ningun usuario, se lanza la activity para loguearse
                authResult.launch(
                    AuthUI.getInstance().createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                            //con setAvailableProviders indicamos todos los proveedores que queremos tener, por ejemplo por correo y password y por google
                        .setAvailableProviders(
                            listOf(
                                AuthUI.IdpConfig.EmailBuilder().build(),
                                AuthUI.IdpConfig.GoogleBuilder().build())
                        )
                        .build()
                )
                mFragmentManager = null
            } else {
                //En caso de que se encuentre un usuario logueado, se lanza lo que se tenga que lanzar y se puede guardar en una variable global la inforamcion del usuario
                SnapshotsApplication.currentUser = it.currentUser!!

                val fragmentProfile = mFragmentManager?.findFragmentByTag(ProfileFragment::class.java.name)
                fragmentProfile?.let {
                    (it as FragmentAux).refresh()
                }

                if (mFragmentManager == null) {
                    mFragmentManager = supportFragmentManager
                    setupBottomNav(mFragmentManager!!)
                }
            }
        }
    }

    // en onResume iniciamos la pedida de credenciales
    override fun onResume() {
        super.onResume()
        mFirebaseAuth?.addAuthStateListener(mAuthListener)
    }

    // en onPause removemos el listener
    override fun onPause() {
        super.onPause()
        mFirebaseAuth?.removeAuthStateListener(mAuthListener)
    }

    /*=======================================================================================*/

    private fun setupBottomNav(fragmentManager: FragmentManager) {
        mFragmentManager?.let { //clean before to prevent errors
            for (fragment in it.fragments) {
                it.beginTransaction().remove(fragment!!).commit()
            }
        }

        val homeFragment = HomeFragment()
        val addFragment = AddFragment()
        val profileFragment = ProfileFragment()

        mActiveFragment = homeFragment

        fragmentManager.beginTransaction()
            .add(R.id.hostFragment, profileFragment, ProfileFragment::class.java.name)
            .hide(profileFragment).commit()
        fragmentManager.beginTransaction()
            .add(R.id.hostFragment, addFragment, AddFragment::class.java.name)
            .hide(addFragment).commit()
        fragmentManager.beginTransaction()
            .add(R.id.hostFragment, homeFragment, HomeFragment::class.java.name).commit()

        mBinding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    fragmentManager.beginTransaction().hide(mActiveFragment).show(homeFragment).commit()
                    mActiveFragment = homeFragment
                    true
                }
                R.id.action_add -> {
                    fragmentManager.beginTransaction().hide(mActiveFragment).show(addFragment).commit()
                    mActiveFragment = addFragment
                    true
                }
                R.id.action_profile -> {
                    fragmentManager.beginTransaction().hide(mActiveFragment).show(profileFragment).commit()
                    mActiveFragment = profileFragment
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
    }

    /*
    *   MainAux
    * */
    override fun showMessage(resId: Int, duration: Int) {
        Snackbar.make(mBinding.root, resId, duration)
            .setAnchorView(mBinding.bottomNav)
            .show()
    }
}