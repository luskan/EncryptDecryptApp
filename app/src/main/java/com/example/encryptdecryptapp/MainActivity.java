package com.example.encryptdecryptapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import com.example.encryptdecryptapp.databinding.ActivityMainBinding;  // Import your binding class

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String SAMPLE_ALIAS = "MYALIAS";

    private ActivityMainBinding binding;

    private EnCryptor encryptor;
    private DeCryptor decryptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.toolbar);

        encryptor = new EnCryptor();

        try {
            decryptor = new DeCryptor();
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException |
                 IOException e) {
            e.printStackTrace();
        }

        binding.btnEncrypt.setOnClickListener(v -> encryptText());
        binding.btnDecrypt.setOnClickListener(v -> decryptText());
    }

    private void decryptText() {
        try {
            String encryptedText = binding.edTextToEncrypt.getText().toString();
            Log.d(TAG, "Decrypting: " + encryptedText);
            byte[] encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT);
            binding.tvDecryptedText.setText(decryptor
                        .decryptData(SAMPLE_ALIAS, encryptedBytes, encryptor.getIv()));

        } catch (UnrecoverableEntryException | NoSuchAlgorithmException | IllegalArgumentException |
                 KeyStoreException | NoSuchPaddingException | NoSuchProviderException |
                 IOException | InvalidKeyException e) {
            Log.e(TAG, "decryptData() called with: " + e.getMessage(), e);
            binding.tvDecryptedText.setText("Error decrypting: " + e.getMessage());
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            binding.tvDecryptedText.setText("Error decrypting: " + e.getMessage());
        }
    }

    private void encryptText() {
        try {
            String textToEncrypt = binding.edTextToEncrypt.getText().toString();
            Log.d(TAG, "Encrypting: " + textToEncrypt);
            final byte[] encryptedTextArray = encryptor
                    .encryptText(SAMPLE_ALIAS, textToEncrypt);
            String encryptedText = Base64.encodeToString(encryptedTextArray, Base64.DEFAULT);
            binding.tvEncryptedText.setText(encryptedText);
            Log.d(TAG, "Encrypted : " + encryptedText);
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException | NoSuchProviderException |
                 KeyStoreException | IOException | NoSuchPaddingException | InvalidKeyException e) {
            Log.e(TAG, "onClick() called with: " + e.getMessage(), e);
            binding.tvEncryptedText.setText("Error encrypting: " + e.getMessage());
        } catch (InvalidAlgorithmParameterException | SignatureException |
                 IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            binding.tvEncryptedText.setText("Error encrypting: " + e.getMessage());
        }
    }
}
