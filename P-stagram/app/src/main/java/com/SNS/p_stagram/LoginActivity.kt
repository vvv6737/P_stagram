package com.SNS.p_stagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        email_login_button.setOnClickListener {
            signinAndSignup()
        }
    }

    //아이디를 만드는 코드
    fun signinAndSignup() {
        auth?.createUserWithEmailAndPassword(
            email_edittext.text.toString(),
            password_edittext.text.toString()
        )
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //아이디가 성공적으로 생성이 될때, 생성 할 코드를 입력해주는 부분
                    moveMainPage(task.result?.user)
                } else if (task.exception?.message.isNullOrEmpty()) {
                    //로그인 에러가 났을경우, 에러메시지
                    Toast.makeText(this, task.exception?.message,Toast.LENGTH_LONG).show()
                } else {
                    //회원가입도 아니고, 에러메시지도 아닐시, 로그인 화면으로 이동
                    signinEmail()
                }
            }
    }

    //로그인하는 코드
    fun signinEmail() {
        auth?.signInWithEmailAndPassword(
            email_edittext.text.toString(),
            password_edittext.text.toString()
        )
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //아이디와 패스워드가 일치할경우
                    moveMainPage(task.result?.user)
                } else {
                    //틀렸을 경우
                    Toast.makeText(this, task.exception?.message,Toast.LENGTH_LONG).show()
                }
            }
    }

    //로그인 성공시 메인으로 이동하는 코드
    fun moveMainPage(user:FirebaseUser?){
        if (user != null) {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}
