package ru.vzotov.fx.controls;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.TextInputControl;
import ru.vzotov.fx.controls.skin.MaterialFieldSkin;

public class MaterialField extends Control {

    private static final String DEFAULT_STYLE_CLASS = "material-field";
    private static final PseudoClass PSEUDO_CLASS_FOCUSED = PseudoClass.getPseudoClass("focused");

    public MaterialField() {
        this(null);
    }

    public MaterialField(Node input) {
        this(null, input);
    }

    public MaterialField(String caption, Node input) {
        setInput(input);
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setFocusTraversable(false);
        setCaption(caption);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MaterialFieldSkin<>(this);
    }

    /**
     *
     */
    private StringProperty text = new SimpleStringProperty(this, "text");

    public String getText() {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }

    /**
     * Input focused
     */
    private final BooleanProperty inputFocused = new SimpleBooleanProperty(this, "inputFocused") {
        @Override
        protected void invalidated() {
            final boolean focused = get();
            pseudoClassStateChanged(PSEUDO_CLASS_FOCUSED, focused);
        }
    };

    public boolean isInputFocused() {
        return inputFocused.get();
    }

    public BooleanProperty inputFocusedProperty() {
        return inputFocused;
    }

    public void setInputFocused(boolean inputFocused) {
        this.inputFocused.set(inputFocused);
    }

    /**
     * Поле
     */
    private final ObjectProperty<Node> input = new SimpleObjectProperty<>(this, "input") {
        @Override
        protected void invalidated() {
            textProperty().unbind();
            inputFocusedProperty().unbind();
            Node input = get();
            if (input instanceof TextInputControl) {
                textProperty().bind(((TextInputControl) input).textProperty());
            /*} else if (input instanceof ComboBox) {
                final var combo = (ComboBox<?>) input;
                textProperty().bind(new StringBinding() {
                    {
                        bind(combo.getSelectionModel().selectedItemProperty());
                    }

                    @Override
                    protected String computeValue() {
                        return combo.getSelectionModel().getSelectedItem() == null ? null : "selected";
                    }
                });*/
            } else if (input instanceof ComboBoxBase) {
                final var picker = (ComboBoxBase<?>) input;
                textProperty().bind(new StringBinding() {
                    {
                        bind(picker.valueProperty());
                    }

                    @Override
                    protected String computeValue() {
                        return picker.getValue() == null ? null : "selected";
                    }
                });

            }
            inputFocusedProperty().bind(input.focusedProperty());
        }
    };

    public Node getInput() {
        return input.get();
    }

    public ObjectProperty<Node> inputProperty() {
        return input;
    }

    public void setInput(Node input) {
        this.input.set(input);
    }

    public <T extends Node> T assignInput(T input) {
        setInput(input);
        return input;
    }

    /**
     * Название поля
     */
    private final StringProperty caption = new SimpleStringProperty(this, "caption", "") {
        @Override
        protected void invalidated() {
            // Strip out newlines
            String txt = get();
            if (txt != null && txt.contains("\n")) {
                txt = txt.replace("\n", "");
                set(txt);
            }
        }
    };

    public final StringProperty captionProperty() {
        return caption;
    }

    public final String getCaption() {
        return caption.get();
    }

    public final void setCaption(String value) {
        caption.set(value);
    }


}
