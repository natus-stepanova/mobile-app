import shared
import SwiftUI

class TopicsRepetitionsAssembly: UIKitAssembly {
    private let recommendedRepetitionsCount: Int32

    init(recommendedRepetitionsCount: Int32) {
        self.recommendedRepetitionsCount = recommendedRepetitionsCount
    }

    func makeModule() -> UIViewController {
        let topicsRepetitionsComponent = AppGraphBridge.sharedAppGraph.buildTopicsRepetitionsComponent()

        let viewModel = TopicsRepetitionsViewModel(
            recommendedRepetitionsCount: recommendedRepetitionsCount,
            feature: topicsRepetitionsComponent.topicsRepetitionsFeature
        )

        let pushRouter = SwiftUIPushRouter()

        let topicsRepetitionsView = TopicsRepetitionsView(
            viewModel: viewModel,
            pushRouter: pushRouter,
            dataMapper: topicsRepetitionsComponent.topicsRepetitionsViewDataMapper
        )

        let viewController = RemoveBackButtonTitleHostingController(rootView: topicsRepetitionsView)

        pushRouter.rootViewController = viewController

        return viewController
    }
}