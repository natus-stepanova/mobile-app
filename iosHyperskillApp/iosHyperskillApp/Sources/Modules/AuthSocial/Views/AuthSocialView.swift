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

    @ObservedObject private var navigationState: AppNavigationState

    @State private var presentingAuthWithEmail = false

    init(viewModel: AuthSocialViewModel, navigationState: AppNavigationState, appearance: Appearance = Appearance()) {
        self.viewModel = viewModel
        self.navigationState = navigationState
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

        return NavigationView {
            AuthAdaptiveContentView { horizontalSizeClass in
                AuthLogoView(logoWidthHeight: appearance.logoSize)
                    .padding(horizontalSizeClass == .regular ? .bottom : .vertical, appearance.logoSize)

                AuthSocialControlsView(
                    socialAuthProviders: viewModel.availableSocialAuthProviders,
                    onSocialAuthProviderClick: viewModel.signIn(with:),
                    onContinueWithEmailClick: { presentingAuthWithEmail = true }
                )
                NavigationLink(
                    isActive: $presentingAuthWithEmail,
                    destination: AuthCredentialsAssembly(navigationState: navigationState).makeModule,
                    label: { EmptyView() }
                )
            }
            .navigationBarHidden(true)
        }
        .onAppear(perform: viewModel.startListening)
        .onDisappear(perform: viewModel.stopListening)
        .navigationViewStyle(StackNavigationViewStyle())
    }

    // MARK: Private API

    private func handleViewAction(_ viewAction: AuthSocialFeatureActionViewAction) {
        switch viewAction {
        case is AuthSocialFeatureActionViewActionCompleteAuthFlow:
            withAnimation {
                navigationState.presentingAuthScreen = false
            }
        case let authError as AuthSocialFeatureActionViewActionShowAuthError:
            let errorText = viewModel.getAuthSocialErrorText(authSocialError: authError.socialError)
            ProgressHUD.showError(status: errorText)
        default:
            print("AuthSocialView :: unhandled viewAction = \(viewAction)")
        }
    }
}

struct AuthView_Previews: PreviewProvider {
    static var previews: some View {
        AuthSocialAssembly()
            .makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))

        AuthSocialAssembly().makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPhone SE (3rd generation)"))
            .preferredColorScheme(.dark)

        AuthSocialAssembly().makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPad (9th generation)"))
    }
}