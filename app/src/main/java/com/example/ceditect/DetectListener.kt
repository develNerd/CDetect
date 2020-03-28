package com.example.ceditect

interface DetectListener {
    fun onStarted()
    fun onSuccess(labelValue:String)
    fun onFailure(error:String)
    fun onModelDownloadComplete(successMessage:String)
}