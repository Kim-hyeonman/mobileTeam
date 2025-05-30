package com.example.mobileteam

import com.google.firebase.Firebase
import com.google.android.gms.tasks.Task
import com.google.firebase.app
import com.google.firebase.functions.FirebaseFunctions

// ...
val region = "asia-northeast1" // Replace with the actual supported region you want to use

// Get the FirebaseFunctions instance for the specific app and region
val functions = FirebaseFunctions.getInstance(app=Firebase.app, region)

fun helloWorlds(text: String): Task<String> {
    // Create the arguments to the callable function.
    val data = hashMapOf(
        "text" to text,
        "push" to true,
    )

    return functions
        .getHttpsCallable("helloWorlds")
        .call(data)
        .continueWith { task ->
            // This continuation runs on either success or failure, but if the task
            // has failed then result will throw an Exception which will be
            // propagated down.
            val result = task.result?.data as String
            result
        }
}