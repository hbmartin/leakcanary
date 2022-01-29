/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.leakcanary

import android.app.Application
import android.app.Dialog
import android.app.Service
import android.os.StrictMode
import android.view.View
import com.facebook.flipper.plugins.inspector.DescriptorMapping

import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin

import com.facebook.flipper.android.AndroidFlipperClient

import com.facebook.flipper.core.FlipperClient

import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.leakcanary2.FlipperLeakListener
import com.facebook.flipper.plugins.leakcanary2.LeakCanary2FlipperPlugin

import com.facebook.soloader.SoLoader
import leakcanary.LeakCanary


open class ExampleApplication : Application() {
  val leakedViews = mutableListOf<View>()
  val leakedDialogs = mutableListOf<Dialog>()
  val leakedServices = mutableListOf<Service>()

  override fun onCreate() {
    super.onCreate()
    SoLoader.init(this, false)

    LeakCanary.config = LeakCanary.config.copy(
      onHeapAnalyzedListener = FlipperLeakListener()
    )

    val client = AndroidFlipperClient.getInstance(this)
    client.addPlugin(LeakCanary2FlipperPlugin())
    client.start()
  }

  private fun enabledStrictMode() {
    StrictMode.setThreadPolicy(
      StrictMode.ThreadPolicy.Builder()
        .detectAll()
        .penaltyLog()
        .penaltyDeath()
        .build()
    )
  }
}
