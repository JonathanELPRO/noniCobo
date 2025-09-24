package com.calyrsoft.ucbp1.features.dollar.domain.model

@JvmInline
value class UrlPath constructor(val value: String) {

    companion object {
        fun create(raw: String): UrlPath {
            val normalized = raw.trim().lowercase()

            require(normalized.isNotEmpty()) {
                "UrlPath must not be empty"
            }
            require(normalized.startsWith("https://")) {
                "UrlPath must start with 'https'"
            }

            return UrlPath(normalized)
        }
    }

    override fun toString(): String = value
}
