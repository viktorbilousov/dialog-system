package models

import com.beust.klaxon.Json

interface Indexable {
    val id : String
    companion object {
        const val ID_NAME = "item_id"
    }
}