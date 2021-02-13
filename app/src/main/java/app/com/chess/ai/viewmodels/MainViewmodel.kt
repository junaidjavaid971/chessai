package app.com.chess.ai.viewmodels

import android.app.Application
import android.view.View
import app.com.chess.ai.R

class MainViewmodel(application: Application) : BaseViewModel(application) {
    fun onVisualizationClicked(v: View) {
        when (v.id) {
            R.id.layout_visualization -> {
                viewClickedLiveData.postValue(R.id.layout_visualization)
            }
            R.id.layout_endGames -> {
                viewClickedLiveData.postValue(R.id.layout_endGames)
            }
            R.id.layout_tactics -> {
                viewClickedLiveData.postValue(R.id.layout_tactics)
            }
            R.id.layout_openings -> {
                viewClickedLiveData.postValue(R.id.layout_openings)
            }
        }
    }

    fun onOptionSelected(v: View) {
        when (v.id) {
            R.id.layout_training -> {
                viewClickedLiveData.postValue(R.id.layout_training)
            }
            R.id.layout_levels -> {
                viewClickedLiveData.postValue(R.id.layout_levels)
            }
        }
    }
}