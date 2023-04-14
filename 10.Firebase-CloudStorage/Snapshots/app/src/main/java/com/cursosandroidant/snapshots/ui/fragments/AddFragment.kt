package com.cursosandroidant.snapshots.ui.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.cursosandroidant.snapshots.R
import com.cursosandroidant.snapshots.SnapshotsApplication
import com.cursosandroidant.snapshots.databinding.FragmentAddBinding
import com.cursosandroidant.snapshots.entities.Snapshot
import com.cursosandroidant.snapshots.utils.MainAux
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddFragment : Fragment() {

    private lateinit var mBinding: FragmentAddBinding

    private var mainAux: MainAux? = null

    private var mPhotoSelectedUri: Uri? = null

    /*  ================================ MANEJO DE GALERIA E IMAGENES ===========================
    * Para abrir la galeria del telefono se utiliza un Intent
    * Se especifica la accion que se realizara, la cual sera en este caso Intent.ACTION_PICK para seleccionar una imagen
    * y se especifica la aplicacion que se abrira la cual sera la de imagenes: MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    *  */
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResult.launch(intent)
    }

    /*Se relaciona el metodo con el boton*/
    private fun setupButtons() {
        with(mBinding) {
            btnPost.setOnClickListener { if (validateFields(tilTitle)) postSnapshot() }
            btnSelect.setOnClickListener { openGallery() }
        }
    }
    /*Para obtener los resultados de la eleccion de imagen se crea un objeto registerForActivityResult*/
    private val galleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK) {
            mPhotoSelectedUri = it.data?.data

            with(mBinding) {
                imgPhoto.setImageURI(mPhotoSelectedUri)
                tilTitle.visibility = View.VISIBLE
                tvMessage.text = getString(R.string.post_message_valid_title)
            }
        }
    }
    /*==========================================================================================*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mBinding = FragmentAddBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    /*====================== SUBIDA DE ARCHIVOS Y NUEVOS REGISTROS EN FIRESTORE =================
    * Se neceistan crear variables StorageReference y DatabaseReference para hacer referencia al
    * storage y base de datos de Firebase
    * */
    private lateinit var mSnapshotsStorageRef: StorageReference
    private lateinit var mSnapshotsDatabaseRef: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTextField()
        setupButtons()
        setupFirebase()
    }

    /*Se inicializan las variables Reference pasandole el nombre de la tabla o storage que se tiene en Firebase*/
    private fun setupFirebase() {
        mSnapshotsStorageRef = FirebaseStorage.getInstance().reference.child(SnapshotsApplication.PATH_SNAPSHOTS)
        mSnapshotsDatabaseRef = FirebaseDatabase.getInstance().reference.child(SnapshotsApplication.PATH_SNAPSHOTS)
    }

    /*========================================================================================*/

    private fun setupTextField() {
        with(mBinding) {
            etTitle.addTextChangedListener { validateFields(tilTitle) }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainAux = activity as MainAux
    }

    /*Con postSnapshot se sube una imagen a nusetro storage*/
    private fun postSnapshot() {
        //verificamos que se ha elegido una imagen
        if (mPhotoSelectedUri != null) {
            enableUI(false)
            mBinding.progressBar.visibility = View.VISIBLE

            // creamos una key a la hora de subir el archivo que sera la ruta en la que estara alojado este
            val key = mSnapshotsDatabaseRef.push().key!!
            //Obtenemos la referencia a la carpeta de Storae del usuario logueado
            val myStorageRef = mSnapshotsStorageRef.child(SnapshotsApplication.currentUser.uid)
                    .child(key)

            //Con putFile se sube la imagen
            myStorageRef.putFile(mPhotoSelectedUri!!)
                    // Conn addOnProgressListener podemos escuchar el progreso de la subida de la imagen
                    .addOnProgressListener {
                        val progress = (100 * it.bytesTransferred / it.totalByteCount).toInt()
                        with(mBinding) {
                            progressBar.progress = progress
                            tvMessage.text = String.format("%s%%", progress)
                        }
                    }
                    // con addOnCompleteListener escuchamos cuando se completo la subida
                    .addOnCompleteListener {
                        mBinding.progressBar.visibility = View.INVISIBLE
                    }
                    // Con addOnSuccessListener se confirma que se completo la subida con exito
                    .addOnSuccessListener {
                        it.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                            saveSnapshot(key, downloadUri.toString(), mBinding.etTitle.text.toString().trim())
                        }
                    }
                    // Con addOnFailureListener se cachan los errores que se puedan tener
                    .addOnFailureListener {
                        mainAux?.showMessage(R.string.post_message_post_image_fail)
                    }
        }
    }

    /*Se utiliza saveSnapshot para guardar un nuevo registro */
    private fun saveSnapshot(key: String, url: String, title: String) {
        //Se crea un objeto de nuestra clase
        val snapshot = Snapshot(ownerUid = SnapshotsApplication.currentUser.uid,
            title = title, photoUrl = url)
        /*Con la referencia a nuestra tabla utilizamos el metodo setValue para setear un registro*/
        mSnapshotsDatabaseRef.child(key).setValue(snapshot)
                // con addOnSuccessListener se escucha cuando se subio el registro exitosamente
                .addOnSuccessListener {
                    hideKeyboard()
                    mainAux?.showMessage(R.string.post_message_post_success)

                    with(mBinding) {
                        tilTitle.visibility = View.GONE
                        etTitle.setText("")
                        tilTitle.error = null
                        tvMessage.text = getString(R.string.post_message_title)
                        imgPhoto.setImageDrawable(null)
                    }
                }
                // con addOnCompleteListener se escucha cuando se completo la subida del registro
                .addOnCompleteListener { enableUI(true) }
                // con addOnFailureListener se escucha cuando algo fallo
                .addOnFailureListener { mainAux?.showMessage(R.string.post_message_post_snapshot_fail) }
    }

    private fun validateFields(vararg textFields: TextInputLayout): Boolean {
        var isValid = true

        for (textField in textFields) {
            if (textField.editText?.text.toString().trim().isEmpty()) {
                textField.error = getString(R.string.helper_required)
                isValid = false
            } else textField.error = null
        }

        return isValid
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun enableUI(enable: Boolean) {
        with(mBinding) {
            btnSelect.isEnabled = enable
            btnPost.isEnabled = enable
            tilTitle.isEnabled = enable
        }
    }
}