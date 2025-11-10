package com.example.futurescript2
import com.example.futurescript.R

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class MessageNotificationFragment : Fragment() {

    private lateinit var envelopeImage: ImageView
    private lateinit var openButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_message_notification, container, false)
        envelopeImage = view.findViewById(R.id.image_envelope)
        openButton = view.findViewById(R.id.button_read_letter
)

        // Gentle flap animation while idle
        val idleAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.wing_flap)
        envelopeImage.startAnimation(idleAnim)

        openButton.setOnClickListener {
            playOpenAnimation()
        }

        return view
    }

    private fun playOpenAnimation() {
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.envelope_open)
        envelopeImage.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                findNavController().navigate(R.id.action_messageNotification_to_viewLetter)
            }
        })
    }
}
