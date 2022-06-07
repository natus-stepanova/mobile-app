import Foundation

final class PanModalPresenter: ObservableObject {
    private let sourcelessRouter: SourcelessRouter

    init(sourcelessRouter: SourcelessRouter) {
        self.sourcelessRouter = sourcelessRouter
    }

    func presentPanModal(_ panModal: PanModalPresentableViewController) {
        guard let currentPresentedViewController = self.sourcelessRouter.currentPresentedViewController() else {
            return
        }

        currentPresentedViewController.presentIfPanModalWithCustomModalPresentationStyle(panModal)
    }
}