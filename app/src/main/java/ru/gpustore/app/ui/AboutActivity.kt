package ru.gpustore.app.ui

import android.os.Bundle
import ru.gpustore.app.util.enableDarkEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import ru.gpustore.app.databinding.ActivityAboutBinding
import ru.gpustore.app.util.applySystemBarsPadding

/** Экран "О себе": статичная информация об авторе и о проекте, данные берутся из strings.xml */
class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableDarkEdgeToEdge()
        val binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarsPadding()
        binding.toolbar.setNavigationOnClickListener { finish() }
    }
}
