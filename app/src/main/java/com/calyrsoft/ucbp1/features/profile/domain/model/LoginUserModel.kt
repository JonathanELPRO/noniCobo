package com.calyrsoft.ucbp1.features.profile.domain.model

data class LoginUserModel(
    val name: Name,
    val email: Email,
    val password: Password,
    val phone: Phone,
    val imageUrl: ImageUrl,
    val summary: Summary
) {
    companion object {
        fun create(
            name: String,
            email: String,
            password: String,
            phone: String,
            imageUrl: String,
            summary: String
        ): LoginUserModel {
            return LoginUserModel(
                name = Name.create(name),
                email = Email.create(email),
                password = Password.create(password),
                phone = Phone.create(phone),
                imageUrl = ImageUrl.create(imageUrl),
                summary = Summary.create(summary)
            )
        }
    }
}
