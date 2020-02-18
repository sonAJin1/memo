package com.line.saj.components.view.activity

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider.getUriForFile
import com.line.saj.R
import com.line.saj.base.BaseActivity
import com.line.saj.utils.Constants.Companion.INTENT_ASPECT_RATIO_X
import com.line.saj.utils.Constants.Companion.INTENT_ASPECT_RATIO_Y
import com.line.saj.utils.Constants.Companion.INTENT_BITMAP_MAX_HEIGHT
import com.line.saj.utils.Constants.Companion.INTENT_BITMAP_MAX_WIDTH
import com.line.saj.utils.Constants.Companion.INTENT_IMAGE_COMPRESSION_QUALITY
import com.line.saj.utils.Constants.Companion.INTENT_IMAGE_PICKER_OPTION
import com.line.saj.utils.Constants.Companion.INTENT_LOCK_ASPECT_RATIO
import com.line.saj.utils.Constants.Companion.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT
import com.line.saj.utils.Constants.Companion.REQUEST_GALLERY_IMAGE
import com.line.saj.utils.Constants.Companion.REQUEST_IMAGE_CAPTURE
import com.yalantis.ucrop.UCrop
import java.io.File




class ImagePickerActivity : BaseActivity() {

    val TAG = ImagePickerActivity::class.java.simpleName

     private var lockAspectRatio = false
     private var setBitmapMaxWidthHeight = false
     private var aspectRatioX = 16
     private var aspectRatioY = 9
     private var bitmapMaxWidth = 1000
     private var bitmapMaxHeight = 1000
     private var imageCompression = 80
     private var fileName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_picker)

        val intent = intent
        if (intent == null) {
            showToast(getString(R.string.image_picker_error))
            return
        }

        aspectRatioX = intent.getIntExtra(INTENT_ASPECT_RATIO_X, aspectRatioX)
        aspectRatioY = intent.getIntExtra(INTENT_ASPECT_RATIO_Y, aspectRatioY)
        imageCompression = intent.getIntExtra(INTENT_IMAGE_COMPRESSION_QUALITY, imageCompression)
        lockAspectRatio = intent.getBooleanExtra(INTENT_LOCK_ASPECT_RATIO, false)
        setBitmapMaxWidthHeight = intent.getBooleanExtra(INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, false)
        bitmapMaxWidth = intent.getIntExtra(INTENT_BITMAP_MAX_WIDTH, bitmapMaxWidth)
        bitmapMaxHeight = intent.getIntExtra(INTENT_BITMAP_MAX_HEIGHT, bitmapMaxHeight)

        val requestCode = intent.getIntExtra(INTENT_IMAGE_PICKER_OPTION, -1)
        if (requestCode == REQUEST_IMAGE_CAPTURE) takeCameraImage()
        if (requestCode != REQUEST_IMAGE_CAPTURE) chooseImageFromGallery()
    }



    // ================================================
    //
    //  Get Image
    //
    // ================================================

    private fun takeCameraImage() {
        fileName = System.currentTimeMillis().toString() + ".jpg"

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getCacheImagePath(fileName!!))

        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun chooseImageFromGallery() {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(pickPhoto, REQUEST_GALLERY_IMAGE)
    }

    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) cropImage(getCacheImagePath(fileName!!))
                if (resultCode != Activity.RESULT_OK) setResultCancelled()
            }
            REQUEST_GALLERY_IMAGE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val imageUri = data!!.data
                    cropImage(imageUri!!)
                }
                if (resultCode != Activity.RESULT_OK) setResultCancelled()
            }
            UCrop.REQUEST_CROP -> {
                if (resultCode == Activity.RESULT_OK) handleUCropResult(data!!)
                if (resultCode != Activity.RESULT_OK) setResultCancelled()
            }
            UCrop.RESULT_ERROR -> {
                val cropError = UCrop.getError(data!!)
                Log.e(TAG, "Crop error: $cropError")
                setResultCancelled()
            }
        }
    }


    private fun cropImage(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, queryName(contentResolver, sourceUri)))
        val options = UCrop.Options()
        options.setCompressionQuality(imageCompression)
        options.setToolbarColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
        options.setStatusBarColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
        options.setActiveWidgetColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))

        if (lockAspectRatio) options.withAspectRatio(
            aspectRatioX.toFloat(),
            aspectRatioY.toFloat()
        )
        if (setBitmapMaxWidthHeight) options.withMaxResultSize(bitmapMaxWidth, bitmapMaxHeight)

        UCrop.of(sourceUri, destinationUri)
            .withOptions(options)
            .start(this)
    }

    private fun handleUCropResult(data: Intent) {
        val resultUri = UCrop.getOutput(data)
        setResultOk(resultUri!!)
    }

    private fun setResultOk(imagePath: Uri){
        val intent = Intent()
        intent.putExtra("path", imagePath)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun setResultCancelled(){
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    private fun getCacheImagePath(fileName: String): Uri {
        val path = File(externalCacheDir, "camera")
        if(!path.exists()) path.mkdir()
        var image = File(path, fileName)
        return getUriForFile(applicationContext, "$packageName.provider", image)
    }

    private fun queryName(resolver: ContentResolver, uri: Uri): String {
        val returnCursor = resolver.query(uri, null, null, null, null)
        assert( returnCursor != null)
        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }

    private fun clearCache(){
        val path = File(applicationContext.externalCacheDir, "camera")
        if (path.exists() && path.isDirectory) {
            for (child in path.listFiles()) {
                child.delete()
            }
        }
    }


}
