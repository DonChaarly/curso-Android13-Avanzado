package com.cursosandroidant.snapshots.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.cursosandroidant.snapshots.R
import com.cursosandroidant.snapshots.SnapshotsApplication
import com.cursosandroidant.snapshots.databinding.FragmentHomeBinding
import com.cursosandroidant.snapshots.databinding.ItemSnapshotBinding
import com.cursosandroidant.snapshots.entities.Snapshot
import com.cursosandroidant.snapshots.utils.FragmentAux
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

/****
 * Project: Snapshots
 * From: com.cursosandroidant.snapshots.ui.fragments
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
class HomeFragment : Fragment(), FragmentAux {

    private lateinit var mBinding: FragmentHomeBinding
    private lateinit var mSnapshotsRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    /* ======================= Adaptador ViewHolder con Firebase ==============================
    * Para renderizar listas de los registros por ejemplo en la vista es necesario crear una clase
    * que extienda de ViewHolder la cual utilizaremos como adaptador,
    * Se puede crear una inner class simplemente para esto:
    * */
    inner class SnapshotHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemSnapshotBinding.bind(view)

        fun setListener(snapshot: Snapshot) {
            with(binding) {
                btnDelete.setOnClickListener { deleteSnapshot(snapshot) }

                cbLike.setOnCheckedChangeListener { _, checked ->
                    setLike(snapshot, checked)
                }
            }
        }
    }

    /* Se crea un objeto FirebaseRecyclerAdapter al cual pasaremos nuestra entidad y nuestro ViewHolder*/
    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<Snapshot, SnapshotHolder>
    /* Se crea un objeto RecyclerView para nuestras lista de registros*/
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupRecyclerView()
    }

    /*Se inicializa el adapter */
    private fun setupAdapter() {

        /*Se crea un objeto FirebaseDatabase con la cual obtendremos una instancia de un nodo,
        * Se le pasa el nombre en string del nodo que tenemos en Firebase
        * */
        val query = FirebaseDatabase.getInstance().reference.child(SnapshotsApplication.PATH_SNAPSHOTS)
        /*Se crea una variable FirebaseRecyclerOptions a la cual se le pasa nuestra query*/
        val options = FirebaseRecyclerOptions.Builder<Snapshot>().setQuery(query) {
            /*Obtenemos el objeto snapshot para configurar que la variable id sera nuestra key*/
            val snapshot = it.getValue(Snapshot::class.java)
            snapshot!!.id = it.key!!
            snapshot
        }.build()

        /*Se inicializa nuestro objeto FirebaseRecyclerAdapter
        * Se le pasa nuestro entity y nuestro viewHolder con las opciones que creamos
        * Dentro se debe sobreescribir los metodos onCreateViewHolder y onBindViewHolder
        * */
        mFirebaseAdapter = object : FirebaseRecyclerAdapter<Snapshot, SnapshotHolder>(options) {
            //Creamos una variable Context
            private lateinit var mContext: Context

            //onCreateViewHolder se utiliza para inicializar la vista que se utilizara y el viewHolder
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnapshotHolder {
                //Inicializamos nuestra variable Context
                mContext = parent.context

                //Relacionamos nuestra vista la cual sera la vista de nuestro item y retornamos nuestro viewHolder
                val view = LayoutInflater.from(mContext)
                        .inflate(R.layout.item_snapshot, parent, false)
                return SnapshotHolder(view)
            }

            /* onBindViewHolder se utiliza relacionar los datos de nuestro objeto con los elementos de la vista
            *  y para inicializar nuestro listener de nuestro ViewHolder
            * */
            override fun onBindViewHolder(holder: SnapshotHolder, position: Int, model: Snapshot) {
                //Recuperamos nuestro objeto snapshot con el metodo getItem al cual le pasamos la posicion
                val snapshot = getItem(position)

                //Configuramos nuestros datos y elementos de la vista
                with(holder) {
                    setListener(snapshot)

                    with(binding) {
                        tvTitle.text = snapshot.title
                        cbLike.text = snapshot.likeList.keys.size.toString()
                        cbLike.isChecked = snapshot.likeList
                                .containsKey(SnapshotsApplication.currentUser.uid)

                        Glide.with(mContext)
                                .load(snapshot.photoUrl)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop()
                                .into(imgPhoto)

                        btnDelete.visibility = if (model.ownerUid == SnapshotsApplication.currentUser.uid){
                            View.VISIBLE
                        } else {
                            View.INVISIBLE
                        }
                    }
                }
            }

            /*Tambien se tiene los metodos onDataChanged y onError*/
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChanged() {
                super.onDataChanged()
                mBinding.progressBar.visibility = View.GONE
                notifyDataSetChanged()
            }

            override fun onError(error: DatabaseError) {
                super.onError(error)
                //Toast.makeText(mContext, error.message, Toast.LENGTH_SHORT).show()
                Snackbar.make(mBinding.root, error.message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    /*Se inicia la configuracion del RecyblesView pasandole el adaptador*/
    private fun setupRecyclerView() {
        mLayoutManager = LinearLayoutManager(context)

        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            adapter = mFirebaseAdapter
        }
    }

    /*Se inicializa y finaliza el Listener del Adapter:*/
    override fun onStart() {
        super.onStart()
        mFirebaseAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mFirebaseAdapter.stopListening()
    }


    private fun deleteSnapshot(snapshot: Snapshot) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                    .setTitle(R.string.dialog_delete_title)
                    .setPositiveButton(R.string.dialog_delete_confirm) { _, _ ->
                        val storageSnapshotsRef = FirebaseStorage.getInstance().reference
                            .child(SnapshotsApplication.PATH_SNAPSHOTS)
                            .child(SnapshotsApplication.currentUser.uid)
                            .child(snapshot.id)
                        storageSnapshotsRef.delete().addOnCompleteListener { result ->
                            if (result.isSuccessful){
                                mSnapshotsRef.child(snapshot.id).removeValue()
                            } else {
                                Snackbar.make(mBinding.root, getString(R.string.home_delete_photo_error),
                                    Snackbar.LENGTH_LONG).show()
                            }
                        }
                    }
                    .setNegativeButton(R.string.dialog_delete_cancel, null)
                    .show()
        }
    }

    private fun setLike(snapshot: Snapshot, checked: Boolean) {
        val myUserRef = mSnapshotsRef.child(snapshot.id)
                .child(SnapshotsApplication.PROPERTY_LIKE_LIST)
                .child(SnapshotsApplication.currentUser.uid)

        if (checked) {
            myUserRef.setValue(checked)
        } else {
            myUserRef.setValue(null)
        }
    }

    /*
    *   FragmentAux
    * */
    override fun refresh() {
        mBinding.recyclerView.smoothScrollToPosition(0)
    }

}