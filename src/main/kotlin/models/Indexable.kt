package models

interface Indexable {
    fun getId(): String
    companion object{
        public const val ID_Property = "v_ID";
    }
}