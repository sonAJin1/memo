package com.line.saj.components.view.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.line.saj.R
import com.line.saj.base.BaseActivity
import com.line.saj.components.view.fragment.PhotoTypeBottomDialogFragment
import com.line.saj.components.viewModel.MemoAddViewModel
import com.line.saj.components.viewModel.MemoListViewModel
import com.line.saj.dao.AppDatabase
import com.line.saj.databinding.ActivityAddMemoBinding
import com.line.saj.databinding.ActivityMainBinding
import com.line.saj.factory.MemoAddViewModelFactory
import com.line.saj.factory.MemoViewModelFactory
import com.line.saj.repository.MemoRepository
import com.line.saj.utils.Constants

class AddMemoActivity : BaseActivity(),PhotoTypeBottomDialogFragment.OnClickListener {

    private var isPermission = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityAddMemoBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_add_memo)

        subscribeUi(binding)


        if (Build.VERSION.SDK_INT >= 23){ getPermission(binding) }


    }


    private fun subscribeUi(binding: ActivityAddMemoBinding) {

        val factory = MemoAddViewModelFactory()
        val vm: MemoAddViewModel =
            ViewModelProviders.of(this, factory).get(MemoAddViewModel::class.java)


        vm.closeConverter.observe(this, Observer {
            finish()
        })

        vm.addPhotoConverter.observe(this, Observer {
//            val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(this)
//            bottomSheetDialog.setContentView(R.layout.fragment_photo_type)
//            bottomSheetDialog.show()
//
//
            val detailFragment = PhotoTypeBottomDialogFragment.newInstance()
            detailFragment.show(supportFragmentManager, PhotoTypeBottomDialogFragment().TAG)
        })

        binding.vm = vm
        binding.lifecycleOwner = this
    }


    // ================================================
    //
    //  Adapter
    //
    // ================================================

    private fun initAdapter(){

    }




    // ================================================
    //
    //  Permission
    //
    // ================================================

    private fun getPermission(binding: ActivityAddMemoBinding){

        var permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                /**
                 * 사진 추가할 수 있는 방법
                 * 1. 사진첩에 저장되어 있는 이미지  2. 카메라로 새로 촬영한 이미지  3. 외부 이미지 주소 (URL)
                 */


            }
            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                refuseAccessOnGallery(binding) // 카메라 클릭처리 클릭하면 접근 금지 다이얼로그
            }
        }
        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("권한을 승인해야 사진을 첨부할 수 있습니다.")
            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            .check()
    }


    // ================================================
    //
    //  Alert
    //
    // ================================================

    private fun refuseAccessOnGallery(binding: ActivityAddMemoBinding){
        binding.rlPhotoBtn.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("권한 없음")
            builder.setMessage("앱에서 요구하는 권한설정이 필요합니다. \n[설정] > [권한]에서 사용으로 활성해주세요.")
            builder.setNeutralButton("확인") { dialog, which ->
            }
            builder.show()
        }

    }


    // ================================================
    //
    //  Select Photo Type
    //
    // ================================================
    override fun onClickItem(item: String) {
        when(item){
            Constants.PHOTO_TYPE_CAMERA->showToast(item)
            Constants.PHOTO_TYPE_GALLERY->showToast(item)
            Constants.PHOTO_TYPE_URL->showToast(item)
        }
    }

}
