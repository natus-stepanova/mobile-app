package org.hyperskill.app.android.profile.view.fragment

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.transform.CircleCropTransformation
import java.util.Locale
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.isChannelNotificationsEnabled
import org.hyperskill.app.android.databinding.FragmentProfileBinding
import org.hyperskill.app.android.notification.injection.PlatformNotificationComponent
import org.hyperskill.app.android.notification.model.HyperskillNotificationChannel
import org.hyperskill.app.android.profile_settings.view.dialog.ProfileSettingsDialogFragment
import org.hyperskill.app.android.streak.view.delegate.StreakCardFormDelegate
import org.hyperskill.app.android.view.base.ui.extension.redirectToUsernamePage
import org.hyperskill.app.core.domain.url.HyperskillUrlBuilder
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.presentation.ProfileFeature
import org.hyperskill.app.profile.presentation.ProfileViewModel
import org.hyperskill.app.profile.view.social_redirect.SocialNetworksRedirect
import org.hyperskill.app.streak.domain.model.Streak
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.argument
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class ProfileFragment :
    Fragment(R.layout.fragment_profile),
    ReduxView<ProfileFeature.State, ProfileFeature.Action.ViewAction>,
    TimeIntervalPickerDialogFragment.Companion.Callback {
    companion object {
        fun newInstance(profileId: Long? = null, isInitCurrent: Boolean = true): Fragment =
            ProfileFragment()
                .apply {
                    this.profileId = profileId ?: 0
                    this.isInitCurrent = isInitCurrent
                }
    }

    private var profileId: Long by argument()
    private var isInitCurrent: Boolean by argument()

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewBinding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)
    private val profileViewModel: ProfileViewModel by reduxViewModel(this) { viewModelFactory }
    private val viewStateDelegate: ViewStateDelegate<ProfileFeature.State> = ViewStateDelegate()

    private lateinit var streakFormDelegate: StreakCardFormDelegate

    private lateinit var profile: Profile
    private var streak: Streak? = null

    private val platformNotificationComponent: PlatformNotificationComponent = HyperskillApp.graph().platformNotificationComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewStateDelegate()
        viewBinding.profileError.tryAgain.setOnClickListener {
            profileViewModel.onNewMessage(ProfileFeature.Message.Init(profileId = profileId, forceUpdate = true, isInitCurrent = isInitCurrent))
        }

        viewBinding.profileSettingsButton.setOnClickListener {
            profileViewModel.onNewMessage(ProfileFeature.Message.ClickedSettingsEventMessage)
            ProfileSettingsDialogFragment
                .newInstance()
                .showIfNotExists(childFragmentManager, ProfileSettingsDialogFragment.TAG)
        }

        profileViewModel.onNewMessage(ProfileFeature.Message.Init(profileId = profileId, isInitCurrent = isInitCurrent))
        profileViewModel.onNewMessage(ProfileFeature.Message.ViewedEventMessage)
    }

    private fun injectComponents() {
        val profileComponent = HyperskillApp.graph().buildProfileComponent()
        val platformProfileComponent = HyperskillApp.graph().buildPlatformProfileComponent(profileComponent)
        viewModelFactory = platformProfileComponent.reduxViewModelFactory
    }

    private fun initViewStateDelegate() {
        with(viewStateDelegate) {
            addState<ProfileFeature.State.Idle>()
            addState<ProfileFeature.State.Loading>(viewBinding.profileProgress)
            addState<ProfileFeature.State.Error>(viewBinding.profileError.root)
            addState<ProfileFeature.State.Content>(viewBinding.profileContainer)
        }
    }

    override fun onAction(action: ProfileFeature.Action.ViewAction) {
        // no op
    }

    override fun render(state: ProfileFeature.State) {
        viewStateDelegate.switchState(state)
        when (state) {
            is ProfileFeature.State.Content -> {
                profileId = state.profile.id
                profile = state.profile
                streak = state.streak
                setupProfile()
            }
        }
    }

    private fun setupProfile() {
        if (streak != null) {
            streakFormDelegate = StreakCardFormDelegate(requireContext(), viewBinding.profileStreakLayout, streak!!)
        } else {
            viewBinding.profileStreakLayout.root.visibility = View.GONE
        }

        setupNameProfileBadge()
        setupRemindersSchedule()
        setupAboutMeSection()
        setupBioSection()
        setupExperienceSection()
        setupSocialButtons()
        setupProfileBrowserRedirect()
    }

    private fun setupNameProfileBadge() {
        val svgImageLoader = ImageLoader.Builder(requireContext())
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
        viewBinding.profileAvatarImageView.load(profile.avatar, svgImageLoader) {
            transformations(CircleCropTransformation())
        }
        viewBinding.profileNameTextView.text = profile.fullname

        if (profile.isStaff) {
            viewBinding.profileRoleTextView.text = resources.getString(R.string.profile_role_staff_text)
        } else {
            viewBinding.profileRoleTextView.text = resources.getString(R.string.profile_role_learner_text)
        }
    }

    private fun setupRemindersSchedule() {
        viewBinding.profileScheduleTextView.setOnClickListener {
            profileViewModel.onNewMessage(ProfileFeature.Message.ClickedDailyStudyRemindsTimeEventMessage)
            TimeIntervalPickerDialogFragment
                .newInstance()
                .showIfNotExists(childFragmentManager, TimeIntervalPickerDialogFragment.TAG)
        }
        val scheduleTime = platformNotificationComponent.notificationInteractor.getDailyStudyRemindersIntervalStartHour()
        viewBinding.profileDailyRemindersSwitchCompat.isChecked = platformNotificationComponent.notificationInteractor.isDailyStudyRemindersEnabled()

        viewBinding.profileScheduleTextView.text = requireContext().resources.getString(R.string.profile_daily_study_reminders_schedule_text) +
            " ${scheduleTime.toString().padStart(2, '0')}:00 - ${(scheduleTime + 1).toString().padStart(2, '0')}:00"

        val notificationManagerCompat = NotificationManagerCompat.from(requireContext())
        viewBinding.profileDailyRemindersSwitchCompat.isChecked =
            notificationManagerCompat.isChannelNotificationsEnabled(HyperskillNotificationChannel.DAILY_REMINDER.channelId) && platformNotificationComponent.notificationInteractor.isDailyStudyRemindersEnabled()

        viewBinding.profileScheduleTextView.isVisible = viewBinding.profileDailyRemindersSwitchCompat.isChecked

        viewBinding.profileDailyRemindersSwitchCompat.setOnCheckedChangeListener { _, isChecked ->
            profileViewModel.onNewMessage(ProfileFeature.Message.ClickedDailyStudyRemindsEventMessage(isChecked))
            platformNotificationComponent.notificationInteractor.setDailyStudyRemindersEnabled(isChecked)

            if (isChecked) {
                platformNotificationComponent.dailyStudyReminderNotificationDelegate.scheduleDailyNotification()

                if (!notificationManagerCompat.areNotificationsEnabled()) {
                    val intent: Intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                    startActivity(intent)
                    return@setOnCheckedChangeListener
                }

                if (!notificationManagerCompat.isChannelNotificationsEnabled(HyperskillNotificationChannel.DAILY_REMINDER.channelId)) {
                    val intent: Intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                        .putExtra(Settings.EXTRA_CHANNEL_ID, HyperskillNotificationChannel.DAILY_REMINDER.channelId)
                    startActivity(intent)
                    return@setOnCheckedChangeListener
                }
            }

            viewBinding.profileScheduleTextView.isVisible = isChecked
        }
    }

    private fun setupAboutMeSection() {
        if (profile.country != null) {
            viewBinding.profileAboutLivesTextView.text =
                "${resources.getString(R.string.profile_lives_in_text)} ${ Locale(Locale.ENGLISH.language, profile.country).displayCountry }"
        } else {
            viewBinding.profileAboutLivesTextView.visibility = View.GONE
        }

        if (profile.languages?.isEmpty() == false) {
            viewBinding.profileAboutSpeaksTextView.text =
                "${resources.getString(R.string.profile_speaks_text)} ${profile.languages!!.joinToString(", ") { Locale(it).getDisplayLanguage(Locale.ENGLISH) }}"
        } else {
            viewBinding.profileAboutSpeaksTextView.visibility = View.GONE
        }
    }

    private fun setupBioSection() {
        if (profile.bio != "") {
            viewBinding.profileAboutBioTextTextView.text = profile.bio
        } else {
            viewBinding.profileAboutBioTextTextView.visibility = View.GONE
            viewBinding.profileAboutBioBarTextView.visibility = View.GONE
        }
    }

    private fun setupExperienceSection() {
        if (profile.experience != "") {
            viewBinding.profileAboutExperienceTextTextView.text = profile.experience
        } else {
            viewBinding.profileAboutExperienceTextTextView.visibility = View.GONE
            viewBinding.profileAboutExperienceBarTextView.visibility = View.GONE
        }
    }

    private fun setupSocialButtons() {
        with(viewBinding) {
            if (profile.facebookUsername != "") {
                profileFacebookButton.setOnClickListener {
                    SocialNetworksRedirect.FACEBOOK.redirectToUsernamePage(requireContext(), profile.facebookUsername)
                }
            } else {
                profileFacebookButton.visibility = View.GONE
            }

            if (profile.twitterUsername != "") {
                profileTwitterButton.setOnClickListener {
                    SocialNetworksRedirect.TWITTER.redirectToUsernamePage(requireContext(), profile.twitterUsername)
                }
            } else {
                profileTwitterButton.visibility = View.GONE
            }

            if (profile.linkedinUsername != "") {
                profileLinkedinButton.setOnClickListener {
                    SocialNetworksRedirect.LINKEDIN.redirectToUsernamePage(requireContext(), profile.linkedinUsername)
                }
            } else {
                profileLinkedinButton.visibility = View.GONE
            }

            if (profile.redditUsername != "") {
                profileRedditButton.setOnClickListener {
                    SocialNetworksRedirect.REDDIT.redirectToUsernamePage(requireContext(), profile.redditUsername)
                }
            } else {
                profileRedditButton.visibility = View.GONE
            }

            if (profile.githubUsername != "") {
                profileGithubButton.setOnClickListener {
                    SocialNetworksRedirect.GITHUB.redirectToUsernamePage(requireContext(), profile.githubUsername)
                }
            } else {
                profileGithubButton.visibility = View.GONE
            }
        }
    }

    private fun setupProfileBrowserRedirect() {
        viewBinding.profileViewFullVersionTextView.paintFlags = viewBinding.profileViewFullVersionTextView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        viewBinding.profileViewFullVersionTextView.setOnClickListener {
            profileViewModel.onNewMessage(ProfileFeature.Message.ClickedViewFullProfileEventMessage)

            val intent = Intent(Intent.ACTION_VIEW)
            val url = HyperskillUrlBuilder.build(HyperskillUrlPath.Profile(profile.id))
            intent.data = Uri.parse(url.toString())

            startActivity(intent)
        }
    }

    override fun onTimeIntervalPicked(chosenInterval: Int) {
        platformNotificationComponent.notificationInteractor.setDailyStudyRemindersIntervalStartHour(chosenInterval)
        platformNotificationComponent.dailyStudyReminderNotificationDelegate.scheduleDailyNotification()

        viewBinding.profileScheduleTextView.text = requireContext().resources.getString(R.string.profile_daily_study_reminders_schedule_text) +
            " ${chosenInterval.toString().padStart(2, '0')}:00 - ${(chosenInterval + 1).toString().padStart(2, '0')}:00"
    }
}