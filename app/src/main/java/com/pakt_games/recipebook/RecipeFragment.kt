package com.pakt_games.recipebook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_recipe.*


class RecipeFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSaveRecipeId.setOnClickListener(){
            btnSaveRecipe(it)
        }
        btnImageSelectionId.setOnClickListener()
        {
            btnImageSelection(it)
        }

    }

    fun btnSaveRecipe(view: View)
    {
    println("Button Çalışıyoru")
    }
    fun btnImageSelection(view: View)
    {
        println("İmage Ekleme Çalışıyor")
    }


}