package com.line.saj.components.view.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
import com.line.saj.components.view.fragment.ImageTypeBottomDialogFragment
import com.line.saj.components.viewModel.MemoEditViewModel
import com.line.saj.dao.AppDatabase
import com.line.saj.databinding.ActivityEditMemoBinding
import com.line.saj.factory.MemoEditViewModelFactory
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
import org.joda.time.DateTime

class EditMemoActivity : BaseActivity(), ImageTypeBottomDialogFragment.OnClickListener {

    private val REQUEST_IMAGE = 0
    private lateinit var binding: ActivityEditMemoBinding
    private val detailFragment = ImageTypeBottomDialogFragment.newInstance()
    private var isModifyMode = false

    private var imageThumbnailList = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_memo)

        val intent = intent
        val memoData = intent.getParcelableExtra<Memo>("MODIFY_MEMO")

        isModifyMode = memoData != null

        subscribeUi(binding, memoData)
        changeBtnText()
    }


    private fun subscribeUi(binding: ActivityEditMemoBinding, memoData: Memo?) {

        val factory = MemoEditViewModelFactory()
        val vm: MemoEditViewModel =
            ViewModelProviders.of(this, factory).get(MemoEditViewModel::class.java)

        initAdapter()


        vm.closeConverter.observe(this, Observer { finish() })

        vm.addPhotoConverter.observe(this, Observer {
            if (Build.VERSION.SDK_INT >= 23) getPermission()
            detailFragment.show(supportFragmentManager, ImageTypeBottomDialogFragment().TAG)
        })

        vm.addMemoConverter.observe(this, Observer {
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()


            if (title.isEmpty() || content.isEmpty()) {
                showContentsNullAlert()
                return@Observer
            }

            if (!isModifyMode) addMemo(Memo(0, title, content, imageThumbnailList, DateTime()))
            if (isModifyMode) {
                Log.e("modify id: ",vm.memo.value!!.memoId.toString())
                modifyMemo(vm.memo.value!!.memoId, title, content)

            }
                finish()
        })

        //TODO: viewModel memo랑 binding memo랑 연결해야함

        if(isModifyMode) modifySetData(vm,memoData!!)

        binding.vm = vm
        binding.lifecycleOwner = this
    }


    // ================================================
    //
    //  change layout for mode
    //
    // ================================================

    private fun changeBtnText() {
        when (isModifyMode) {
            true -> binding.btnOk.text = getString(R.string.modify_ok)
            false -> binding.btnOk.text = getString(R.string.add_ok)
        }
    }

    private fun modifySetData(vm:MemoEditViewModel, memo: Memo) {
        vm.memo.value = memo
        memo.image.map { addThumbnail(it) }
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
        imageThumbnailList.add(thumbnailUri)
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
            .setDeniedMessage(getString(R.string.denied_no_permission))
            .setPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .check()
    }


    // ================================================
    //
    //  Save Memo
    //
    // ================================================

    private fun addMemo(memo: Memo) {
        val r = Runnable {
            val memoRepo = MemoRepository.getInstance(AppDatabase.getInstance(this).memoDao())
            memoRepo.addMemos(memo)
        }

        val thread = Thread(r)
        thread.start()
    }

    private fun modifyMemo(memoId: Int, title: String, contents: String) {
        val r = Runnable {
            val memoRepo = MemoRepository.getInstance(AppDatabase.getInstance(this).memoDao())
            memoRepo.update(memoId, title, contents)
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
            builder.setTitle(getString(R.string.no_permission))
            builder.setMessage(getString(R.string.need_permission))
            builder.setNegativeButton(getString(R.string.add_ok)) { _, _ -> }
            builder.show()
        }
    }

    private fun showContentsNullAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.alert))
        builder.setMessage(getString(R.string.contents_null_alert))
        builder.setNegativeButton(R.string.add_ok) { _, _ -> }
        builder.show()
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
                val uri = data!!.getParcelableExtra<Uri>("path") ?: return
                Xutil.getRealPath(this, uri)?.let { addThumbnail(it) }

            }
        }
    }

}
