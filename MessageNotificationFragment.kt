val envelope = view.findViewById<ImageView>(R.id.image_envelope)
val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.envelope_open)

animation.setAnimationListener(object : Animation.AnimationListener {
    override fun onAnimationStart(animation: Animation) {}
    override fun onAnimationEnd(animation: Animation) {
        // Navigate to the next fragment after the envelope fades out
        findNavController().navigate(R.id.action_messageNotification_to_viewLetter)
    }
    override fun onAnimationRepeat(animation: Animation) {}
})

envelope.startAnimation(animation)
