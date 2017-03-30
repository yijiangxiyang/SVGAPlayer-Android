package com.opensource.svgaplayer

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.File
import java.io.Serializable
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by PonyCui_Home on 16/6/18.
 */
class SVGAVideoEntity(obj: JSONObject, val cacheDir: File) {

    var videoSize = SVGARect(0.0, 0.0, 0.0, 0.0)
        private set

    var FPS = 15
        private set

    var frames: Int = 0
        private set

    var sprites: List<SVGAVideoSpriteEntity> = listOf()
        private set

    var images = HashMap<String, Bitmap>()
        private set

    init {
        obj.getJSONObject("movie")?.let {
            it.getJSONObject("viewBox")?.let {
                videoSize = SVGARect(0.0, 0.0, it.optDouble("width", 0.0), it.optDouble("height", 0.0))
            }
            FPS = it.optInt("fps", 20)
            frames = it.optInt("frames", 0)
        }
        resetImages(obj)
        resetSprites(obj)
    }

    internal fun resetImages(obj: JSONObject) {
        obj.getJSONObject("images")?.let {
            val imgObjects = it
            it.keys().forEach {
                val imageKey = it
                val filePath = cacheDir.absolutePath + "/" + imgObjects[imageKey] + ".png"
                BitmapFactory.decodeFile(filePath)?.let {
                    images.put(imageKey, it)
                }
            }
        }
    }

    internal fun resetSprites(obj: JSONObject) {
        val mutableList: MutableList<SVGAVideoSpriteEntity> = mutableListOf()
        obj.getJSONArray("sprites")?.let {
            for (i in 0..it.length() - 1) {
                it.getJSONObject(i)?.let {
                    mutableList.add(SVGAVideoSpriteEntity(it))
                }
            }
        }
        sprites = mutableList.toList()
    }

}
