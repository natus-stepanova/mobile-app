import shared
import SwiftUI

struct SettingsView: View {
    @State private var authResponse: String?

    var body: some View {
        NavigationView {
            Form {
                Section(header: Text("AUTH")) {
                    Text(authResponse ?? "N/A")
                }
                .onAppear {
                    authResponse = Settings.default.getStringOrNull(key: AuthCacheKeyValues.shared.AUTH_RESPONSE)
                }
            }
            .navigationBarTitle("SettingsTitle")
        }
    }
}

struct SettingsView_Previews: PreviewProvider {
    static var previews: some View {
        SettingsView()
    }
}