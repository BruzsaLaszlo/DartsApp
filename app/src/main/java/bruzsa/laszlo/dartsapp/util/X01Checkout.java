package bruzsa.laszlo.dartsapp.util;

import android.content.Context;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;
import java.util.function.IntConsumer;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ViewModelScoped;

@ViewModelScoped
public class X01Checkout {

    @Inject
    public X01Checkout() {
    }

    public void getCheckoutDartsCount(int throwValue, Context context, IntConsumer onClickListener) {
        if (throwValue > 110 || List.of(109, 108, 106, 105, 103, 102).contains(throwValue)) {
            onClickListener.accept(3);
        } else {
            boolean two = (throwValue > 40 && throwValue != 50) || throwValue % 2 == 1;
            new MaterialAlertDialogBuilder(context)
                    .setTitle("How many darts has been thrown?")
                    .setItems(two ? new CharSequence[]{"Not checkout", "2 darts", "3 darts"}
                                    : new CharSequence[]{"Not checkout", "1 dart", "2 darts", "3 darts"},
                            (dialog, which) ->
                            {
                                if (which == 0)
                                    onClickListener.accept(0);
                                else
                                    onClickListener.accept(two ? which + 1 : which);
                            })
                    .show();
        }
    }

}
