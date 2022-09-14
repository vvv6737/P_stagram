package com.SNS.p_stagram.navigation.model

data class ContentDTO(
    var explain: String? = null, //컨텐츠의 설명
    var imageUrl: String? = null, //이미지 주소
    var uid: String? = null, //유저 아이디
    var userId: String? = null, //올린 유저의 이미지
    var timestamp: Long? = null, //몇시, 몇분에 컨텐츠를 올렸는지 관리
    var favoriteCount: String? = null, //좋아요 수
    var favorites: Map<String, Boolean> = HashMap() //중복 좋아요 방지
) {
    data class Comment( //댓글
        var uid: String? = null, //누가 달았는지
        var userId: String? = null, //이메일
        var comment: String? = null, //댓글
        var timestamp: Long? = null //몇시, 몇분에 댓글을 달았는지,
    )
}