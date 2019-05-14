# A Hacker
**To solve Toast crash(android.view.WindowManager$BadTokenException) in Android 5.1 to Android 7.0**
Replace the base Context to a SafeToastContext, it will hook the WindowManagerWrapper.addView(view, params) method and fix the exception. Android N

Bugly info : https://bugly.qq.com/v2/crash-reporting/crashes/96c53caaf3/1517?pid=1

### Thanks to 
- [PureWriter-ToastCompat](https://github.com/PureWriter/ToastCompat)
- [ToastCompat](https://github.com/cat9/ToastCompat)