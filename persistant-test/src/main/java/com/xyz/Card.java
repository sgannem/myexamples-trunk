package com.xyz;

import java.util.Objects;

import javax.annotation.Nullable;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

/**
 * Card type and keys.
 *
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
@Embeddable
public class Card {

  private CardTypeEnum cardType;
  private KeyReference damAuthKeyRef;
  private KeyReference damMacKeyRef;
  private KeyReference damEncKeyRef;
  private KeyReference ev1MasterKeyRef;

  Card(@Nullable final CardTypeEnum cardType, @Nullable final KeyReference damAuthKeyRef, @Nullable final KeyReference damMacKeyRef,
       @Nullable final KeyReference damEncKeyRef, @Nullable final KeyReference ev1MasterKeyRef) {
    this.cardType = cardType;
    this.damAuthKeyRef = damAuthKeyRef;
    this.damMacKeyRef = damMacKeyRef;
    this.damEncKeyRef = damEncKeyRef;
    this.ev1MasterKeyRef = ev1MasterKeyRef;
  }

  Card() {
    this(null, null, null, null, null);
  }

  static Card newInstance(final CardTypeEnum cardType, final KeyReference damAuthKeyRef, final KeyReference damMacKeyRef, final
  KeyReference damEncKeyRef, final KeyReference ev1MasterKeyRef) {
    return new Card(cardType, damAuthKeyRef, damMacKeyRef, damEncKeyRef, ev1MasterKeyRef);
  }

  @Enumerated(EnumType.STRING)
  public CardTypeEnum getCardType() {
    return cardType;
  }

  void setCardType(final CardTypeEnum cardType) {
    this.cardType = cardType;
  }

  @ManyToOne(optional = true, targetEntity = KeyReference.class, cascade = CascadeType.PERSIST)
  public KeyReference getDamAuthKeyRef() {
    return damAuthKeyRef;
  }

  public void setDamAuthKeyRef(final KeyReference damAuthKeyRef) {
    this.damAuthKeyRef = damAuthKeyRef;
  }

  public boolean hasDamAuthKeyRef() {
    return (damAuthKeyRef != null);
  }

  @ManyToOne(optional = true, targetEntity = KeyReference.class, cascade = CascadeType.PERSIST)
  public KeyReference getDamMacKeyRef() {
    return damMacKeyRef;
  }

  public void setDamMacKeyRef(final KeyReference damMacKeyRef) {
    this.damMacKeyRef = damMacKeyRef;
  }

  public boolean hasDamMacKeyRef() {
    return (damAuthKeyRef != null);
  }

  @ManyToOne(optional = true, targetEntity = KeyReference.class, cascade = CascadeType.PERSIST)
  public KeyReference getDamEncKeyRef() {
    return damEncKeyRef;
  }

  public void setDamEncKeyRef(final KeyReference damEncKeyRef) {
    this.damEncKeyRef = damEncKeyRef;
  }

  public boolean hasDamEncKeyRef() {
    return (damAuthKeyRef != null);
  }

  @ManyToOne(optional = true, targetEntity = KeyReference.class, cascade = CascadeType.PERSIST)
  public KeyReference getEv1MasterKeyRef() {
    return ev1MasterKeyRef;
  }

  public void setEv1MasterKeyRef(final KeyReference ev1MasterKeyRef) {
    this.ev1MasterKeyRef = ev1MasterKeyRef;
  }

  public boolean hasEv1MasterKeyRef() {
    return (ev1MasterKeyRef != null);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }

    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }

    final Card card = (Card) o;

    return Objects.equals(getDamAuthKeyRef(), card.getDamAuthKeyRef()) &&
        Objects.equals(getDamMacKeyRef(), card.getDamMacKeyRef()) &&
        Objects.equals(getDamEncKeyRef(), card.getDamEncKeyRef()) &&
        Objects.equals(getEv1MasterKeyRef(), card.getEv1MasterKeyRef()) &&
        (getCardType() == card.getCardType());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getDamAuthKeyRef(), getDamMacKeyRef(), getDamEncKeyRef(), getEv1MasterKeyRef(), getCardType());
  }

  @Override
  public String toString() {
    return "Card{" +
        "cardType=" + cardType +
        ", damAuthKeyRef=" + damAuthKeyRef +
        ", damMacKeyRef=" + damMacKeyRef +
        ", damEncKeyRef=" + damEncKeyRef +
        ", ev1MasterKeyRef=" + ev1MasterKeyRef +
        '}';
  }

  public boolean areCardKeysNotSet() {
    return true;//!cardType.areRequiredKeysSet(this);
  }
}
