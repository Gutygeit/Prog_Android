package fr.uha.hassenforder.android.ui.field

data class FieldWrapper<T>(
    val value: T? = null,
    val errorId: Int? = null
)
