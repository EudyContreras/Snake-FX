/* ....Show License.... */

package com.EudyContreras.Snake.CustomNodes;


import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;

import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;

public class VirtualizedScrollPane extends Region  {

    private final ScrollBar hbar;
    private final ScrollBar vbar;
    private final Region content;

    private SimpleDoubleProperty hbarValue;
    private SimpleDoubleProperty vbarValue;

    /** The Policy for the Horizontal ScrollBar */
    private ScrollPane.ScrollBarPolicy hbarPolicy;
    public  ScrollPane.ScrollBarPolicy getHbarPolicy() { return hbarPolicy; }
    public void setHbarPolicy(ScrollPane.ScrollBarPolicy value) {  hbarPolicy = value; }
    public  ScrollPane.ScrollBarPolicy hbarPolicyProperty() { return hbarPolicy; }

    /** The Policy for the Vertical ScrollBar */
    private ScrollPane.ScrollBarPolicy vbarPolicy;
    public ScrollPane.ScrollBarPolicy getVbarPolicy() { return vbarPolicy; }
    public void setVbarPolicy(ScrollPane.ScrollBarPolicy value) { vbarPolicy = value; }
    public ScrollPane.ScrollBarPolicy vbarPolicyProperty() { return vbarPolicy; }

    /**
     * Constructs a VirtualizedScrollPane with the given content and policies
     */
    public VirtualizedScrollPane(Region content, ScrollPane.ScrollBarPolicy hPolicy, ScrollPane.ScrollBarPolicy vPolicy) {
        this.getStyleClass().add("virtualized-scroll-pane");
        this.content = content;

        // create scrollbars
        hbar = new ScrollBar();
        vbar = new ScrollBar();
        hbar.setOrientation(Orientation.HORIZONTAL);
        vbar.setOrientation(Orientation.VERTICAL);

        // scrollbar ranges
        hbar.setMin(0);
        vbar.setMin(0);
        hbar.maxProperty().bind(content.widthProperty());
        vbar.maxProperty().bind(content.heightProperty());

        // scrollbar increments
        setupUnitIncrement(hbar);
        setupUnitIncrement(vbar);
        hbar.blockIncrementProperty().bind(hbar.visibleAmountProperty());
        vbar.blockIncrementProperty().bind(vbar.visibleAmountProperty());

        // scrollbar positions
        hbarValue = new SimpleDoubleProperty(hbar.valueProperty().get());
        vbarValue = new SimpleDoubleProperty(vbar.valueProperty().get());
        Bindings.bindBidirectional(
                hbarValue,
                content.layoutXProperty());
        Bindings.bindBidirectional(
                vbarValue,
                content.layoutYProperty());

        // scrollbar visibility
        hbarPolicy = hPolicy;
        vbarPolicy = vPolicy;

//
//        // request layout later, because if currently in layout, the request is ignored
//        shouldDisplayHorizontal.addListener(obs -> Platform.runLater(this::requestLayout));
//        shouldDisplayVertical.addListener(obs -> Platform.runLater(this::requestLayout));
//
//        hbar.visibleProperty().bind(shouldDisplayHorizontal);
//        vbar.visibleProperty().bind(shouldDisplayVertical);

        getChildren().addAll(content, hbar, vbar);
        getChildren().addListener((Observable obs) -> dispose());
    }

    /**
     * Constructs a VirtualizedScrollPane that only displays its horizontal and vertical scroll bars as needed
     */
    public VirtualizedScrollPane(Region content) {
        this(content, AS_NEEDED, AS_NEEDED);
    }

    /**
     * Does not unbind scrolling from Content before returning Content.
     * @return - the content
     */
    public Region getContent() {
        return content;
    }

    /**
     * Unbinds scrolling from Content before returning Content.
     * @return - the content
     */
    public Region removeContent() {
        getChildren().clear();
        return content;
    }

    private void dispose() {
        hbarValue.unbindBidirectional(content.layoutXProperty());
        vbarValue.unbindBidirectional(content.layoutYProperty());
        unbindScrollBar(hbar);
        unbindScrollBar(vbar);
    }

    private void unbindScrollBar(ScrollBar bar) {
        bar.maxProperty().unbind();
        bar.unitIncrementProperty().unbind();
        bar.blockIncrementProperty().unbind();
        bar.visibleProperty().unbind();
    }


    @Override
    protected double computePrefWidth(double height) {
        return content.prefWidth(height);
    }

    @Override
    protected double computePrefHeight(double width) {
        return content.prefHeight(width);
    }

    @Override
    protected double computeMinWidth(double height) {
        return vbar.minWidth(-1);
    }

    @Override
    protected double computeMinHeight(double width) {
        return hbar.minHeight(-1);
    }

    @Override
    protected double computeMaxWidth(double height) {
        return content.maxWidth(height);
    }

    @Override
    protected double computeMaxHeight(double width) {
        return content.maxHeight(width);
    }

    @Override
    protected void layoutChildren() {
        double layoutWidth = getLayoutBounds().getWidth();
        double layoutHeight = getLayoutBounds().getHeight();
        boolean vbarVisible = vbar.isVisible();
        boolean hbarVisible = hbar.isVisible();
        double vbarWidth = vbarVisible ? vbar.prefWidth(-1) : 0;
        double hbarHeight = hbarVisible ? hbar.prefHeight(-1) : 0;

        double w = layoutWidth - vbarWidth;
        double h = layoutHeight - hbarHeight;

        content.resize(w, h);

        hbar.setVisibleAmount(w);
        vbar.setVisibleAmount(h);

        if(vbarVisible) {
            vbar.resizeRelocate(layoutWidth - vbarWidth, 0, vbarWidth, h);
        }

        if(hbarVisible) {
            hbar.resizeRelocate(0, layoutHeight - hbarHeight, w, hbarHeight);
        }
    }

    private static void setupUnitIncrement(ScrollBar bar) {
        bar.unitIncrementProperty().bind(new DoubleBinding() {
            { bind(bar.maxProperty(), bar.visibleAmountProperty()); }

            @Override
            protected double computeValue() {
                double max = bar.getMax();
                double visible = bar.getVisibleAmount();
                return max > visible
                        ? 16 / (max - visible) * max
                        : 0;
            }
        });
    }
}