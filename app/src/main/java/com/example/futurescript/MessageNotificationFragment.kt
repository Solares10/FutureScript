package com.example.futurescript

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_message_notification, container, false)

        // Connect UI elements
        envelopeImage = view.findViewById(R.id.image_envelope)
        openButton = view.findViewById(R.id.button_read_letter)

        // Start the idle floating animation
        val idleAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.envelope_idle)
        envelopeImage.startAnimation(idleAnim)

        // Handle button click → play open animation and navigate
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
                // Navigate to the next screen after animation completes
                //findNavController().navigate(R.id.action_messageNotification_to_viewLetter)
            }
        })
    }
}
