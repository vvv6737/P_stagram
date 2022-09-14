package com.SNS.p_stagram.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.multidex.MultiDex
import com.SNS.p_stagram.R
import com.SNS.p_stagram.navigation.model.ContentDTO
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_photo.*
import java.text.SimpleDateFormat
import java.util.*

class AddPhotoActivity : AppCompatActivity() {
    var PICK_IMAGE_FROM_ALBUM = 0
    var storage: FirebaseStorage? = null
    var photoUri: Uri? = null
    var auth: FirebaseAuth? = null
    var firestore: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)
        MultiDex.install(this)

        //initiate storage - 스토리지 초기화
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        //Open the album - 액티비티를 실행하자마자 화면을 오픈해주는 코드
        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)

        //add image upload event - 버튼에 이벤트를 넣어주도록 한다.
        addphoto_btn_upload.setOnClickListener {
            contentUpload()
        }
    }

    //선택한 이미지를 받는 부분
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_FROM_ALBUM) {
            if (resultCode == Activity.RESULT_OK) {
                //사진을 선택했을때, 이미지의 경로가 이쪽으로 온다.
                photoUri = data?.data
                addphoto_image.setImageURI(photoUri)
            } else {
                //취소버튼을 눌렀을 경우. 피니시로 액티비티를 닫는다.
                finish()
            }
        }
    }

    fun contentUpload() {
        //파일이름을 만들어주는 코드
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_" + timestamp + "_.png"

        var storageRef = storage?.reference?.child("images")?.child(imageFileName)

        //promise method - 프로미스방식 (구글에서는 프로미스방식을 권장한다.)
        storageRef?.putFile(photoUri!!)?.continueWithTask { task: Task<UploadTask.TaskSnapshot> ->
            return@continueWithTask storageRef.downloadUrl
        }?.addOnSuccessListener { uri ->
            var contentDTO = ContentDTO()

            //insert downloadUrl of image
            contentDTO.imageUrl = uri.toString()

            //insert uid of user
            contentDTO.uid = auth?.currentUser?.uid

            //insert userId
            contentDTO.userId = auth?.currentUser?.email

            //insert explain of content - 사용자가 입력한 설명그
            contentDTO.explain = addphoto_edit_explain.text.toString()

            //insert timestamp
            contentDTO.timestamp = System.currentTimeMillis()

            firestore?.collection("images")?.document()?.set(contentDTO)

            setResult(Activity.RESULT_OK)
            //업로드 완료시 피니시로 창을 닫는다.
            finish()
        }

        //사진 업로드 , 데이터 베이스 입력 Callback method - 콜백방식
        /*storageRef?.putFile(photoUri!!)?.addOnSuccessListener {
            //결과 성공시 메시지
            //Toast.makeText(this,getString(R.string.upload_success),Toast.LENGTH_LONG).show()

            //이미지 업로드 완료시, 이미지 주소를 받는 코드
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                var contentDTO = ContentDTO()

                //insert downloadUrl of image
                contentDTO.imageUrl = uri.toString()

                //insert uid of user
                contentDTO.uid = auth?.currentUser?.uid

                //insert userId
                contentDTO.userId = auth?.currentUser?.email

                //insert explain of content - 사용자가 입력한 설명그
                contentDTO.explain = addphoto_edit_explain.text.toString()

                //insert timestamp
                contentDTO.timestamp = System.currentTimeMillis()

                firestore?.collection("images")?.document()?.set(contentDTO)

                setResult(Activity.RESULT_OK)
                //업로드 완료시 피니시로 창을 닫는다.
                finish()
            }
        }*/
    }
}