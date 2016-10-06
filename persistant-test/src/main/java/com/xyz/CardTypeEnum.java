package com.xyz;

import java.util.Optional;

/**
 * Type of a card.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
public enum CardTypeEnum {

  EV1(CardTypeEnum.EV1_MAJOR_VERSION, CardTypeEnum.EV1_MAJOR_VERSION) {
    @Override
    public boolean areRequiredKeysSet(final Card card) {
      return card.hasEv1MasterKeyRef();
    }
  },

  EV2(CardTypeEnum.EV2_HARDWARE_INFO_MAJOR_VERSION, CardTypeEnum.EV2_SOFTWARE_INFO_MAJOR_VERSION) {
    @Override
    public boolean areRequiredKeysSet(final Card card) {
      return card.hasDamAuthKeyRef() && card.hasDamMacKeyRef() && card.hasDamEncKeyRef();
    }
  };

  public static final byte EV1_MAJOR_VERSION = 0x01;
  public static final byte EV2_HARDWARE_INFO_MAJOR_VERSION = 0x12;
  public static final byte EV2_SOFTWARE_INFO_MAJOR_VERSION = 0x02;

  public final byte majorVersionHardwareInfo;
  private final byte majorVersionSoftwareInfo;

  CardTypeEnum(final int majorVersionHardwareInfo, final int majorVersionSoftwareInfo) {
    this.majorVersionHardwareInfo = (byte) majorVersionHardwareInfo;
    this.majorVersionSoftwareInfo = (byte) majorVersionSoftwareInfo;
  }

  public byte getMajorVersionHardwareInfo() {
    return majorVersionHardwareInfo;
  }

  public byte getMajorVersionSoftwareInfo() {
    return majorVersionSoftwareInfo;
  }

  public abstract boolean areRequiredKeysSet(Card card);

  public static Optional<CardTypeEnum> getByMajorVersion(final byte majorVersionHardwareInfo, final byte majorVersionSoftwareInfo) {
    if ((EV1.majorVersionHardwareInfo == majorVersionHardwareInfo) || (EV1.majorVersionSoftwareInfo == majorVersionSoftwareInfo)) {
      return Optional.of(EV1);
    }

    if ((EV2.majorVersionHardwareInfo == majorVersionHardwareInfo) || (EV2.majorVersionSoftwareInfo == majorVersionSoftwareInfo)) {
      return Optional.of(EV2);
    }

    return Optional.empty();
  }
}
