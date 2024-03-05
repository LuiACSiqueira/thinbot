package br.com.thin.utils;

import br.com.thin.buttons.*;

import java.util.HashSet;
import java.util.Set;

public class ButtonUtils {
    public static Set<ActionButton> getAll() {
        Set<ActionButton> all = new HashSet<>();
        ActionButton pauseToggle = new PauseToggleButton();
        ActionButton repeat = new RepeatButton();
        ActionButton shuffle = new ShuffleButton();
        ActionButton skip = new SkipButton();
        ActionButton stop = new StopButton();

        all.add(pauseToggle);
        all.add(repeat);
        all.add(shuffle);
        all.add(skip);
        all.add(stop);

        return all;
    }
}
