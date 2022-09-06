package dev.psuchanek.endurancepacecalculator

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.psuchanek.endurancepacecalculator.databinding.ActivityMainBinding
import dev.psuchanek.endurancepacecalculator.utils.handleSoftKeyboard

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            handleSoftKeyboard(this, currentFocus!!)
            currentFocus!!.clearFocus()

        }
        return super.dispatchTouchEvent(ev)
    }


}


