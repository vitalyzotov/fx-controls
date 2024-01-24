package ru.vzotov.fx.controls;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import ru.vzotov.fx.controls.skin.MaterialTextFieldSkin;

public class MaterialTextField extends TextField {

    public MaterialTextField() {
        this("");
    }

    public MaterialTextField(String text) {
        this(text, null);
    }

    public MaterialTextField(String text, String caption) {
        super(text);
        setCaption(caption);
        getStyleClass().add("material");
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MaterialTextFieldSkin(this);
    }

    /**
     * The label text to display in the {@code TextInputControl}. If set to null or an empty string, no
     * label text is displayed.
     */
    private final StringProperty caption = new SimpleStringProperty(this, "caption", "") {
        @Override protected void invalidated() {
            // Strip out newlines
            String txt = get();
            if (txt != null && txt.contains("\n")) {
                txt = txt.replace("\n", "");
                set(txt);
            }
        }
    };
    public final StringProperty captionProperty() { return caption; }
    public final String getCaption() { return caption.get(); }
    public final void setCaption(String value) { caption.set(value); }

}
