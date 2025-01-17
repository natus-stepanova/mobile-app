import UIKit

protocol CodeEditorViewDelegate: AnyObject {
    func codeEditorViewDidChange(_ codeEditorView: CodeEditorView)
    func codeEditorView(_ codeEditorView: CodeEditorView, beginEditing editing: Bool)
    func codeEditorViewDidBeginEditing(_ codeEditorView: CodeEditorView)
    func codeEditorViewDidEndEditing(_ codeEditorView: CodeEditorView)
    func codeEditorViewDidRequestSuggestionPresentationController(_ codeEditorView: CodeEditorView) -> UIViewController?
}

extension CodeEditorViewDelegate {
    func codeEditorViewDidChange(_ codeEditorView: CodeEditorView) {}

    func codeEditorView(_ codeEditorView: CodeEditorView, beginEditing editing: Bool) {}

    func codeEditorViewDidBeginEditing(_ codeEditorView: CodeEditorView) {}

    func codeEditorViewDidEndEditing(_ codeEditorView: CodeEditorView) {}

    func codeEditorViewDidRequestSuggestionPresentationController(
        _ codeEditorView: CodeEditorView
    ) -> UIViewController? {
        nil
    }
}
