package br.com.advancedswingbutton;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Color;
import java.awt.Cursor;
import java.beans.BeanProperty;
import java.io.Serial;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.UIManager;

/**
 * Botão avançado baseado em JButton e FlatLaf.
 *
 * Mantém todas as propriedades nativas do JButton e adiciona
 * propriedades específicas através do prefixo "advButton".
 *
 * Como funciona:
 * - O estilo (ButtonStyle) define o "preset" visual, sem alterar as demais
 *   propriedades. Ex.: em OUTLINE o fundo nunca aparece, mesmo com
 *   advButtonBackgroundEnabled = true. Assim a ordem em que o NetBeans
 *   chama os setters não altera o resultado.
 * - Tudo é aplicado por uma única string em "FlatLaf.style".
 * - O tipo do botão fica nulo (padrão) em DEFAULT/FILLED/OUTLINE, porque
 *   BUTTON_TYPE_ROUND_RECT ignora a propriedade "arc" (formato pílula).
 *
 * Observação: este componente controla a propriedade "FlatLaf.style".
 * Se você definir um estilo próprio por código, ele será sobrescrito.
 *
 * Compatível com:
 * - NetBeans GUI Builder
 * - FlatLaf Light / Dark
 * - Java 21+
 */
public class AdvancedSwingButton extends JButton {

    @Serial
    private static final long serialVersionUID = 2L;

    private static final String TRANSPARENT = "#00000000";

    /**
     * Estilos disponíveis para o botão.
     */
    public enum ButtonStyle {

        /** Aparência padrão do FlatLaf (fundo + borda). */
        DEFAULT,

        /** Botão preenchido com a cor de destaque, sem borda. */
        FILLED,

        /** Somente borda, sem fundo. */
        OUTLINE,

        /** Sem fundo e sem borda, com destaque apenas no hover. */
        GHOST,

        /** Aparência de link (cor de link, cursor de mão, sem hover). */
        LINK
    }

    /*
     * ---------------------------------------------------------------
     * Estado
     * ---------------------------------------------------------------
     */
    private ButtonStyle advButtonStyle = ButtonStyle.DEFAULT;

    private int advButtonRadius = 6;

    private boolean advButtonBackgroundEnabled = true;

    private boolean advButtonBorderEnabled = true;

    private Color advButtonBorderColor;

    private int advButtonBorderWidth = 1;

    private boolean advButtonHoverEnabled = true;

    private Color advButtonHoverBackground;

    private Color advButtonPressedBackground;

    private boolean advButtonFocusEnabled = true;

    /** Cor do estilo FILLED. Nulo utiliza a cor de destaque do tema. */
    private Color advButtonFilledColor;

    /** Largura mínima. Negativo utiliza o valor do tema. */
    private int advButtonMinimumWidth = -1;

    /** Força o cursor de mão (LINK sempre usa). */
    private boolean advButtonHandCursor = false;

    /**
     * Evita aplicar estilo antes do fim da construção.
     * O JButton chama updateUI() dentro do super(), quando os campos
     * acima ainda não foram inicializados.
     */
    private boolean initialized;

    /*
     * ---------------------------------------------------------------
     * Construtores
     * ---------------------------------------------------------------
     */

    /**
     * Cria um botão.
     */
    public AdvancedSwingButton() {
        super();
        initialize();
    }

    /**
     * Cria um botão com texto.
     *
     * @param text texto do botão
     */
    public AdvancedSwingButton(String text) {
        super(text);
        initialize();
    }

    /**
     * Cria um botão com texto e ícone.
     *
     * @param text texto do botão
     * @param icon ícone do botão
     */
    public AdvancedSwingButton(String text, Icon icon) {
        super(text, icon);
        initialize();
    }

    /**
     * Inicialização do componente.
     */
    private void initialize() {

        setContentAreaFilled(true);
        setOpaque(false);
        setRolloverEnabled(true);

        initialized = true;

        refresh();
    }

    /**
     * Reaplica o estilo quando o LookAndFeel/tema é trocado
     * (ex.: claro para escuro).
     */
    @Override
    public void updateUI() {

        super.updateUI();

        if (initialized) {
            refresh();
        }
    }

    /*
     * ---------------------------------------------------------------
     * Propriedades (aparecem no editor do NetBeans)
     * ---------------------------------------------------------------
     */

    @BeanProperty(
            preferred = true,
            description = "Define o estilo visual do botão."
    )
    public ButtonStyle getAdvButtonStyle() {
        return advButtonStyle;
    }

    public void setAdvButtonStyle(ButtonStyle style) {

        ButtonStyle old = advButtonStyle;
        advButtonStyle = style != null ? style : ButtonStyle.DEFAULT;

        firePropertyChange("advButtonStyle", old, advButtonStyle);
        refresh();
    }

    @BeanProperty(
            preferred = true,
            description = "Define o arredondamento dos cantos (em pixels). Use um valor alto, como 999, para formato de pílula."
    )
    public int getAdvButtonRadius() {
        return advButtonRadius;
    }

    public void setAdvButtonRadius(int radius) {

        int old = advButtonRadius;
        advButtonRadius = Math.max(0, radius);

        firePropertyChange("advButtonRadius", old, advButtonRadius);
        refresh();
    }

    @BeanProperty(
            preferred = true,
            description = "Define se o fundo do botão será exibido (DEFAULT e FILLED)."
    )
    public boolean isAdvButtonBackgroundEnabled() {
        return advButtonBackgroundEnabled;
    }

    public void setAdvButtonBackgroundEnabled(boolean enabled) {

        boolean old = advButtonBackgroundEnabled;
        advButtonBackgroundEnabled = enabled;

        firePropertyChange("advButtonBackgroundEnabled", old, enabled);
        refresh();
    }

    @BeanProperty(
            preferred = true,
            description = "Define se a borda do botão será exibida (DEFAULT e OUTLINE)."
    )
    public boolean isAdvButtonBorderEnabled() {
        return advButtonBorderEnabled;
    }

    public void setAdvButtonBorderEnabled(boolean enabled) {

        boolean old = advButtonBorderEnabled;
        advButtonBorderEnabled = enabled;

        firePropertyChange("advButtonBorderEnabled", old, enabled);
        refresh();
    }

    @BeanProperty(
            description = "Define a cor da borda. Nulo utiliza a cor do tema."
    )
    public Color getAdvButtonBorderColor() {
        return advButtonBorderColor;
    }

    public void setAdvButtonBorderColor(Color color) {

        Color old = advButtonBorderColor;
        advButtonBorderColor = color;

        firePropertyChange("advButtonBorderColor", old, color);
        refresh();
    }

    @BeanProperty(
            preferred = true,
            description = "Define a espessura da borda."
    )
    public int getAdvButtonBorderWidth() {
        return advButtonBorderWidth;
    }

    public void setAdvButtonBorderWidth(int width) {

        int old = advButtonBorderWidth;
        advButtonBorderWidth = Math.max(0, width);

        firePropertyChange("advButtonBorderWidth", old, advButtonBorderWidth);
        refresh();
    }

    @BeanProperty(
            preferred = true,
            description = "Define se o efeito hover será utilizado."
    )
    public boolean isAdvButtonHoverEnabled() {
        return advButtonHoverEnabled;
    }

    public void setAdvButtonHoverEnabled(boolean enabled) {

        boolean old = advButtonHoverEnabled;
        advButtonHoverEnabled = enabled;

        firePropertyChange("advButtonHoverEnabled", old, enabled);
        refresh();
    }

    @BeanProperty(
            description = "Define a cor de fundo do hover. Nulo utiliza a cor do tema."
    )
    public Color getAdvButtonHoverBackground() {
        return advButtonHoverBackground;
    }

    public void setAdvButtonHoverBackground(Color color) {

        Color old = advButtonHoverBackground;
        advButtonHoverBackground = color;

        firePropertyChange("advButtonHoverBackground", old, color);
        refresh();
    }

    @BeanProperty(
            description = "Define a cor de fundo quando o botão estiver pressionado. Nulo utiliza a cor do tema."
    )
    public Color getAdvButtonPressedBackground() {
        return advButtonPressedBackground;
    }

    public void setAdvButtonPressedBackground(Color color) {

        Color old = advButtonPressedBackground;
        advButtonPressedBackground = color;

        firePropertyChange("advButtonPressedBackground", old, color);
        refresh();
    }

    @BeanProperty(
            preferred = true,
            description = "Define se o indicador visual de foco será exibido."
    )
    public boolean isAdvButtonFocusEnabled() {
        return advButtonFocusEnabled;
    }

    public void setAdvButtonFocusEnabled(boolean enabled) {

        boolean old = advButtonFocusEnabled;
        advButtonFocusEnabled = enabled;

        firePropertyChange("advButtonFocusEnabled", old, enabled);
        refresh();
    }

    @BeanProperty(
            description = "Cor do estilo FILLED. Nulo utiliza a cor de destaque do tema. O texto ajusta para preto/branco automaticamente."
    )
    public Color getAdvButtonFilledColor() {
        return advButtonFilledColor;
    }

    public void setAdvButtonFilledColor(Color color) {

        Color old = advButtonFilledColor;
        advButtonFilledColor = color;

        firePropertyChange("advButtonFilledColor", old, color);
        refresh();
    }

    @BeanProperty(
            description = "Largura mínima do botão. Valor negativo utiliza o padrão do tema (72). Use 0 para botões pequenos."
    )
    public int getAdvButtonMinimumWidth() {
        return advButtonMinimumWidth;
    }

    public void setAdvButtonMinimumWidth(int width) {

        int old = advButtonMinimumWidth;
        advButtonMinimumWidth = width;

        firePropertyChange("advButtonMinimumWidth", old, width);
        refresh();
    }

    @BeanProperty(
            description = "Exibe o cursor de mão ao passar o mouse (o estilo LINK sempre usa)."
    )
    public boolean isAdvButtonHandCursor() {
        return advButtonHandCursor;
    }

    public void setAdvButtonHandCursor(boolean enabled) {

        boolean old = advButtonHandCursor;
        advButtonHandCursor = enabled;

        firePropertyChange("advButtonHandCursor", old, enabled);
        refresh();
    }

    /*
     * ---------------------------------------------------------------
     * Regras efetivas (estilo + propriedades)
     * ---------------------------------------------------------------
     */

    /** O estilo permite fundo e a propriedade está ligada. */
    private boolean isBackgroundVisible() {
        return advButtonBackgroundEnabled
                && (advButtonStyle == ButtonStyle.DEFAULT
                || advButtonStyle == ButtonStyle.FILLED);
    }

    /** O estilo permite borda e a propriedade está ligada. */
    private boolean isBorderVisible() {
        return advButtonBorderEnabled
                && (advButtonStyle == ButtonStyle.DEFAULT
                || advButtonStyle == ButtonStyle.OUTLINE);
    }

    /** LINK nunca tem fundo de hover. */
    private boolean isHoverActive() {
        return advButtonHoverEnabled
                && advButtonStyle != ButtonStyle.LINK;
    }

    /*
     * ---------------------------------------------------------------
     * Aplicação no FlatLaf
     * ---------------------------------------------------------------
     */

    /**
     * Aplica tipo, estilo, foco e cursor. Ponto único de atualização.
     */
    private void refresh() {

        if (!initialized) {
            return;
        }

        boolean borderless = advButtonStyle == ButtonStyle.GHOST
                || advButtonStyle == ButtonStyle.LINK;

        /*
         * Tipo nulo = botão normal (respeita "arc").
         * BORDERLESS para GHOST/LINK.
         */
        putClientProperty(
                FlatClientProperties.BUTTON_TYPE,
                borderless ? FlatClientProperties.BUTTON_TYPE_BORDERLESS : null
        );

        putClientProperty(
                FlatClientProperties.STYLE,
                buildFlatLafStyle()
        );

        setFocusPainted(advButtonFocusEnabled);
        applyCursor();

        revalidate();
        repaint();
    }

    private void applyCursor() {

        if (advButtonHandCursor || advButtonStyle == ButtonStyle.LINK) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else if (isCursorSet()
                && getCursor().getType() == Cursor.HAND_CURSOR) {
            setCursor(null);
        }
    }

    /**
     * Constrói a string "FlatLaf.style".
     */
    private String buildFlatLafStyle() {

        StringBuilder style = new StringBuilder();

        /*
         * Raio.
         */
        add(style, "arc", String.valueOf(advButtonRadius));

        if (advButtonMinimumWidth >= 0) {
            add(style, "minimumWidth", String.valueOf(advButtonMinimumWidth));
        }

        /*
         * Fundo, texto e estados.
         */
        String baseBackground = "$Button.background";

        if (advButtonStyle == ButtonStyle.FILLED && isBackgroundVisible()) {

            if (advButtonFilledColor != null) {

                baseBackground = toHex(advButtonFilledColor);

                add(style, "background", baseBackground);
                add(style, "foreground", toHex(contrastColor(advButtonFilledColor)));
                add(style, "hoverBackground",
                        toHex(shift(advButtonFilledColor, 0.12f)));
                add(style, "pressedBackground",
                        toHex(shift(advButtonFilledColor, 0.24f)));

            } else {

                baseBackground = "$Button.default.background";

                add(style, "background", baseBackground);
                add(style, "foreground", "$Button.default.foreground");
                add(style, "hoverBackground", "$Button.default.hoverBackground");
                add(style, "pressedBackground", "$Button.default.pressedBackground");
            }

        } else if (!isBackgroundVisible()) {

            baseBackground = TRANSPARENT;

            add(style, "background", TRANSPARENT);
        }

        if (advButtonStyle == ButtonStyle.LINK) {

            add(style, "foreground", "$Component.linkColor");
            add(style, "hoverBackground", TRANSPARENT);
            add(style, "pressedBackground", TRANSPARENT);
        }

        /*
         * Hover: sem hover, o fundo do hover é igual ao fundo normal.
         * Cor personalizada tem prioridade sobre a do preset.
         */
        if (!isHoverActive()) {

            add(style, "hoverBackground", baseBackground);

        } else if (advButtonHoverBackground != null) {

            add(style, "hoverBackground", toHex(advButtonHoverBackground));
        }

        /*
         * Pressed.
         */
        if (advButtonStyle != ButtonStyle.LINK
                && advButtonPressedBackground != null) {

            add(style, "pressedBackground", toHex(advButtonPressedBackground));
        }

        /*
         * Borda.
         */
        if (!isBorderVisible()) {

            add(style, "borderWidth", "0");
            add(style, "borderColor", TRANSPARENT);
            add(style, "disabledBorderColor", TRANSPARENT);

        } else {

            add(style, "borderWidth", String.valueOf(advButtonBorderWidth));

            if (advButtonBorderColor != null) {

                String border = toHex(advButtonBorderColor);

                add(style, "borderColor", border);
                add(style, "hoverBorderColor", border);
            }
        }

        /*
         * Foco.
         */
        if (!advButtonFocusEnabled) {

            add(style, "focusWidth", "0");
            add(style, "innerFocusWidth", "0");

            /*
             * No FlatLaf o foco em botões também muda a cor da borda.
             */
            add(style, "focusedBorderColor",
                    advButtonBorderColor != null
                            ? toHex(advButtonBorderColor)
                            : "$Button.borderColor");
        }

        return style.toString();
    }

    private static void add(StringBuilder style, String key, String value) {

        /*
         * Se a chave já existe, a última ocorrência vence.
         * Removemos a anterior para manter a string limpa.
         */
        String token = key + ": ";
        int start = style.indexOf(token);

        while (start >= 0
                && start > 0
                && style.charAt(start - 1) != ' ') {
            start = style.indexOf(token, start + 1);
        }

        if (start >= 0) {
            int end = style.indexOf("; ", start);
            if (end >= 0) {
                style.delete(start, end + 2);
            }
        }

        style.append(key).append(": ").append(value).append("; ");
    }

    /*
     * ---------------------------------------------------------------
     * Utilitários de cor
     * ---------------------------------------------------------------
     */

    /**
     * Converte Color para hexadecimal aceito pelo FlatLaf.
     */
    private static String toHex(Color color) {

        if (color == null) {
            return TRANSPARENT;
        }

        StringBuilder hex = new StringBuilder("#");

        hex.append(String.format("%02X", color.getRed()));
        hex.append(String.format("%02X", color.getGreen()));
        hex.append(String.format("%02X", color.getBlue()));

        if (color.getAlpha() < 255) {
            hex.append(String.format("%02X", color.getAlpha()));
        }

        return hex.toString();
    }

    /**
     * Preto ou branco, o que tiver melhor contraste com a cor.
     */
    private static Color contrastColor(Color color) {

        double luminance = 0.299 * color.getRed()
                + 0.587 * color.getGreen()
                + 0.114 * color.getBlue();

        return luminance > 150 ? Color.BLACK : Color.WHITE;
    }

    /**
     * Clareia cores escuras e escurece cores claras
     * (usado para derivar hover/pressed).
     */
    private static Color shift(Color color, float amount) {

        double luminance = 0.299 * color.getRed()
                + 0.587 * color.getGreen()
                + 0.114 * color.getBlue();

        int target = luminance > 150 ? 0 : 255;

        int r = Math.round(color.getRed() + (target - color.getRed()) * amount);
        int g = Math.round(color.getGreen() + (target - color.getGreen()) * amount);
        int b = Math.round(color.getBlue() + (target - color.getBlue()) * amount);

        return new Color(r, g, b, color.getAlpha());
    }

    /**
     * Diagnóstico: informa se o FlatLaf é o LookAndFeel atual.
     *
     * @return true caso o FlatLaf esteja ativo
     */
    public boolean isFlatLafActive() {

        return UIManager.getLookAndFeel() != null
                && UIManager.getLookAndFeel()
                        .getClass()
                        .getName()
                        .startsWith("com.formdev.flatlaf.");
    }
}