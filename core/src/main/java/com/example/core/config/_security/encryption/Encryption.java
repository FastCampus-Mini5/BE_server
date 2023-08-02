package com.example.core.config._security.encryption;

import com.example.core.errors.exception.DecryptException;
import com.example.core.errors.exception.EncryptException;

public interface Encryption {

    String encrypt(String data) throws EncryptException;

    String decrypt(String encryptedData) throws DecryptException;
}
