package com.xyz;

import com.nxp.nfclib.desfire.DESFireEV1.AuthType;

/**
 * Type of a key.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 * @see KeyReference
 */
public enum KeyTypeEnum {

  AES(16, AuthType.AES),
  TWO_KEY_TRIPLE_DES(14, AuthType.Native);

  private final int lengthInBytes;
  private final AuthType authType;

  KeyTypeEnum(final int lengthInBytes, final AuthType authType) {
    this.lengthInBytes = lengthInBytes;
    this.authType = authType;
  }

  public int getLengthInBytes() {
    return lengthInBytes;
  }

  public AuthType getAuthType() {
    return authType;
  }
}
