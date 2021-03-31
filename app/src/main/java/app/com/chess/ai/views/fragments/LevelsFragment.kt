package app.com.chess.ai.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import app.com.chess.ai.R
import app.com.chess.ai.adapters.LevelsAdapter
import app.com.chess.ai.databinding.FragmentLevelsBinding
import app.com.chess.ai.models.global.Level

class LevelsFragment(val levelsArrayList: MutableList<Level>) : Fragment() {

    lateinit var binding: FragmentLevelsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_levels, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLevelsAdapter()
    }

    private fun initLevelsAdapter() {
        binding.rvLevels.layoutManager =
            GridLayoutManager(requireActivity(), 4)
        val adapter = LevelsAdapter(levelsArrayList, requireActivity())
        binding.rvLevels.adapter = adapter
    }

}