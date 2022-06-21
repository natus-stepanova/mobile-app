import Foundation
import shared

enum StepQuizChildQuizType {
    case choice
    case matching
    case sorting
    case table
    case unsupported(blockName: String)

    init(blockName: String) {
        switch blockName {
        case BlockName.shared.CHOICE:
            self = .choice
        case BlockName.shared.MATCHING:
            self = .matching
        case BlockName.shared.SORTING:
            self = .sorting
        case BlockName.shared.TABLE:
            self = .table
        default:
            self = .unsupported(blockName: blockName)
        }
    }
}
