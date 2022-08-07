import shared
import SwiftUI

private enum Theme: String, CaseIterable, Identifiable {
    case light
    case dark
    case system

    var id: Self { self }

    var title: String {
        switch self {
        case .light:
            return Strings.Settings.Theme.light
        case .dark:
            return Strings.Settings.Theme.dark
        case .system:
            return Strings.Settings.Theme.system
        }
    }
}

struct SettingsView: View {
    private static let termsOfServiceURL = URL(string: "https://www.jetbrains.com/legal/terms/jetbrains-academy.html")
    private static let privacyPolicyURL = URL(string: "https://hi.hyperskill.org/terms")
    private static let helpCenterURL = URL(string: "https://support.hyperskill.org/hc/en-us")

    @StateObject var viewModel: SettingsViewModel
    @State private var selectedTheme: Theme = .light
    @State private var isPresentingLogoutAlert = false

    @Environment(\.presentationMode) private var presentationMode

    var body: some View {
        NavigationView {
            Form {
                Section(header: Text(Strings.Settings.appearance)) {
                    Picker(Strings.Settings.Theme.title, selection: $selectedTheme) {
                        ForEach(Theme.allCases) { theme in
                            if theme != selectedTheme {
                                Text(theme.title)
                                    .navigationTitle(Strings.Settings.Theme.title)
                            } else {
                                Text(theme.title)
                            }
                        }
                    }
                }

                Section(header: Text(Strings.Settings.about)) {
                    if let termsOfServiceURL = Self.termsOfServiceURL {
                        OpenURLInsideAppButton(
                            text: Strings.Settings.termsOfService,
                            url: termsOfServiceURL
                        )
                        .foregroundColor(.primaryText)
                    }

                    if let privacyPolicyURL = Self.privacyPolicyURL {
                        OpenURLInsideAppButton(
                            text: Strings.Settings.privacyPolicy,
                            url: privacyPolicyURL
                        )
                        .foregroundColor(.primaryText)
                    }

                    if let helpCenterURL = Self.helpCenterURL {
                        OpenURLInsideAppButton(
                            text: Strings.Settings.helpCenter,
                            url: helpCenterURL
                        )
                        .foregroundColor(.primaryText)
                    }

                    HStack {
                        Text(Strings.Settings.version)
                            .foregroundColor(.primaryText)

                        Spacer()

                        if let shortVersionWithBuildNumberString = MainBundleInfo.shortVersionWithBuildNumberString {
                            Text(shortVersionWithBuildNumberString)
                                .foregroundColor(.secondaryText)
                        }
                    }

                    Button(Strings.Settings.rateApplication) {
                    }
                    .foregroundColor(Color(ColorPalette.primary))
                }

                Section {
                    Button(Strings.Settings.logout) {
                        isPresentingLogoutAlert = true
                    }
                    .foregroundColor(Color(ColorPalette.overlayRed))
                }

                Section {
                    Button(Strings.Settings.deleteAccount) {
                    }
                    .foregroundColor(Color(ColorPalette.overlayRed))
                }
            }
            .navigationBarTitleDisplayMode(.inline)
            .navigationTitle(Strings.Settings.title)
            .toolbar {
                ToolbarItem(placement: .primaryAction) {
                    Button(Strings.General.done) {
                        presentationMode.wrappedValue.dismiss()
                    }
                }
            }
            .alert(isPresented: $isPresentingLogoutAlert) {
                Alert(
                    title: Text(Strings.Settings.logoutDialogTitle),
                    message: Text(Strings.Settings.logoutDialogExplanation),
                    primaryButton: .default(
                        Text(Strings.General.no)
                    ),
                    secondaryButton: .destructive(
                        Text(Strings.General.yes),
                        action: viewModel.logout
                    )
                )
            }
            .onAppear {
                viewModel.startListening()
                viewModel.onViewAction = handleViewAction(_:)
            }
            .onDisappear(perform: viewModel.stopListening)
        }
    }

    // MARK: Private API

    private func handleViewAction(_ viewAction: ProfileSettingsFeatureActionViewAction) {
        switch viewAction {
        case let viewAction as ProfileSettingsFeatureActionViewActionLogout:
            //TODO: show auth screen
            break
        default:
            print("SettingsView :: unhandled viewAction = \(viewAction)")
        }
    }
}

struct SettingsView_Previews: PreviewProvider {
    static var previews: some View {
        SettingsAssembly().makeModule()
    }
}
