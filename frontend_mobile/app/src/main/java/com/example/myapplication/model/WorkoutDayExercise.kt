package com.example.myapplication.model

import android.os.Parcel
import android.os.Parcelable

data class WorkoutDayExercise(
    val id: Long,
    val exerciseName: String?, // Match the backend's `exerciseName` field
    val reps: Int?,
    val duration: Int?,
    val gifUrl: String? // Add gifUrl field
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(exerciseName)
        parcel.writeValue(reps)
        parcel.writeValue(duration)
        parcel.writeString(gifUrl)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<WorkoutDayExercise> {
        override fun createFromParcel(parcel: Parcel): WorkoutDayExercise {
            return WorkoutDayExercise(parcel)
        }

        override fun newArray(size: Int): Array<WorkoutDayExercise?> {
            return arrayOfNulls(size)
        }
    }

}