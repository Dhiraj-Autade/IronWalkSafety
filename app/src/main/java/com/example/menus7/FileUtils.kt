package com.example.menus7

import android.content.Context
import android.os.Build
import android.os.Environment
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

object FileUtils {
    private const val APP_FOLDER = "IronWalk"
    private const val USERS_FOLDER = "users"

    fun saveUserDetails(context: Context, fileName: String, content: String) {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val formattedFileName = "${fileName}_$timestamp.txt"
        saveFile(context, USERS_FOLDER, formattedFileName, content)
    }

    private fun saveFile(context: Context, subFolder: String, fileName: String, content: String) {
        try {
            val baseDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // For Android 10 and above, use app-specific directory
                File(context.getExternalFilesDir(null), APP_FOLDER)
            } else {
                // For Android 9 and below, use external storage
                File(Environment.getExternalStorageDirectory(), APP_FOLDER)
            }

            // Create app folder if it doesn't exist
            if (!baseDir.exists()) {
                baseDir.mkdirs()
            }

            // Create sub folder (users)
            val subDir = File(baseDir, subFolder)
            if (!subDir.exists()) {
                subDir.mkdirs()
            }

            // Create and write to file
            val file = File(subDir, fileName)
            FileWriter(file).use { writer ->
                writer.write("Timestamp: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())}\n")
                writer.write("----------------------------------------\n")
                writer.write(content)
                writer.write("\n----------------------------------------\n")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getFilePath(context: Context, subFolder: String, fileName: String): String {
        val baseDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File(context.getExternalFilesDir(null), APP_FOLDER)
        } else {
            File(Environment.getExternalStorageDirectory(), APP_FOLDER)
        }
        return File(File(baseDir, subFolder), fileName).absolutePath
    }
} 