import shared
import SwiftUI

final class StepQuizViewModel: FeatureViewModel<
  StepQuizFeatureState,
  StepQuizFeatureMessage,
  StepQuizFeatureActionViewAction
> {
    private let step: Step

    private let viewDataMapper: StepQuizViewDataMapper

    init(step: Step, viewDataMapper: StepQuizViewDataMapper, feature: Presentation_reduxFeature) {
        self.step = step
        self.viewDataMapper = viewDataMapper
        super.init(feature: feature)
    }

    func loadAttempt(forceUpdate: Bool = false) {
        self.onNewMessage(StepQuizFeatureMessageInitWithStep(step: self.step, forceUpdate: forceUpdate))
    }

    func syncReply(_ reply: Reply) {
        self.onNewMessage(StepQuizFeatureMessageSyncReply(reply: reply))
    }

    func doMainQuizAction() {
        guard let attemptLoadedState = self.state as? StepQuizFeatureStateAttemptLoaded,
              let submissionLoadedState = attemptLoadedState.submissionState as? StepQuizFeatureSubmissionStateLoaded,
              let reply = submissionLoadedState.submission.reply else {
            return
        }

        self.onNewMessage(StepQuizFeatureMessageCreateSubmissionClicked(step: self.step, reply: reply))
    }

    func makeViewData() -> StepQuizViewData {
        self.viewDataMapper.mapStepToViewData(self.step)
    }
}

// MARK: - StepQuizViewModel: StepQuizChildQuizDelegate -

extension StepQuizViewModel: StepQuizChildQuizDelegate {
    func handleChildQuizSync(reply: Reply) {
        self.syncReply(reply)
    }

    func handleChildQuizSubmit(reply: Reply) {
        self.doMainQuizAction()
    }
}

extension Reply {
    convenience init(
        sortingChoices: [Bool]? = nil,
        tableChoices: [TableChoiceAnswer]? = nil,
        text: String? = nil,
        attachments: [Attachment]? = nil,
        formula: String? = nil,
        number: String? = nil,
        ordering: [Int]? = nil,
        language: String? = nil,
        code: String? = nil,
        blanks: [String]? = nil,
        solveSql: String? = nil
    ) {
        let choicesAnswer: [ChoiceAnswer]? = {
            if let sortingChoices = sortingChoices {
                return sortingChoices.map(ChoiceAnswerChoice.init(boolValue:))
            } else if let tableChoices = tableChoices {
                return tableChoices.map(ChoiceAnswerTable.init(tableChoice:))
            }
            return nil
        }()

        self.init(
            choices: choicesAnswer,
            text: text,
            attachments: attachments,
            formula: formula,
            number: number,
            ordering: ordering?.map({ KotlinInt(value: Int32($0)) }),
            language: language,
            code: code,
            blanks: blanks,
            solveSql: solveSql
        )
    }
}

extension Dataset {
    convenience init(
        options: [String]? = nil,
        pairs: [Pair]? = nil,
        rows: [String]? = nil,
        columns: [String]? = nil,
        desc: String? = nil,
        components: [Component]? = nil,
        isMultipleChoice: Bool = false,
        isCheckbox: Bool = false,
        isHtmlEnabled: Bool = false
    ) {
        self.init(
            options: options,
            pairs: pairs,
            rows: rows,
            columns: columns,
            description: desc,
            components: components,
            isMultipleChoice: isMultipleChoice,
            isCheckbox: isCheckbox,
            isHtmlEnabled: isHtmlEnabled
        )
    }
}
