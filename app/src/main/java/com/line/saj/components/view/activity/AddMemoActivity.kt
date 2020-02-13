package com.line.saj.components.view.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.line.saj.R
import com.line.saj.base.BaseActivity
import com.line.saj.components.model.Memo
import com.line.saj.components.view.adapter.ThumbnailAdapter
import com.line.saj.components.view.fragment.PhotoTypeBottomDialogFragment
import com.line.saj.components.viewModel.MemoAddViewModel
import com.line.saj.dao.AppDatabase
import com.line.saj.databinding.ActivityAddMemoBinding
import com.line.saj.factory.MemoAddViewModelFactory
import com.line.saj.repository.MemoRepository
import com.line.saj.utils.Constants
import com.line.saj.utils.Constants.Companion.INTENT_ASPECT_RATIO_X
import com.line.saj.utils.Constants.Companion.INTENT_ASPECT_RATIO_Y
import com.line.saj.utils.Constants.Companion.INTENT_BITMAP_MAX_HEIGHT
import com.line.saj.utils.Constants.Companion.INTENT_BITMAP_MAX_WIDTH
import com.line.saj.utils.Constants.Companion.INTENT_IMAGE_PICKER_OPTION
import com.line.saj.utils.Constants.Companion.INTENT_LOCK_ASPECT_RATIO
import com.line.saj.utils.Constants.Companion.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT
import com.line.saj.utils.Constants.Companion.REQUEST_GALLERY_IMAGE
import com.line.saj.utils.Constants.Companion.REQUEST_IMAGE_CAPTURE
import com.line.saj.utils.Xutil
import java.io.IOException

class AddMemoActivity : BaseActivity(), PhotoTypeBottomDialogFragment.OnClickListener {

    private val REQUEST_IMAGE = 0
    private var imageArray = ArrayList<Bitmap>()
    private lateinit var binding: ActivityAddMemoBinding
    private val detailFragment = PhotoTypeBottomDialogFragment.newInstance()


    private var imageThumbnailList = arrayListOf<Uri>()
    private var imageThumbnail = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_memo)

        subscribeUi(binding)

    }


    private fun subscribeUi(binding: ActivityAddMemoBinding) {

        val factory = MemoAddViewModelFactory()
        val vm: MemoAddViewModel =
            ViewModelProviders.of(this, factory).get(MemoAddViewModel::class.java)

        initAdapter()


        vm.closeConverter.observe(this, Observer { finish() })

        vm.addPhotoConverter.observe(this, Observer {

            if (Build.VERSION.SDK_INT >= 23) getPermission()
            detailFragment.show(supportFragmentManager, PhotoTypeBottomDialogFragment().TAG)

        })

        vm.addMemoConverter.observe(this, Observer {
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()

            addMemo(Memo(0, title, content, imageThumbnail, "", ""))
            finish()
        })

        binding.vm = vm
        binding.lifecycleOwner = this
    }


    // ================================================
    //
    //  Image Thumbnail Adapter
    //
    // ================================================

    private fun initAdapter() {
        val adapter = ThumbnailAdapter()
        adapter.setListener(object : ThumbnailAdapter.OnClickListener {
            override fun onClickDeleteItem(position: Int) {
                deleteThumbnail(position)
            }

        })

        binding.rcThumbnail.adapter = adapter
    }

    private fun addThumbnail(thumbnailUri: String) {
        val adapter = binding.rcThumbnail.adapter as ThumbnailAdapter
        adapter.add(thumbnailUri)

        imageThumbnail = thumbnailUri
    }

    private fun deleteThumbnail(position: Int) {
        val adapter = binding.rcThumbnail.adapter as ThumbnailAdapter
        adapter.remove(position)
    }


    // ================================================
    //
    //  Permission
    //
    // ================================================

    private fun getPermission() {

        var permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                refuseAccessOnGallery() // 접근 금지 다이얼로그
            }
        }
        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("권한을 승인해야 사진을 첨부할 수 있습니다.")
            .setPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .check()
    }


    private fun addMemo(memo: Memo) {
        val r = Runnable {
            val memoRepo = MemoRepository.getInstance(AppDatabase.getInstance(this).memoDao())
            memoRepo.addMemos(memo)
        }

        val thread = Thread(r)
        thread.start()
    }


    // ================================================
    //
    //  Alert
    //
    // ================================================

    private fun refuseAccessOnGallery() {
        binding.rlPhotoBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("권한 없음")
            builder.setMessage("앱에서 요구하는 권한설정이 필요합니다. \n[설정] > [권한]에서 사용으로 활성해주세요.")
            builder.setNegativeButton("확인") { _, _ -> }
            builder.show()
        }
    }


    // ================================================
    //
    //  Select Photo Type
    //
    // ================================================

    override fun onClickItem(item: String) {
        when (item) {
            Constants.PHOTO_TYPE_CAMERA -> {
                launchCameraIntent()
                detailFragment.dismiss()
            }
            Constants.PHOTO_TYPE_GALLERY -> {
                launchGalleryIntent()
                detailFragment.dismiss()
            }
            Constants.PHOTO_TYPE_URL -> ""
        }
    }


    // ================================================
    //
    //  Image
    //
    // ================================================

    private fun launchCameraIntent() {
        val intent = Intent(this, ImagePickerActivity::class.java)

        intent.putExtra(
            INTENT_IMAGE_PICKER_OPTION,
            REQUEST_IMAGE_CAPTURE
        )

        // setting aspect ratio
        intent.putExtra(INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(INTENT_ASPECT_RATIO_Y, 1)

        // setting maximum bitmap width and height
        intent.putExtra(INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(INTENT_BITMAP_MAX_WIDTH, 1000)
        intent.putExtra(INTENT_BITMAP_MAX_HEIGHT, 1000)

        startActivityForResult(intent, REQUEST_IMAGE)
    }


    private fun launchGalleryIntent() {
        val intent = Intent(this, ImagePickerActivity::class.java)
        intent.putExtra(
            INTENT_IMAGE_PICKER_OPTION,
            REQUEST_GALLERY_IMAGE
        )
        // setting aspect ratio
        intent.putExtra(INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(INTENT_ASPECT_RATIO_Y, 1)
        startActivityForResult(intent, REQUEST_IMAGE)
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                val uri = data!!.getParcelableExtra<Uri>("path")
                addThumbnail(Xutil.getRealPath(this, uri)!!)
                Log.e("uri", Xutil.getRealPath(this, uri)!!)
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    imageArray.add(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

}
