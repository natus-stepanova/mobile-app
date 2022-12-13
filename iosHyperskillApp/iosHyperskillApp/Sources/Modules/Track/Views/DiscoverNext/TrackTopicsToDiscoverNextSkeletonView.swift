import SwiftUI

struct TrackTopicsToDiscoverNextSkeletonView: View {
    var body: some View {
        SkeletonRoundedView(appearance: .init(cornerRadius: 0))
            .frame(height: 335)
    }
}

struct TrackTopicsToDiscoverNextSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        TrackTopicsToDiscoverNextSkeletonView()
    }
}
