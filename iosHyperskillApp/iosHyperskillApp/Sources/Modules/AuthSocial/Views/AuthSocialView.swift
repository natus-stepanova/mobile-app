import shared
import SwiftUI

extension AuthSocialView {
    struct Appearance {
        let logoSize: CGFloat = 48
    }
}

struct AuthSocialView: View {
    let appearance: Appearance

    @ObservedObject private var viewModel: AuthSocialViewModel

    init(viewModel: AuthSocialViewModel, appearance: Appearance = Appearance()) {
        self.viewModel = viewModel
        self.appearance = appearance
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }

    var body: some View {
        let state = viewModel.state

        if state is AuthSocialFeatureStateLoading {
            ProgressHUD.show()
        } else if state is AuthSocialFeatureStateAuthenticated {
            ProgressHUD.showSuccess()
        }

        return AuthAdaptiveContentView { horizontalSizeClass in
            AuthLogoView(logoWidthHeight: appearance.logoSize)
                .padding(horizontalSizeClass == .regular ? .bottom : .vertical, appearance.logoSize)

            AuthSocialControlsView(
                socialAuthProviders: viewModel.availableSocialAuthProviders,
                onSocialAuthProviderClick: viewModel.signIn(with:),
                onContinueWithEmailClick: { print("presentingContinueWithEmail") }
            )
        }
        .onAppear(perform: viewModel.startListening)
        .onDisappear(perform: viewModel.stopListening)
    }

    // MARK: Private API

    private func handleViewAction(_ viewAction: AuthSocialFeatureActionViewAction) {
        if viewAction is AuthSocialFeatureActionViewActionShowAuthError {
            ProgressHUD.showError()
        }
    }
}

struct AuthView_Previews: PreviewProvider {
    static var previews: some View {
        AuthSocialAssembly()
            .makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))
            .preferredColorScheme(.light)

        if #available(iOS 15.0, *) {
            AuthSocialAssembly()
                .makeModule()
                .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))
                .preferredColorScheme(.light)
                .previewInterfaceOrientation(.landscapeRight)
        }

        AuthSocialAssembly().makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))
            .preferredColorScheme(.dark)

        AuthSocialAssembly().makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPhone SE (3rd generation)"))

        AuthSocialAssembly().makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPad (9th generation)"))
    }
}
