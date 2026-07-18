package com.mosseygremlin.utiliwheel.screen;

import com.mosseygremlin.utiliwheel.menu.MenuEntry;
import com.mosseygremlin.utiliwheel.menu.MenuRegistry;
import com.mosseygremlin.utiliwheel.menu.MenuState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class RadialMenuScreen extends Screen {

    private List<MenuEntry> currentMenu;
    private String currentTitle;

    private int selected = -1;
    private boolean actionExecuted = false;

    private static final int INNER_RADIUS = 16;
    private static final int OUTER_RADIUS = 60;
    private static final int TEXT_RADIUS = 41;

    private static final float TEXT_SCALE = 0.75f;

    private static final int BUTTON_COLOR = 0x00909090;
    private static final int BUTTON_HOVER_COLOR = 0xB0B0B0B0;
    private static final int BORDER_DARK = 0xC0202020;

    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int HOVER_TEXT_COLOR = 0xFFFFFFA0;

    private final List<MenuState> menuHistory = new ArrayList<>();

    public RadialMenuScreen() {
        super(Component.literal("RadialKeys"));

        currentMenu = MenuRegistry.ROOT_MENU;
        currentTitle = "Home";
    }

    public void releaseF() {

        if (actionExecuted) {
            return;
        }

        MenuEntry selectedEntry = null;

        if (selected >= 0) {
            selectedEntry = currentMenu.get(selected);
        }

        Minecraft.getInstance().setScreen(null);

        if (selectedEntry != null
                && selectedEntry.getAction() != null) {

            actionExecuted = true;
            selectedEntry.getAction().run();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float delta
    ) {


        int centerX = this.width / 2;
        int centerY = this.height / 2;

        int count = currentMenu.size();

        if (count == 0) {

            drawScaledCenteredString(
                    graphics,
                    currentTitle,
                    centerX,
                    centerY,
                    TEXT_COLOR
            );

            return;
        }

        updateSelection(
                mouseX,
                mouseY,
                centerX,
                centerY,
                count
        );

        double sliceSize = (Math.PI * 2) / count;

        drawMenuWedges(
                graphics,
                centerX,
                centerY,
                count,
                sliceSize
        );

        drawCenterHub(
                graphics,
                centerX,
                centerY
        );

        drawMenuText(
                graphics,
                centerX,
                centerY,
                count,
                sliceSize
        );

        drawScaledCenteredString(
                graphics,
                currentTitle,
                centerX,
                centerY,
                TEXT_COLOR
        );
    }

    private void updateSelection(
            int mouseX,
            int mouseY,
            int centerX,
            int centerY,
            int count
    ) {
        double mouseXFromCenter = mouseX - centerX;
        double mouseYFromCenter = mouseY - centerY;

        double mouseDistance = Math.sqrt(
                mouseXFromCenter * mouseXFromCenter
                        + mouseYFromCenter * mouseYFromCenter
        );

        if (mouseDistance < INNER_RADIUS
                || mouseDistance > OUTER_RADIUS) {

            selected = -1;
            return;
        }

        double mouseAngle = Math.atan2(
                mouseYFromCenter,
                mouseXFromCenter
        );

        if (mouseAngle < 0) {
            mouseAngle += Math.PI * 2;
        }

        double sliceSize = (Math.PI * 2) / count;

        selected = (int) (
                (mouseAngle + sliceSize / 2) / sliceSize
        ) % count;
    }

    private void drawMenuWedges(
            GuiGraphics graphics,
            int centerX,
            int centerY,
            int count,
            double sliceSize
    ) {
        for (int i = 0; i < count; i++) {

            double centerAngle = sliceSize * i;
            double startAngle =
                    centerAngle - sliceSize / 2;
            double endAngle =
                    centerAngle + sliceSize / 2;

            int buttonColor = i == selected
                    ? BUTTON_HOVER_COLOR
                    : BUTTON_COLOR;

            // Dark outer border.
            drawWedge(
                    graphics,
                    centerX,
                    centerY,
                    INNER_RADIUS,
                    OUTER_RADIUS,
                    startAngle,
                    endAngle,
                    BORDER_DARK
            );

            // Translucent button face.
            drawWedge(
                    graphics,
                    centerX,
                    centerY,
                    INNER_RADIUS + 2,
                    OUTER_RADIUS - 2,
                    startAngle + 0.015,
                    endAngle - 0.015,
                    buttonColor
            );
        }
    }

    private void drawCenterHub(
            GuiGraphics graphics,
            int centerX,
            int centerY
    ) {
        drawCircle(
                graphics,
                centerX,
                centerY,
                INNER_RADIUS,
                BORDER_DARK
        );

        drawCircle(
                graphics,
                centerX,
                centerY,
                INNER_RADIUS - 2,
                BUTTON_COLOR
        );
    }

    private void drawMenuText(
            GuiGraphics graphics,
            int centerX,
            int centerY,
            int count,
            double sliceSize
    ) {
        for (int i = 0; i < count; i++) {

            MenuEntry entry = currentMenu.get(i);

            double angle = sliceSize * i;

            int textX = centerX
                    + (int) (
                    Math.cos(angle) * TEXT_RADIUS
            );

            int textY = centerY
                    + (int) (
                    Math.sin(angle) * TEXT_RADIUS
            );

            int textColor = i == selected
                    ? HOVER_TEXT_COLOR
                    : TEXT_COLOR;

            drawScaledCenteredString(
                    graphics,
                    entry.getName(),
                    textX,
                    textY,
                    textColor
            );
        }
    }

    private void drawScaledCenteredString(
            GuiGraphics graphics,
            String text,
            int x,
            int y,
            int color
    ) {
        graphics.pose().pushMatrix();

        graphics.pose().translate(
                x,
                y - this.font.lineHeight
                        * TEXT_SCALE / 2.0f
        );

        graphics.pose().scale(
                TEXT_SCALE,
                TEXT_SCALE
        );

        graphics.drawCenteredString(
                this.font,
                text,
                0,
                0,
                color
        );

        graphics.pose().popMatrix();
    }

    private void drawWedge(
            GuiGraphics graphics,
            int centerX,
            int centerY,
            int innerRadius,
            int outerRadius,
            double startAngle,
            double endAngle,
            int color
    ) {
        for (int y = -outerRadius;
             y <= outerRadius;
             y++) {

            int runStart = Integer.MIN_VALUE;

            for (int x = -outerRadius;
                 x <= outerRadius;
                 x++) {

                boolean inside = isInsideWedge(
                        x,
                        y,
                        innerRadius,
                        outerRadius,
                        startAngle,
                        endAngle
                );

                if (inside
                        && runStart
                        == Integer.MIN_VALUE) {

                    runStart = x;
                }

                boolean runEnded =
                        !inside
                                && runStart
                                != Integer.MIN_VALUE;

                boolean reachedFinalPixel =
                        x == outerRadius
                                && runStart
                                != Integer.MIN_VALUE;

                if (runEnded || reachedFinalPixel) {

                    int runEnd = inside
                            ? x + 1
                            : x;

                    graphics.fill(
                            centerX + runStart,
                            centerY + y,
                            centerX + runEnd,
                            centerY + y + 1,
                            color
                    );

                    runStart = Integer.MIN_VALUE;
                }
            }
        }
    }

    private boolean isInsideWedge(
            int x,
            int y,
            int innerRadius,
            int outerRadius,
            double startAngle,
            double endAngle
    ) {
        double distanceSquared = x * x + y * y;

        if (distanceSquared
                < innerRadius * innerRadius
                || distanceSquared
                > outerRadius * outerRadius) {

            return false;
        }

        double angle = Math.atan2(y, x);

        if (angle < 0) {
            angle += Math.PI * 2;
        }

        startAngle = normalizeAngle(startAngle);
        endAngle = normalizeAngle(endAngle);

        if (startAngle <= endAngle) {
            return angle >= startAngle
                    && angle <= endAngle;
        }

        return angle >= startAngle
                || angle <= endAngle;
    }

    private double normalizeAngle(double angle) {

        double fullCircle = Math.PI * 2;

        angle %= fullCircle;

        if (angle < 0) {
            angle += fullCircle;
        }

        return angle;
    }

    private void drawCircle(
            GuiGraphics graphics,
            int centerX,
            int centerY,
            int radius,
            int color
    ) {
        for (int y = -radius;
             y <= radius;
             y++) {

            int halfWidth = (int) Math.sqrt(
                    radius * radius - y * y
            );

            graphics.fill(
                    centerX - halfWidth,
                    centerY + y,
                    centerX + halfWidth + 1,
                    centerY + y + 1,
                    color
            );
        }
    }

    @Override
    public boolean mouseClicked(
            MouseButtonEvent event,
            boolean doubled
    ) {
        // Right click = go back.
        if (event.button() == 1) {

            if (!menuHistory.isEmpty()) {

                MenuState previous =
                        menuHistory.remove(
                                menuHistory.size() - 1
                        );

                currentMenu = previous.menu;
                currentTitle = previous.title;
                selected = -1;
            }

            return true;
        }

        // Left click = enter submenu or run action.
        if (event.button() == 0
                && selected >= 0) {

            MenuEntry entry =
                    currentMenu.get(selected);

            if (!entry.getChildren().isEmpty()) {

                menuHistory.add(
                        new MenuState(
                                currentMenu,
                                currentTitle
                        )
                );

                currentMenu = entry.getChildren();
                currentTitle = entry.getName();
                selected = -1;

            } else if (entry.getAction() != null) {

                actionExecuted = true;

                Minecraft.getInstance().setScreen(null);

                entry.getAction().run();
            }

            return true;
        }

        return super.mouseClicked(event, doubled);
    }
}