package ru.vzotov.fx.controls.skin;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.skin.TextFieldSkin;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import ru.vzotov.fx.controls.MaterialTextField;

import static ru.vzotov.fx.utils.LayoutUtils.styled;

public class MaterialTextFieldSkin extends TextFieldSkin {

    private final Label captionText;

    private final Scale captionTextScale;

    private ObservableBooleanValue raiseCaptionText;

    public MaterialTextFieldSkin(MaterialTextField control) {
        super(control);

        captionTextScale = new Scale(1.0, 1.0, 0.0, 0.0);

        captionText = styled("caption", new Label());
        captionText.setManaged(false);
        captionText.setMouseTransparent(true);
        captionText.getTransforms().add(captionTextScale);
        captionText.textProperty().bind(control.captionProperty());

        raiseCaptionText = new BooleanBinding() {
            {
                bind(control.textProperty(),
                        control.captionProperty(),
                        control.focusedProperty()
                );
            }

            @Override
            protected boolean computeValue() {
                var txt = control.getText();
                var labelTxt = control.getCaption();

                boolean hasText = txt != null && !txt.isEmpty();
                boolean hasCaption = labelTxt != null && !labelTxt.isEmpty();

                return (hasText && hasCaption) || control.isFocused();
            }
        };

        if (raiseCaptionText.get()) {
            raiseLabelTextNode(false, true);
        }

        raiseCaptionText.addListener((observable, was, raised) -> {
            raiseLabelTextNode(was, raised);
            control.requestLayout();
        });


        getChildren().add(captionText);
    }

    private void raiseLabelTextNode(boolean wasRaised, boolean raised) {
        if (raised && !wasRaised) {
            final Timeline timeline = new Timeline();
            final ObservableList<KeyFrame> frames = timeline.getKeyFrames();
            frames.add(new KeyFrame(Duration.millis(100), new KeyValue(captionTextScale.xProperty(), .75)));
            frames.add(new KeyFrame(Duration.millis(100), new KeyValue(captionTextScale.yProperty(), .75)));
            frames.add(new KeyFrame(Duration.millis(100), new KeyValue(captionText.translateYProperty(), -20.0)));
            timeline.play();
        }
        if (!raised && wasRaised) {
            final Timeline timeline = new Timeline();
            final ObservableList<KeyFrame> frames = timeline.getKeyFrames();
            frames.add(new KeyFrame(Duration.millis(100), new KeyValue(captionTextScale.xProperty(), 1.0)));
            frames.add(new KeyFrame(Duration.millis(100), new KeyValue(captionTextScale.yProperty(), 1.0)));
            frames.add(new KeyFrame(Duration.millis(100), new KeyValue(captionText.translateYProperty(), 0.0)));
            timeline.play();
        }
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);

        captionText.autosize();
        layoutInArea(captionText, x, y, w, h, 0, HPos.LEFT, VPos.CENTER);
    }
}
