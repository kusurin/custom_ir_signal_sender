package kusurin.custom_ir_signal_sender;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.os.Build;

import androidx.annotation.Nullable;

public class IrApi {
    private static IrApi instance;
    private static ConsumerIrManager IrService;

    private IrApi(Context context){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            IrService=(ConsumerIrManager) context.getApplicationContext().getSystemService((Context.CONSUMER_IR_SERVICE));
        }
    }

    public static IrApi getIr(Context context){
        if(instance==null){
            instance=new IrApi(context);
        }
        return instance;
    }

    public static boolean hasIrEmitter(){
        if(IrService!=null)
            return  IrService.hasIrEmitter();
        return false;
    }

    @Nullable
    public static int[][] getFrequencies(){
        if(IrService!=null){
            ConsumerIrManager.CarrierFrequencyRange[] ranges=IrService.getCarrierFrequencies();
            int[][] range_i = new int[2][];
            for (int i = 0; i < ranges.length; i++) {
                range_i[0][i]=ranges[i].getMinFrequency();
                range_i[1][i]=ranges[i].getMaxFrequency();
            }
            return range_i;
        }
        return null;
    }

    public static boolean transmit(int cf,int[] pattern){
        if(IrService!=null){
            IrService.transmit(cf,pattern);
            return true;
        }
        return false;
    }
}
