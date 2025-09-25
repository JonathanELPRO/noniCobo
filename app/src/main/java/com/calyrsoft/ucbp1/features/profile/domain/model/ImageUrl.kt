package com.calyrsoft.ucbp1.features.profile.domain.model

@JvmInline
value class ImageUrl private constructor(val value: String) {
    companion object {
        fun create(raw: String): ImageUrl {
            val normalized = raw.trim().lowercase()

            require(normalized.startsWith("http://") || normalized.startsWith("https://")) {
                "Image URL must start with http/https"
            }

            return ImageUrl(normalized)
        }
    }

    override fun toString(): String = value
}
