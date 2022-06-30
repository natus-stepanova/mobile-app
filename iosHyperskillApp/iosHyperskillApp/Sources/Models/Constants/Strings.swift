import Foundation
import shared

enum Strings {
    private static let sharedStrings = SharedResources.strings.shared

    // MARK: - General -

    enum General {
        static let connectionError = sharedStrings.connection_error.localized()
    }

    // MARK: - TabBar -

    enum TabBar {
        static let home = sharedStrings.tab_bar_home_title.localized()
        static let track = sharedStrings.tab_bar_track_title.localized()
        static let profile = sharedStrings.tab_bar_profile_title.localized()
    }

    // MARK: - AuthSocial -

    enum AuthSocial {
        static let logInTitle = sharedStrings.auth_log_in_title.localized()
        static let jetBrainsAccount = sharedStrings.auth_jetbrains_account_text.localized()
        static let googleAccount = sharedStrings.auth_google_account_text.localized()
        static let gitHubAccount = sharedStrings.auth_github_account_text.localized()
        static let appleAccount = sharedStrings.auth_apple_account_text.localized()
        static let emailText = sharedStrings.auth_email_text.localized()
    }

    // MARK: - AuthCredentials -

    enum AuthCredentials {
        static let socialText = sharedStrings.auth_credentials_social_text.localized()
        static let resetPassword = sharedStrings.auth_credentials_reset_password_text.localized()
        static let logIn = sharedStrings.auth_credentials_log_in_text.localized()
        static let emailPlaceholder = sharedStrings.auth_credentials_email_placeholder.localized()
        static let passwordPlaceholder = sharedStrings.auth_credentials_password_placeholder.localized()
    }

    // MARK: - Register -

    enum Register {
        static let title = sharedStrings.register_title.localized()
        static let introText = sharedStrings.register_intro_text.localized()
        static let buttonText = sharedStrings.register_button_text.localized()
        static let possibilityText = sharedStrings.register_possibility_text.localized()
        static let callText = sharedStrings.register_call_text.localized()
    }

    // MARK: - Step -

    enum Step {
        static let startPracticing = sharedStrings.step_start_practicing_text.localized()
    }

    // MARK: - StepQuiz -

    enum StepQuiz {
        static let quizStatusCorrect = sharedStrings.step_quiz_status_correct_text.localized()
        static let quizStatusWrong = sharedStrings.step_quiz_status_wrong_text.localized()
        static let quizStatusEvaluation = sharedStrings.step_quiz_status_evaluation_text.localized()
        static let feedbackTitle = sharedStrings.step_quiz_feedback_title.localized()
        static let hintButton = sharedStrings.step_quiz_hint_button_text.localized()
        static let continueButton = sharedStrings.step_quiz_continue_button_text.localized()
        static let retryButton = sharedStrings.step_quiz_retry_button_text.localized()
        static let sendButton = sharedStrings.step_quiz_send_button_text.localized()
        static let checkingButton = sharedStrings.step_quiz_checking_button_text.localized()
        static let discussionsButton = sharedStrings.step_quiz_discussions_button_text.localized()
        static let unsupportedText = sharedStrings.step_quiz_unsupported_quiz_text.localized()
    }

    // MARK: - StepQuizChoice -

    enum StepQuizChoice {
        static let singleChoiceTitle = sharedStrings.step_quiz_choice_single_choice_title.localized()
        static let multipleChoiceTitle = sharedStrings.step_quiz_choice_multiple_choice_title.localized()
    }

    // MARK: - StepQuizTable -

    enum StepQuizTable {
        static let singleChoicePrompt = sharedStrings.step_quiz_table_single_choice.localized()
        static let multipleChoicePrompt = sharedStrings.step_quiz_table_multiple_choice.localized()
        static let confirmButton = sharedStrings.step_quiz_table_confirm_choice.localized()
    }

    // MARK: - Placeholder -

    enum Placeholder {
        static let networkErrorTitle = sharedStrings.placeholder_network_error_title.localized()
        static let networkErrorButtonText = sharedStrings.placeholder_network_error_button_text.localized()
    }

    // MARK: - StepQuizSorting -

    enum StepQuizSorting {
        static let title = sharedStrings.step_quiz_sorting_title.localized()
    }

    // MARK: - StepQuizString -

    enum StepQuizString {
        static let placeholder = sharedStrings.step_quiz_text_field_hint.localized()
    }

    // MARK: - Home -

    enum Home {
        static let title = sharedStrings.home_title.localized()
    }

    // MARK: - Track -

    enum Track {
        static let title = sharedStrings.track_title.localized()
    }

    // MARK: - Profile -

    enum Profile {
        static let title = sharedStrings.profile_title.localized()
    }

    // MARK: - Settings -

    enum Settings {
        static let title = sharedStrings.settings_title.localized()
    }
}
