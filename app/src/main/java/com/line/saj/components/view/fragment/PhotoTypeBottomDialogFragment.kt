package com.line.saj.components.view.fragment

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.line.saj.R
import com.line.saj.databinding.FragmentPhotoTypeBinding
import org.w3c.dom.Text
import java.lang.RuntimeException


class PhotoTypeBottomDialogFragment : BottomSheetDialogFragment(), View.OnClickListener {

    val TAG = "photoTypeFragment"

    private var listener: OnClickListener? = null
    private lateinit var binding: FragmentPhotoTypeBinding


    //=============================================
    //
    // Layout Animation
    //
    //=============================================

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val bottomSheetDialog =
            super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        bottomSheetDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation

        bottomSheetDialog.setOnShowListener { dialog: DialogInterface ->
            val dialogc = dialog as BottomSheetDialog
            val bottomSheet =
                dialogc.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet).apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                            state = BottomSheetBehavior.STATE_EXPANDED
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                })
            }

        }

        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_photo_type, container, false)

        subscribeUi(binding)


        return binding.root
    }


    private fun subscribeUi(binding: FragmentPhotoTypeBinding) {

        binding.tvCamera.setOnClickListener(this)
        binding.tvGallery.setOnClickListener(this)
        binding.tvUrl.setOnClickListener(this)

        binding.lifecycleOwner = this


    }

    // ================================================
    //
    //  Click Listener
    //
    // ================================================

    fun setListener(listener: OnClickListener) {
        this.listener = listener
    }

    interface OnClickListener {
        fun onClickItem(item: String)
    }


    override fun onClick(v: View?) {
        val textView = v as TextView
        listener!!.onClickItem(textView.text.toString())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnClickListener) {
            listener = context
        } else {
            throw RuntimeException(
                "$context must implement ItemClickListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    companion object {
        fun newInstance(): PhotoTypeBottomDialogFragment =
            PhotoTypeBottomDialogFragment().apply {
                arguments = Bundle().apply {

                }
            }

    }


}