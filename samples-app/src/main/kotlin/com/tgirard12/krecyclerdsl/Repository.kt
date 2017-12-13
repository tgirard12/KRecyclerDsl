package com.tgirard12.krecyclerdsl

data class Repositories(

        val total_count: Int,
        val incomplete_results: Boolean,
        val items: List<Repository>

) {
    data class Repository(
            val id: Long,
            val name: String,
            val full_name: String,
            val html_url: String,
            val description: String,
            val url: String,
            val language: String,
            val score: Float,
            val owner: Owner
    ) {
        data class Owner(
                val login: String,
                val avatar_url: String,
                val url: String)
    }
}