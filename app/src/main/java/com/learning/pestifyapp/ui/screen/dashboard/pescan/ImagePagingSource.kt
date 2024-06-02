package com.learning.pestifyapp.ui.screen.dashboard.pescan

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.paging.PagingSource
import androidx.paging.PagingState
import android.util.Log

class ImagePagingSource(
    private val context: Context
) : PagingSource<Int, Uri>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Uri> {
        return try {
            val page = params.key ?: 0
            val pageSize = params.loadSize
            val images = loadImagesFromGallery(context, page * pageSize, pageSize)
            Log.d("ImagePagingSource", "Loaded ${images.size} images for page $page")
            LoadResult.Page(
                data = images,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (images.size < pageSize) null else page + 1
            )
        } catch (e: Exception) {
            Log.e("ImagePagingSource", "Error loading images", e)
            LoadResult.Error(e)
        }
    }

    private fun loadImagesFromGallery(context: Context, offset: Int, limit: Int): List<Uri> {
        val uris = mutableListOf<Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            if (cursor.count > offset) {
                cursor.moveToPosition(offset - 1)
                var count = 0
                while (cursor.moveToNext() && count < limit) {
                    val id = cursor.getLong(idColumn)
                    val contentUri = Uri.withAppendedPath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id.toString()
                    )
                    uris.add(contentUri)
                    count++
                }
            }
        }
        return uris
    }

    override fun getRefreshKey(state: PagingState<Int, Uri>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
