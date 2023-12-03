package com.painandpanic.blossombuddy.domain.usecase

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.painandpanic.blossombuddy.R
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.torchvision.TensorImageUtils
import java.math.RoundingMode

class ClassifyImageUseCase(
    private val context: Context,
    private val classifier: Module
) {

    operator fun invoke(imageId: Long): Triple<Bitmap,Int,Map<String,Float>> {
        val contentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val imageUri: Uri = ContentUris.withAppendedId(contentUri, imageId)

        val image = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.RGBA_F16, true)
        }


        val tensorImage = TensorImageUtils.bitmapToFloat32Tensor(
            image,
            floatArrayOf(0.5f,0.5f,0.5f),
            floatArrayOf(0.5f,0.5f,0.5f)
        )

        val outputTensor = classifier
            .forward(IValue.from(tensorImage))
            .toTensor()

        val scores = outputTensor?.dataAsFloatArray
        val maxScoreIdx = scores?.indices?.maxByOrNull { scores[it] } ?: -1
        val secondMaxScoreIdx = scores?.drop(maxScoreIdx)?.indices?.maxByOrNull { scores[it] } ?: -1
        val thirdMaxScoreIdx = scores?.drop(maxScoreIdx)?.drop(secondMaxScoreIdx)?.indices?.maxByOrNull { scores[it] } ?: -1
        val topThreeMaxScoreIndices = listOf(maxScoreIdx, secondMaxScoreIdx, thirdMaxScoreIdx)
        val labels = topThreeMaxScoreIndices.map { context.resources.getStringArray(R.array.flower_names)[it] }
        val preds = topThreeMaxScoreIndices.map { scores?.get(it) ?: 0f }
        val predLabels = labels.zip(preds.map { (
                (it - (scores?.minOrNull() ?: 0f)) / ((scores?.maxOrNull() ?: 1f) - (scores?.minOrNull() ?: 0f))
            ).toBigDecimal().setScale(4,RoundingMode.HALF_UP).toFloat()
        }).toMap()
        return Triple(image, maxScoreIdx, predLabels)
    }

    operator fun invoke(image: Bitmap): Triple<Bitmap,Int,Map<String,Float>> {
        val tensorImage = TensorImageUtils.bitmapToFloat32Tensor(
            image,
            floatArrayOf(0.5f,0.5f,0.5f),
            floatArrayOf(0.5f,0.5f,0.5f)
        )

        val outputTensor = classifier
            .forward(IValue.from(tensorImage))
            .toTensor()

        val scores = outputTensor?.dataAsFloatArray
        val maxScoreIdx = scores?.indices?.maxByOrNull { scores[it] } ?: -1
        val secondMaxScoreIdx = scores?.drop(maxScoreIdx)?.indices?.maxByOrNull { scores[it] } ?: -1
        val thirdMaxScoreIdx = scores?.drop(maxScoreIdx)?.drop(secondMaxScoreIdx)?.indices?.maxByOrNull { scores[it] } ?: -1
        val topThreeMaxScoreIndices = listOf(maxScoreIdx, secondMaxScoreIdx, thirdMaxScoreIdx)
        val labels = topThreeMaxScoreIndices.map { context.resources.getStringArray(R.array.flower_names)[it] }
        val preds = topThreeMaxScoreIndices.map { scores?.get(it) ?: 0f }
        val predLabels = labels.zip(preds.map { it.toBigDecimal().setScale(4,RoundingMode.HALF_UP).toFloat() }).toMap()
        return Triple(image, maxScoreIdx, predLabels)
    }

    companion object {
        const val ASSET_NAME = "model.ptl"
    }
}
