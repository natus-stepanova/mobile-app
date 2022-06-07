import SwiftUI

extension StepQuizTableSelectColumnsColumnView {
    struct Appearance {
        let interItemSpacing = LayoutInsets.smallInset

        let checkboxIndicatorWidthHeight: CGFloat = 18
        let radioIndicatorWidthHeight: CGFloat = 20
    }
}

struct StepQuizTableSelectColumnsColumnView: View {
    private(set)var appearance = Appearance()

    var isSelected: Binding<Bool>

    let text: String

    var isMultipleChoice: Bool

    var onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            HStack(spacing: appearance.interItemSpacing) {
                buildIndicator(isSelected: isSelected, onTap: onTap)

                Text(text)
                    .font(.body)
                    .foregroundColor(.primaryText)
                    .multilineTextAlignment(.leading)
                    .alignmentGuide(.stepQuizTableSelectColumnsTitleAlignmentGuide) { context in
                        context[.leading]
                    }
            }
        }
        .padding(.vertical)
    }

    @ViewBuilder
    private func buildIndicator(isSelected: Binding<Bool>, onTap: @escaping () -> Void) -> some View {
        if isMultipleChoice {
            CheckboxButton(isSelected: isSelected, onClick: onTap)
                .frame(widthHeight: appearance.checkboxIndicatorWidthHeight)
        } else {
            RadioButton(isSelected: isSelected, onClick: onTap)
                .frame(widthHeight: appearance.radioIndicatorWidthHeight)
        }
    }
}

struct StepQuizTableSelectColumnsColumnView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizTableSelectColumnsColumnView(
                isSelected: .constant(true),
                text: "Some option",
                isMultipleChoice: false,
                onTap: {}
            )
            StepQuizTableSelectColumnsColumnView(
                isSelected: .constant(true),
                text: "Some option",
                isMultipleChoice: true,
                onTap: {}
            )
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}