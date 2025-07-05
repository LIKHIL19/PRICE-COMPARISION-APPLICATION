package utils;
import java.util.ArrayList;
import java.util.List;

public class WishlistEventBus {
    public interface WishlistListener {
        void onWishlistChanged();
    }

    private static final List<WishlistListener> listeners = new ArrayList<>();

    public static void addListener(WishlistListener listener) {
        listeners.add(listener);
    }

    public static void removeListener(WishlistListener listener) {
        listeners.remove(listener);
    }

    public static void notifyWishlistChanged() {
        for (WishlistListener listener : listeners) {
            listener.onWishlistChanged();
        }
    }
} 