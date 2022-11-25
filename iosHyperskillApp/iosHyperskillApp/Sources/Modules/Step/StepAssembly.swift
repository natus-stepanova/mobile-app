import shared
import SwiftUI

final class StepAssembly: UIKitAssembly {
    private let stepID: Int

    init(stepID: Int) {
        self.stepID = stepID
    }

    func makeModule() -> UIViewController {
        let commonComponent = AppGraphBridge.sharedAppGraph.commonComponent
        let stepComponent = AppGraphBridge.sharedAppGraph.buildStepComponent()

        let viewModel = StepViewModel(
            stepID: self.stepID,
            viewDataMapper: StepViewDataMapper(
                formatter: Formatter(resourceProvider: commonComponent.resourceProvider),
                resourceProvider: commonComponent.resourceProvider,
                commentThreadTitleMapper: stepComponent.commentThreadTitleMapper
            ),
            feature: stepComponent.stepFeature
        )

        let pushRouter = SwiftUIPushRouter()
        let modalRouter = SwiftUIModalRouter()
        let stepView = StepView(
            viewModel: viewModel,
            pushRouter: pushRouter,
            modalRouter: modalRouter
        )
        let hostingController = RemoveBackButtonTitleHostingController(rootView: stepView)

        pushRouter.rootViewController = hostingController
        modalRouter.rootViewController = hostingController

        return hostingController
    }
}
