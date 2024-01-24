package ru.vzotov.fx.controls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class MaterialComboBoxTableCell<S, T> extends ComboBoxTableCell<S, T> {

    /* *************************************************************************
     *                                                                         *
     * Static cell factories                                                   *
     *                                                                         *
     **************************************************************************/

    @SafeVarargs
    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(
            final T... items) {
        return forTableColumn(null, items);
    }

    @SafeVarargs
    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(
            final StringConverter<T> converter,
            final T... items) {
        return forTableColumn(converter, FXCollections.observableArrayList(items));
    }

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(
            final ObservableList<T> items) {
        return forTableColumn(null, items);
    }

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(
            final StringConverter<T> converter,
            final ObservableList<T> items) {
        return list -> new MaterialComboBoxTableCell<>(converter, items);
    }

    public MaterialComboBoxTableCell() {
    }

    @SafeVarargs
    public MaterialComboBoxTableCell(T... items) {
        super(items);
    }

    @SafeVarargs
    public MaterialComboBoxTableCell(StringConverter<T> converter, T... items) {
        super(converter, items);
    }

    public MaterialComboBoxTableCell(ObservableList<T> items) {
        super(items);
    }

    public MaterialComboBoxTableCell(StringConverter<T> converter, ObservableList<T> items) {
        super(converter, items);
    }

    @Override
    public void startEdit() {
        if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
            return;
        }
        super.startEdit();

        if (isEditing()) {
            pseudoClassStateChanged(PSEUDO_CLASS_EDITING, isEditing());
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        pseudoClassStateChanged(PSEUDO_CLASS_EDITING, isEditing());
    }

    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        pseudoClassStateChanged(PSEUDO_CLASS_EDITING, isEditing());
    }


    private static final PseudoClass PSEUDO_CLASS_EDITING =
            PseudoClass.getPseudoClass("editing");

}
