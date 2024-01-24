package ru.vzotov.fx.controls.skin;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import ru.vzotov.fx.controls.MaterialField;

import static ru.vzotov.fx.utils.LayoutUtils.styled;

public class MaterialFieldSkin<T extends MaterialField> extends SkinBase<T> {

    private final Label captionText;

    private final Scale captionTextScale;

    private ObservableBooleanValue raiseCaptionText;


    public MaterialFieldSkin(T control) {
        super(control);

        captionTextScale = new Scale(1.0, 1.0, 0.0, 0.0);

        captionText = styled("caption", new Label());
        captionText.setManaged(false);
        captionText.setMouseTransparent(true);
        captionText.getTransforms().add(captionTextScale);
        captionText.textProperty().bind(control.captionProperty());
        captionText.visibleProperty().bind(control.captionProperty().isNotEmpty());


        raiseCaptionText = new BooleanBinding() {
            {
                bind(
                        control.textProperty(),
                        control.captionProperty(),
                        control.inputFocusedProperty()
                );
            }

            @Override
            protected boolean computeValue() {
                var txt = control.getText();
                var labelTxt = control.getCaption();

                boolean hasText = txt != null && !txt.isEmpty();
                boolean hasCaption = labelTxt != null && !labelTxt.isEmpty();

                return (hasText && hasCaption) || control.isInputFocused();
            }
        };

        control.inputProperty().addListener((it, oldInput, newInput) -> {
            getChildren().remove(oldInput);
            if (newInput != null) {
                getChildren().add(0, newInput);
            }
        });

        if (control.getInput() != null) {
            getChildren().add(control.getInput());
        }

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

        //captionText.autosize();
        Node input = getSkinnable().getInput();
        layoutInArea(captionText, x, y, w, h, input == null ? getSkinnable().getBaselineOffset() : input.getBaselineOffset(), HPos.LEFT, VPos.BASELINE);
    }

    @Override
    protected double computeMinWidth(double h, double top, double right, double bottom, double left) {
        final Node input = getSkinnable().getInput();
        return input == null ? super.computeMinWidth(h, top, right, bottom, left) : input.minWidth(h);
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        final Node input = getSkinnable().getInput();
        return input == null ? super.computeMinHeight(width, topInset, rightInset, bottomInset, leftInset) : input.minHeight(width);
    }

    @Override
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        final Node input = getSkinnable().getInput();
        return input == null ? super.computeMaxWidth(height, topInset, rightInset, bottomInset, leftInset) : input.maxWidth(height);
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        final Node input = getSkinnable().getInput();
        return input == null ? super.computeMaxHeight(width, topInset, rightInset, bottomInset, leftInset) : input.maxHeight(width);
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        final Node input = getSkinnable().getInput();
        return input == null ? super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset) : input.prefWidth(height);
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        final Node input = getSkinnable().getInput();
        return input == null ? super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset) : input.prefHeight(width);
    }

    @Override
    protected double computeBaselineOffset(double topInset, double rightInset, double bottomInset, double leftInset) {
        final Node input = getSkinnable().getInput();
        return input == null ? super.computeBaselineOffset(topInset, rightInset, bottomInset, leftInset) : input.getBaselineOffset();
    }
}
