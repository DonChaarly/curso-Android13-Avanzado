# Hacer uso de la galeria para obtener imagenes

Esto se vio en la clase 210 del curso.

Para abrir la galeria del telefono se utiliza un Intent, este seria el codigo que se uitliza:

```kotlin
private fun openGallery() {
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    galleryResult.launch(intent)
}
```

Para cachar el resultado de la seleccion de la imagen se utliza el siguiente codigo:
```kotlin
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
```



## Codigo ejemplo layout
Este codigo corresponde a la leccion 10
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingHorizontal="@dimen/common_padding_default"
    android:paddingVertical="@dimen/common_padding_min"
    tools:context=".ui.fragments.AddFragment">

    <ProgressBar
        android:id="@+id/progressBar"
        style="?android:progressBarStyleHorizontal"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:visibility="invisible"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <com.google.android.material.button.MaterialButton
        android:id="@+id/btnPost"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="@dimen/common_padding_min"
        android:text="@string/add_button_post"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toBottomOf="@id/progressBar" />

    <TextView
        android:id="@+id/tvMessage"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="@string/post_message_title"
        android:textAppearance="?attr/textAppearanceHeadline6"
        app:layout_constraintBottom_toBottomOf="@id/btnPost"
        app:layout_constraintEnd_toStartOf="@id/btnPost"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="@id/btnPost" />

    <com.google.android.material.textfield.TextInputLayout
        android:id="@+id/tilTitle"
        style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox.Dense"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginTop="@dimen/common_padding_min"
        android:hint="@string/add_hint_title"
        android:visibility="gone"
        app:endIconMode="clear_text"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/btnPost">

        <com.google.android.material.textfield.TextInputEditText
            android:id="@+id/etTitle"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:inputType="textCapSentences|textMultiLine"
            android:maxLines="@integer/add_max_lines_title" />
    </com.google.android.material.textfield.TextInputLayout>

    <ImageView
        android:id="@+id/imgPhoto"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_marginTop="@dimen/common_padding_min"
        android:background="@color/gray_100"
        android:importantForAccessibility="no"
        android:scaleType="centerCrop"
        app:layout_constraintDimensionRatio="H, 4:3"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/tilTitle" />

    <ImageButton
        android:id="@+id/btnSelect"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:background="?attr/selectableItemBackground"
        android:contentDescription="@string/add_button_select"
        android:src="@drawable/ic_image_search"
        app:layout_constraintBottom_toBottomOf="@id/imgPhoto"
        app:layout_constraintEnd_toEndOf="@id/imgPhoto"
        app:layout_constraintStart_toStartOf="@id/imgPhoto"
        app:layout_constraintTop_toTopOf="@id/imgPhoto" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

## Codigo ejemplo de kotlin
Este codigo corresponde a la leccion 10
```kotlin
class AddFragment : Fragment() {

    private lateinit var mBinding: FragmentAddBinding
    private lateinit var mSnapshotsStorageRef: StorageReference
    private lateinit var mSnapshotsDatabaseRef: DatabaseReference

    private var mainAux: MainAux? = null

    private var mPhotoSelectedUri: Uri? = null

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mBinding = FragmentAddBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTextField()
        setupButtons()
        setupFirebase()
    }

    private fun setupTextField() {
        with(mBinding) {
            etTitle.addTextChangedListener { validateFields(tilTitle) }
        }
    }

    private fun setupButtons() {
        with(mBinding) {
            btnPost.setOnClickListener { if (validateFields(tilTitle)) postSnapshot() }
            btnSelect.setOnClickListener { openGallery() }
        }
    }

    private fun setupFirebase() {
        mSnapshotsStorageRef = FirebaseStorage.getInstance().reference.child(SnapshotsApplication.PATH_SNAPSHOTS)
        mSnapshotsDatabaseRef = FirebaseDatabase.getInstance().reference.child(SnapshotsApplication.PATH_SNAPSHOTS)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainAux = activity as MainAux
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResult.launch(intent)
    }

    private fun postSnapshot() {
        if (mPhotoSelectedUri != null) {
            enableUI(false)
            mBinding.progressBar.visibility = View.VISIBLE

            val key = mSnapshotsDatabaseRef.push().key!!
            val myStorageRef = mSnapshotsStorageRef.child(SnapshotsApplication.currentUser.uid)
                    .child(key)

            myStorageRef.putFile(mPhotoSelectedUri!!)
                    .addOnProgressListener {
                        val progress = (100 * it.bytesTransferred / it.totalByteCount).toInt()
                        with(mBinding) {
                            progressBar.progress = progress
                            tvMessage.text = String.format("%s%%", progress)
                        }
                    }
                    .addOnCompleteListener {
                        mBinding.progressBar.visibility = View.INVISIBLE
                    }
                    .addOnSuccessListener {
                        it.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                            saveSnapshot(key, downloadUri.toString(), mBinding.etTitle.text.toString().trim())
                        }
                    }
                    .addOnFailureListener {
                        mainAux?.showMessage(R.string.post_message_post_image_fail)
                    }
        }
    }

    private fun saveSnapshot(key: String, url: String, title: String) {
        val snapshot = Snapshot(ownerUid = SnapshotsApplication.currentUser.uid,
            title = title, photoUrl = url)
        mSnapshotsDatabaseRef.child(key).setValue(snapshot)
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
                .addOnCompleteListener { enableUI(true) }
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
```