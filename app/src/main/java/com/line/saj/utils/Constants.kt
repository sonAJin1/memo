package com.line.saj.utils

class Constants {

    companion object {
        const val SERVER_URL = "https://grepp-programmers-challenges.s3.ap-northeast-2.amazonaws.com/"
        const val DB_NAME = "memo_db"

        const val PHOTO_TYPE_CAMERA = "Camera"
        const val PHOTO_TYPE_GALLERY = "Gallery"
        const val PHOTO_TYPE_URL = "URL"


        const val INTENT_IMAGE_PICKER_OPTION = "image_picker_option"
        const val INTENT_ASPECT_RATIO_X = "aspect_ratio_x"
        const val INTENT_ASPECT_RATIO_Y = "aspect_ratio_Y"
        const val INTENT_LOCK_ASPECT_RATIO = "lock_aspect_ratio"
        const val INTENT_IMAGE_COMPRESSION_QUALITY = "compression_quality"
        const val INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT = "set_bitmap_max_width_height"
        const val INTENT_BITMAP_MAX_WIDTH = "max_width"
        const val INTENT_BITMAP_MAX_HEIGHT = "max_height"

        const val REQUEST_IMAGE_CAPTURE = 0
        const val REQUEST_GALLERY_IMAGE = 1
    }
}