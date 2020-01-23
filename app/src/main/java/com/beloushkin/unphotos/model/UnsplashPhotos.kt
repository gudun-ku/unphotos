package com.beloushkin.unphotos.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Photo(
    val id: String?,
    val description: String?,
    @SerializedName("urls")
    val url: PhotoUrl?,
    val user: User?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(PhotoUrl::class.java.classLoader),
        parcel.readParcelable(User::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(description)
        parcel.writeParcelable(url, flags)
        parcel.writeParcelable(user, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Photo> {
        override fun createFromParcel(parcel: Parcel): Photo {
            return Photo(parcel)
        }

        override fun newArray(size: Int): Array<Photo?> {
            return arrayOfNulls(size)
        }
    }
}

data class PhotoUrl(
    val full: String?,
    val regular: String?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(full)
        parcel.writeString(regular)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotoUrl> {
        override fun createFromParcel(parcel: Parcel): PhotoUrl {
            return PhotoUrl(parcel)
        }

        override fun newArray(size: Int): Array<PhotoUrl?> {
            return arrayOfNulls(size)
        }
    }

}

data class User(
    val id: String?,
    val username: String?,
    @SerializedName("profile_image")
    val profileImage: ProfileImage?
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(ProfileImage::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(username)
        parcel.writeParcelable(profileImage, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}

data class ProfileImage(
    val small: String?
):Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(small)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfileImage> {
        override fun createFromParcel(parcel: Parcel): ProfileImage {
            return ProfileImage(parcel)
        }

        override fun newArray(size: Int): Array<ProfileImage?> {
            return arrayOfNulls(size)
        }
    }

}

