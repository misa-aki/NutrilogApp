package br.edu.ufam.nutrilogapp.repository

import android.graphics.Bitmap
import br.edu.ufam.nutrilogapp.model.MealAnalysis
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GeminiRepository {

    // IMPORTANT: In a real app, use BuildConfig.GEMINI_API_KEY
    // For this task, I'll assume the user will provide it or I'll use a placeholder.
    // Since I cannot change local.properties easily without user input, I will use a placeholder
    // and handle the error gracefully or ask the user.
    // private val apiKey = BuildConfig.GEMINI_API_KEY
    private val apiKey = "AIzaSyDnf_Q5UxCFMWc7I-4L9DF1Z0xYecl3pHI" // TODO: Replace with actual key or BuildConfig

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = apiKey
    )

    fun analyzeMeal(bitmap: Bitmap): Flow<Result<MealAnalysis>> = flow {
        try {
            val inputContent = content {
                image(bitmap)
                text("Analyze this image of a meal. Identify the meal name and provide a short nutritional description (calories, main ingredients). Format the output as 'Name: [Meal Name] | Description: [Description]'. Answers in brazilian portuguese")
            }

            val response = generativeModel.generateContent(inputContent)
            val text = response.text

            if (text != null) {
                // Simple parsing logic based on the requested format
                val parts = text.split("|")
                if (parts.size >= 2) {
                    val name = parts[0].replace("Name:", "").trim()
                    val description = parts[1].replace("Description:", "").trim()
                    emit(Result.success(MealAnalysis(name, description)))
                } else {
                    // Fallback if format is not exact
                    emit(Result.success(MealAnalysis("Refeição Identificada", text)))
                }
            } else {
                emit(Result.failure(Exception("No response text from Gemini")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}
