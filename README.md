# FileGuard

This repository contains a solution for secure file encryption and decryption using symmetric encryption with AES algorithm, PKCS7 padding, and CBC block mode. It also includes functionality for generating and validating message digests using SHA-256.

## Key Generation

The `generateKey()` function in the KeyStore module is responsible for generating a key that can be used for encryption and decryption.

## Encryption

The `encrypt(input_file, output_file)` function in the Encryption module takes an input file (`input.txt`) and encrypts its contents using AES algorithm with PKCS7 padding and CBC block mode. The encrypted data is then saved to an output file (`output.txt`).

## Decryption

The `decrypt(input_file, output_file)` function in the Decryption module decrypts the contents of an encrypted file (`output.txt`) and saves the decrypted data to a plaintext file (`plaintext.txt`). It ensures successful decryption by utilizing the Initialization Vector (IV) used during encryption.

## Message Digest Key Generation

To generate a key for message digest and its validation, the `generateDigestKey()` function in the KeyStore module is used.

## Message Digest Generation

The `generateDigest(input_file, output_file)` function in the MessageDigest module calculates the SHA-256 message digest for the contents of the input file (`input.txt`). It then creates a `digest.txt` file containing both the contents of the input file and the calculated digest.

## Message Digest Verification

To verify the correctness of the message digest received in the `digest.txt` file, the `verifyDigest(input_file)` function in the MessageDigest module is used. It compares the digest in the file with the calculated digest of the corresponding input file. If they match, the message digest is considered valid.

Please refer to the individual modules and their respective functions for more detailed information on their implementations.
