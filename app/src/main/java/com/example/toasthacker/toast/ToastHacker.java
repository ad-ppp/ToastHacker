package com.example.toasthacker.toast;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ToastHacker {
    private static final String TAG = "ToastHacker";

    private static boolean isNeedToHacker() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                        Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1;
    }

    public static void tryToHack(Toast toast) {
        try {
            if (!isNeedToHacker()) {
                return;
            }

            Object mTN = getFieldValue(toast, "mTN");
            if (mTN != null) {
                boolean isSuccess = false;

                //a hack to some device which use the code between android 6.0 and android 7.1.1
                Object rawShowRunnable = getFieldValue(mTN, "mShow");
                if (rawShowRunnable instanceof Runnable) {
                    isSuccess = setFieldValue(mTN, "mShow", new InternalRunnable((Runnable) rawShowRunnable));
                }

                // hack to android 7.1.1,these cover 99% devices.
                if (!isSuccess) {
                    Object rawHandler = getFieldValue(mTN, "mHandler");
                    if (rawHandler instanceof Handler) {
                        isSuccess = setFieldValue(rawHandler, "mCallback", new InternalHandlerCallback((Handler) rawHandler));
                    }
                }

                if (!isSuccess) {
                    Log.d(TAG, "toast tryToHack error.");
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static class InternalRunnable implements Runnable {
        private final Runnable mRunnable;

        InternalRunnable(Runnable mRunnable) {
            this.mRunnable = mRunnable;
        }

        @Override
        public void run() {
            try {
                this.mRunnable.run();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private static class InternalHandlerCallback implements Handler.Callback {
        private final Handler mHandler;

        InternalHandlerCallback(Handler mHandler) {
            this.mHandler = mHandler;
        }

        @Override
        public boolean handleMessage(Message msg) {
            try {
                mHandler.handleMessage(msg);
            } catch (Throwable e) {
                Log.e(TAG, "catch error");
                e.printStackTrace();
            }
            return true;
        }
    }

    private static boolean setFieldValue(Object object, String fieldName, Object newFieldValue) {
        Field field = getDeclaredField(object, fieldName);
        if (field != null) {
            try {
                int accessFlags = field.getModifiers();
                if (Modifier.isFinal(accessFlags)) {
                    Field modifiersField = Field.class.getDeclaredField("accessFlags");
                    modifiersField.setAccessible(true);
                    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                }
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                field.set(object, newFieldValue);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static Object getFieldValue(Object obj, final String fieldName) {
        Field field = getDeclaredField(obj, fieldName);
        return getFieldValue(obj, field);
    }

    private static Object getFieldValue(Object obj, Field field) {
        if (field != null) {
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field.get(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static Field getDeclaredField(final Object obj, final String fieldName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // new add
            }
        }
        return null;
    }

}
