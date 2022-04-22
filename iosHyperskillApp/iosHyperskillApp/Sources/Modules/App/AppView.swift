import shared
import SwiftUI

struct AppView: View {
    @ObservedObject private var viewModel: AppViewModel

    @StateObject private var navigationState = AppNavigationState()

    @Environment(\.colorScheme) private var colorScheme

    init(viewModel: AppViewModel) {
        self.viewModel = viewModel
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }

    var body: some View {
        buildBody()
            .onAppear {
                viewModel.startListening()
                updateProgressHUDStyle(colorScheme: colorScheme)
            }
            .onDisappear {
                viewModel.stopListening()
            }
            .onChange(of: colorScheme) { newColorScheme in
                updateProgressHUDStyle(colorScheme: newColorScheme)
            }
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        let state = viewModel.state

        switch state {
        case is AppFeatureStateIdle, is AppFeatureStateLoading:
            ProgressView()
        case is AppFeatureStateReady:
            TabView(selection: $navigationState.selectedTab) {
                HomeAssembly()
                    .makeModule()
                    .tag(AppTabItem.home)
                    .tabItem {
                        Image(systemName: AppTabItem.home.imageSystemName)
                        Text(AppTabItem.home.title)
                    }

                SettingsAssembly()
                    .makeModule()
                    .tag(AppTabItem.settings)
                    .tabItem {
                        Image(systemName: AppTabItem.settings.imageSystemName)
                        Text(AppTabItem.settings.title)
                    }
            }
        default:
            ProgressView()
        }
    }

    private func updateProgressHUDStyle(colorScheme: ColorScheme) {
        ProgressHUD.updateStyle(isDark: colorScheme == .dark)
    }

    private func handleViewAction(_ viewAction: AppFeatureActionViewAction) {
        print("AppView :: \(#function) viewAction = \(viewAction)")
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        AppAssembly().makeModule()
    }
}
