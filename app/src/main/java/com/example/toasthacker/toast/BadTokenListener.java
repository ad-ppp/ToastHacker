package com.example.toasthacker.toast;

import android.support.annotation.NonNull;
import android.widget.Toast;

public interface BadTokenListener {
  void onBadTokenCaught(@NonNull Toast toast);
}
