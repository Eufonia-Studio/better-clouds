package com.qendolin.betterclouds.gui;

import com.google.common.collect.ImmutableSet;
import dev.isxander.yacl.api.*;
import dev.isxander.yacl.gui.YACLScreen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CustomButtonOption implements ButtonOption {

    private final Supplier<Text> name;
    private final Text tooltip;
    private final BiConsumer<YACLScreen, ButtonOption> action;
    private boolean available;
    private final Controller<BiConsumer<YACLScreen, ButtonOption>> controller;
    private final Binding<BiConsumer<YACLScreen, ButtonOption>> binding;

    public CustomButtonOption(
        @NotNull Supplier<Text> name,
        @Nullable Text tooltip,
        @NotNull BiConsumer<YACLScreen, ButtonOption> action,
        boolean available,
        @NotNull Function<ButtonOption, Controller<BiConsumer<YACLScreen, ButtonOption>>> controlGetter
    ) {
        this.name = name;
        this.tooltip = tooltip;
        this.action = action;
        this.available = available;
        this.controller = controlGetter.apply(this);
        this.binding = new EmptyBinderImpl();
    }

    public static Builder createBuilder() {
        return new Builder();
    }

    @Override
    public @NotNull Text name() {
        return name.get();
    }

    @Override
    public @NotNull Text tooltip() {
        return tooltip;
    }

    @Override
    public BiConsumer<YACLScreen, ButtonOption> action() {
        return action;
    }

    @Override
    public boolean available() {
        return available;
    }

    @Override
    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public @NotNull Controller<BiConsumer<YACLScreen, ButtonOption>> controller() {
        return controller;
    }

    @Override
    public @NotNull Binding<BiConsumer<YACLScreen, ButtonOption>> binding() {
        return binding;
    }

    @Override
    public @NotNull Class<BiConsumer<YACLScreen, ButtonOption>> typeClass() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull ImmutableSet<OptionFlag> flags() {
        return ImmutableSet.of();
    }

    @Override
    public boolean changed() {
        return false;
    }

    @Override
    public @NotNull BiConsumer<YACLScreen, ButtonOption> pendingValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void requestSet(BiConsumer<YACLScreen, ButtonOption> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean applyValue() {
        return false;
    }

    @Override
    public void forgetPendingValue() {

    }

    @Override
    public void requestSetDefault() {

    }

    @Override
    public boolean isPendingValueDefault() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addListener(BiConsumer<Option<BiConsumer<YACLScreen, ButtonOption>>, BiConsumer<YACLScreen, ButtonOption>> changedListener) {

    }

    protected static class EmptyBinderImpl implements Binding<BiConsumer<YACLScreen, ButtonOption>> {
        @Override
        public void setValue(BiConsumer<YACLScreen, ButtonOption> value) {

        }

        @Override
        public BiConsumer<YACLScreen, ButtonOption> getValue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public BiConsumer<YACLScreen, ButtonOption> defaultValue() {
            throw new UnsupportedOperationException();
        }
    }

    @ApiStatus.Internal
    public static final class Builder {
        private Supplier<Text> name;
        private final List<Text> tooltipLines = new ArrayList<>();
        private boolean available = true;
        private Function<ButtonOption, Controller<BiConsumer<YACLScreen, ButtonOption>>> controlGetter;
        private BiConsumer<YACLScreen, ButtonOption> action;

        public Builder name(@NotNull Supplier<Text> name) {
            Validate.notNull(name, "`name` cannot be null");

            this.name = name;
            return this;
        }

        public Builder tooltip(@NotNull Text... tooltips) {
            Validate.notNull(tooltips, "`tooltips` cannot be empty");

            tooltipLines.addAll(List.of(tooltips));
            return this;
        }

        public Builder action(@NotNull BiConsumer<YACLScreen, ButtonOption> action) {
            Validate.notNull(action, "`action` cannot be null");

            this.action = action;
            return this;
        }


        public Builder available(boolean available) {
            this.available = available;
            return this;
        }

        public Builder controller(@NotNull Function<ButtonOption, Controller<BiConsumer<YACLScreen, ButtonOption>>> control) {
            Validate.notNull(control, "`control` cannot be null");

            this.controlGetter = control;
            return this;
        }

        public ButtonOption build() {
            Validate.notNull(name, "`name` must not be null when building `Option`");
            Validate.notNull(controlGetter, "`control` must not be null when building `Option`");
            Validate.notNull(action, "`action` must not be null when building `Option`");

            MutableText concatenatedTooltip = Text.empty();
            boolean first = true;
            for (Text line : tooltipLines) {
                if (!first) concatenatedTooltip.append("\n");
                first = false;

                concatenatedTooltip.append(line);
            }

            return new CustomButtonOption(name, concatenatedTooltip, action, available, controlGetter);
        }
    }
}
