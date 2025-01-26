package bruzsa.laszlo.dartsapp.ui.x01;

import android.content.Context;

import java.util.function.Consumer;

import bruzsa.laszlo.dartsapp.model.x01.X01Throw;

public interface OnChangeThrow {

    void change(X01Throw x01Throw, Context context, Consumer<Boolean> callback);

}
