package com.example.menus7.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import com.example.menus7.data.User
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Locale

object FileUtils {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private const val APP_FOLDER = "IronWalk"

    fun saveUserToFile(context: Context, user: User) {
        try {
            val directory = getStorageDirectory(context, "users")
            val file = File(directory, "user_${user.id}.json")
            
            FileWriter(file).use { writer ->
                val userJson = gson.toJson(user)
                writer.write(userJson)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getStorageDirectory(context: Context, subdirectory: String): File {
        // For Android 10 (API 29) and above, use app-specific directory
        val baseDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File(context.getExternalFilesDir(null), APP_FOLDER)
        } else {
            // For older versions, use external storage
            File(Environment.getExternalStorageDirectory(), APP_FOLDER)
        }

        if (!baseDir.exists()) {
            baseDir.mkdirs()
        }

        val subDir = File(baseDir, subdirectory)
        if (!subDir.exists()) {
            subDir.mkdirs()
        }

        return subDir
    }

    fun getFilePath(context: Context, type: String): String {
        val baseDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.getExternalFilesDir(null)
        } else {
            Environment.getExternalStorageDirectory()
        }
        return "$baseDir/$APP_FOLDER/$type"
    }
} 