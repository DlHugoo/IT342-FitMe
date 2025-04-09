package com.example.myapplication.model

import android.os.Parcel
import android.os.Parcelable

data class Workout(
    val workoutId: Long,
    val title: String,
    val difficulty: String,
    val days: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(workoutId)
        parcel.writeString(title)
        parcel.writeString(difficulty)
        parcel.writeInt(days)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Workout> {
        override fun createFromParcel(parcel: Parcel): Workout {
            return Workout(parcel)
        }

        override fun newArray(size: Int): Array<Workout?> {
            return arrayOfNulls(size)
        }
    }
}
