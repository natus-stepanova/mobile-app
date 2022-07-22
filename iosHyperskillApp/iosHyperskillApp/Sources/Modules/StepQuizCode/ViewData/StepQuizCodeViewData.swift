import Foundation

struct StepQuizCodeViewData {
    let language: CodeLanguage?

    let code: String?
    let codeTemplate: String?

    let samples: [Sample]

    let executionTimeLimit: String?
    let executionMemoryLimit: String?

    let stepText: String
    let stepStats: String

    struct Sample: Hashable {
        let inputTitle: String
        let inputValue: String

        let outputTitle: String
        let outputValue: String
    }
}
