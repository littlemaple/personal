package network.rainbow.com.volley;

import android.os.RemoteException;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import junit.framework.Assert;

/**
 * Created by blue on 2015/7/16.
 */
public class UITester extends InstrumentationTestCase {

    private UiDevice device;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        device = UiDevice.getInstance(getInstrumentation());
    }

    public void testAdd() {
        UiObject2 oneBtn = device.findObject(By.text("9"));
        oneBtn.click();
        device.waitForIdle(500);
        UiObject2 twoBtn = device.findObject(By.text("6"));
        twoBtn.click();
        device.waitForIdle(500);
        UiObject2 separateBtn = device.findObject(By.desc("times"));
        separateBtn.click();
        device.waitForIdle(500);
        UiObject2 threeBtn = device.findObject(By.text("2"));
        threeBtn.click();
        device.waitForIdle(500);
        UiObject2 equalBtn = device.findObject(By.desc("equals"));
        equalBtn.click();
        device.waitForIdle(2000);
        UiObject2 resultView = device.findObject(By.clazz(EditText.class.getCanonicalName()));
        String result = resultView.getText();
        Log.d("automator:","the result:"+result);
        Assert.assertEquals(result,"192");
    }

}
