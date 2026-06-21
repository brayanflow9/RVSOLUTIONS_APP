package vista;

public class ValidadorRV {
    private ValidadorRV() {}

    public static boolean textoObligatorio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    public static boolean numeroEnteroPositivo(String texto) {
        if (!textoObligatorio(texto)) return false;
        try {
            return Integer.parseInt(texto.trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
