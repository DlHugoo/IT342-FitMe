package com.example.myapplication.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class WorkoutDay(
    val dayId: Long,
    val dayNumber: Int,
    @SerializedName("restDay") val isRestDay: Boolean = false, // Correctly map "restDay" from JSON
    val workout: Workout?
    //val exercises: List<WorkoutDayExercise>?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readParcelable(Workout::class.java.classLoader),
        //parcel.readArrayList(WorkoutDayExercise::class.java.classLoader) as List<WorkoutDayExercise>?
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(dayId)
        parcel.writeInt(dayNumber)
        parcel.writeByte(if (isRestDay) 1 else 0)
        parcel.writeParcelable(workout, flags)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<WorkoutDay> {
        override fun createFromParcel(parcel: Parcel): WorkoutDay = WorkoutDay(parcel)
        override fun newArray(size: Int): Array<WorkoutDay?> = arrayOfNulls(size)
    }
}