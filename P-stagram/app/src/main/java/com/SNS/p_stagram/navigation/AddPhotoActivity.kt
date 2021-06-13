package com.SNS.p_stagram.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.SNS.p_stagram.R
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_photo.*
import java.text.SimpleDateFormat
import java.util.*

class AddPhotoActivity : AppCompatActivity() {
    var PICK_IMAGE_FROM_ALBUM = 0
    var storage: FirebaseStorage? = null
    var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        //initiate storage - 스토리지 초기화
        storage = FirebaseStorage.getInstance()

        //Open the album - 액티비티를 실행하자마자 화면을 오픈해주는 코드
        var photoPickerIntent= Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent,PICK_IMAGE_FROM_ALBUM)

        //add image upload event - 버튼에 이벤트를 넣어주도록 한다.
        addphoto_btn_upload.setOnClickListener {
            contentUpload()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
         if (requestCode == PICK_IMAGE_FROM_ALBUM ) {
             if (resultCode == Activity.RESULT_OK){
                 //사진을 선택했을때, 이미지의 경로가 이쪽으로 온다.
                 photoUri = data?.data
                 addphoto_image.setImageURI(photoUri)
             } else {
                 //취소버튼을 눌렀을 경우. 피니시로 액티비티를 닫는다.
                 finish()
             }
         }
    }
    fun contentUpload(){
        //파일이름을 만들어주는 코드
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_" + timestamp + "_.png"

        var storageRef = storage?.reference?.child("images")?.child(imageFileName)

        //사진 업로드
        storageRef?.putFile(photoUri!!)?.addOnSuccessListener {
            //결과 성공시 메시지
            Toast.makeText(this, getString(R.string.upload_success),Toast.LENGTH_LONG).show()
        }
    }
}