package com.xyz;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Transient;


/**
 * Entity representing a Card Issuer that is responsible for Issuing a physical DESFire Card.
 *
 * @author Gerald Madlmayr (gerald.madlmayr@rise-world.com)
 */

@Entity
public class CardIssuer extends AbstractStrongEntity {

  @Embedded
  private User user;
  private String company;
  private String email;
  private String cardName;
  private byte[] logo;
  private NumberOfCardsEnum numberOfCards;
  private String countryIsoCode;
  private String industrySectorNaicsNr;
  private String linkedInUrl;
  private String phone;
  private String homepage;
  private String addressName;
  private String addressStreet;
  private String addressZipCity;
  private String addressCountry;
  private CardIssuerRegistrationStatusEnum registrationStatus;
  private Long registrationConfirmedOn;
  private String ndefUrlToken;
  private Integer numberOfSharedSlots;
  private Integer quotaLimitOfSharedSlot;
  private Integer availableCardSpaceInBytes;
  private boolean hasSharedSlotApproval;

  @Embedded
  private Card card;

  private List<ApprovedApplication> approvedApplications;

  private boolean isCardIssuerDataComplete;
  private boolean isCardDataComplete;

  CardIssuer() {
    super();
  }

  private CardIssuer(final Builder builder) {
    super(builder.id);
    setUser(builder.user);
    setCompany(builder.company);
    setEmail(builder.email);
    setCardName(builder.cardName);
    setLogo(builder.logo);
    setNumberOfCards(builder.numberOfCards);
    setCountryIsoCode(builder.countryIsoCode);
    setIndustrySectorNaicsNr(builder.industrySectorNaicsNr);
    setLinkedInUrl(builder.linkedInUrl);
    setPhone(builder.phone);
    setHomepage(builder.homepage);
    setAddressName(builder.addressName);
    setAddressStreet(builder.addressStreet);
    setAddressZipCity(builder.addressZipCity);
    setAddressCountry(builder.addressCountry);
    setRegistrationStatus(builder.registrationStatus);
    setRegistrationConfirmedOn(builder.registrationConfirmedOn);
    setNdefUrlToken(builder.ndefUrlToken);
    setCard(Card.newInstance(builder.cardType, builder.damAuthKeyRef, builder.damMacKeyRef, builder.damEncKeyRef, builder.ev1MasterKeyRef));
    isCardIssuerDataComplete = builder.isCardIssuerDataComplete;
    isCardDataComplete = builder.isCardDataComplete;
    setNumberOfSharedSlots(builder.numberOfSharedSlots);
    setQuotaLimitOfSharedSlot(builder.quotaLimitOfSharedSlot);
    setAvailableCardSpaceInBytes(builder.availableCardSpaceInBytes);
    setHasSharedSlotApproval(builder.hasSharedSlotApproval);
  }

  @Column(length = 128, nullable = false)
  public String getCompany() {
    return company;
  }

  public void setCompany(final String company) {
    this.company = company;
  }

  @Column
  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public User getUser() {
    return user;
  }

  public void setUser(final User user) {
    this.user = user;
  }

  @Column
  public String getCardName() {
    return cardName;
  }

  public void setCardName(final String cardName) {
    this.cardName = cardName;
  }

//  @Column(length = 1000000)
  public byte[] getLogo() {
    return logo;
  }

  public void setLogo(final byte[] logo) {
    this.logo = logo;
  }

  @Enumerated(EnumType.STRING)
  public NumberOfCardsEnum getNumberOfCards() {
    return numberOfCards;
  }

  public void setNumberOfCards(final NumberOfCardsEnum numberOfCards) {
    this.numberOfCards = numberOfCards;
  }

  @Column
  public String getCountryIsoCode() {
    return countryIsoCode;
  }

  public void setCountryIsoCode(final String countryIsoCode) {
    this.countryIsoCode = countryIsoCode;
  }

  @Column
  public String getIndustrySectorNaicsNr() {
    return industrySectorNaicsNr;
  }

  public void setIndustrySectorNaicsNr(final String industrySectorNaicsNr) {
    this.industrySectorNaicsNr = industrySectorNaicsNr;
  }

  @Column
  public String getLinkedInUrl() {
    return linkedInUrl;
  }

  public void setLinkedInUrl(final String linkedInUrl) {
    this.linkedInUrl = linkedInUrl;
  }

  @Column
  public String getPhone() {
    return phone;
  }

  public void setPhone(final String phone) {
    this.phone = phone;
  }

  @Column
  public String getHomepage() {
    return homepage;
  }

  public void setHomepage(final String homepage) {
    this.homepage = homepage;
  }

  @Column
  public String getAddressName() {
    return addressName;
  }

  public void setAddressName(final String addressName) {
    this.addressName = addressName;
  }

  @Column
  public String getAddressStreet() {
    return addressStreet;
  }

  public void setAddressStreet(final String addressStreet) {
    this.addressStreet = addressStreet;
  }

  @Column
  public String getAddressZipCity() {
    return addressZipCity;
  }

  public void setAddressZipCity(final String addressZipCity) {
    this.addressZipCity = addressZipCity;
  }

  @Column
  public String getAddressCountry() {
    return addressCountry;
  }

  public void setAddressCountry(final String addressCountry) {
    this.addressCountry = addressCountry;
  }

  @Enumerated(EnumType.STRING)
  public CardIssuerRegistrationStatusEnum getRegistrationStatus() {
    return registrationStatus;
  }

  void setRegistrationStatus(final CardIssuerRegistrationStatusEnum registrationStatus) {
    this.registrationStatus = registrationStatus;
  }

  public Long getRegistrationConfirmedOn() {
    return registrationConfirmedOn;
  }

  void setRegistrationConfirmedOn(final Long registrationConfirmedOn) {
    this.registrationConfirmedOn = registrationConfirmedOn;
  }

  public Card getCard() {
    return card;
  }

  void setCard(final Card card) {
    this.card = card;
  }

  @Column(columnDefinition = "int2")
  public Integer getNumberOfSharedSlots() {
    return numberOfSharedSlots;
  }

  void setNumberOfSharedSlots(final Integer numberOfSharedSlots) {
    this.numberOfSharedSlots = numberOfSharedSlots;
  }

  @Column(columnDefinition = "int2")
  public Integer getQuotaLimitOfSharedSlot() {
    return quotaLimitOfSharedSlot;
  }

  void setQuotaLimitOfSharedSlot(final Integer quotaLimitOfSharedSlot) {
    this.quotaLimitOfSharedSlot = quotaLimitOfSharedSlot;
  }

  public boolean areSharedSlotsEnabled() {
    return (numberOfSharedSlots != null);
  }

  public boolean isSharedSlotAllowed(final int applicationQuotaLimit) {
    return (areSharedSlotsEnabled() && (applicationQuotaLimit <= getQuotaLimitOfSharedSlot()));
  }

  public void updateSharedSlotSettings(final int numberOfSharedSlots, final int quotaLimitOfSharedSlot) {
    this.numberOfSharedSlots = numberOfSharedSlots;
    this.quotaLimitOfSharedSlot = quotaLimitOfSharedSlot;
  }

  public void clearSharedSlotSettings() {
    this.numberOfSharedSlots = null;
    this.quotaLimitOfSharedSlot = null;
  }

  @Column
  public Integer getAvailableCardSpaceInBytes() {
    return availableCardSpaceInBytes;
  }

  public void setAvailableCardSpaceInBytes(final Integer availableCardSpaceInBytes) {
    this.availableCardSpaceInBytes = availableCardSpaceInBytes;
  }

  @Transient
  public boolean isAvailableCardSpaceInBytesUnset() {
    return (availableCardSpaceInBytes == null);
  }

  public boolean getHasSharedSlotApproval() {
    return hasSharedSlotApproval;
  }

  void setHasSharedSlotApproval(final boolean hasSharedSlotApproval) {
    this.hasSharedSlotApproval = hasSharedSlotApproval;
  }

  public void markAsHasSharedSlotApproval() {
    setHasSharedSlotApproval(true);
  }

  @Transient
  public com.xyz.CardIssuerStatusEnum getStatus() {
    if (this.getRegistrationStatus().equals(com.xyz.CardIssuerRegistrationStatusEnum.UNCONFIRMED)) {
      return com.xyz.CardIssuerStatusEnum.UNCONFIRMED;
    } else if (!(isCardDataComplete() && isCardIssuerDataComplete())) {
      return com.xyz.CardIssuerStatusEnum.MISSING_DATA;
    } else {
      return com.xyz.CardIssuerStatusEnum.READY;
    }
  }

  @Transient
  public boolean isConfirmed() {
	  return (this.registrationStatus == com.xyz.CardIssuerRegistrationStatusEnum.CONFIRMED);
  }

  public void markAsConfirmed(final User user, final String ndefUrlToken, final long now) {
    setUser(user);
    setNdefUrlToken(ndefUrlToken);
    this.registrationStatus = com.xyz.CardIssuerRegistrationStatusEnum.CONFIRMED;
    this.registrationConfirmedOn = now;
  }

  @Column
  public String getNdefUrlToken() {
    return ndefUrlToken;
  }

  void setNdefUrlToken(final String ndefUrlToken) {
    this.ndefUrlToken = ndefUrlToken;
  }

  @OneToMany(mappedBy = "cardIssuer")
  public List<ApprovedApplication> getApprovedApplications() {
    return approvedApplications;
  }

  public void setApprovedApplications(final List<ApprovedApplication> approvedApplications) {
    this.approvedApplications = approvedApplications;
  }

  @Transient
  public CardTypeEnum getCardType() {
    return card.getCardType();
  }

  @Transient
  public void setCardType(final CardTypeEnum cardType) {
    card.setCardType(cardType);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }

    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }

    final CardIssuer that = (CardIssuer) o;
    return Objects.equals(getUser(), that.getUser());
  }

  @Transient
  public boolean isCardIssuerDataComplete() {
    return this.isCardIssuerDataComplete;
  }

  @Transient
  public boolean isCardDataComplete() {
    return this.isCardDataComplete;
  }

  public void setIsCardIssuerDataComplete(final boolean isCardIssuerDataComplete) {
    this.isCardIssuerDataComplete = isCardIssuerDataComplete;
  }

  public void setIsCardDataComplete(final boolean isCardDataComplete) {
    this.isCardDataComplete = isCardDataComplete;
  }

  public boolean areCardKeysNotSet() {
    return card.areCardKeysNotSet();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUser());
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public String toString() {
    return "CardIssuer{" +
        "user=" + user +
        ", company='" + company + '\'' +
        ", email='" + email + '\'' +
        ", cardName='" + cardName + '\'' +
        ", logo=" + Arrays.toString(logo) +
        ", numberOfCards=" + numberOfCards +
        ", countryIsoCode='" + countryIsoCode + '\'' +
        ", industrySectorNaicsNr='" + industrySectorNaicsNr + '\'' +
        ", linkedInUrl='" + linkedInUrl + '\'' +
        ", phone='" + phone + '\'' +
        ", homepage='" + homepage + '\'' +
        ", addressName='" + addressName + '\'' +
        ", addressStreet='" + addressStreet + '\'' +
        ", addressZipCity='" + addressZipCity + '\'' +
        ", addressCountry='" + addressCountry + '\'' +
        ", registrationStatus=" + registrationStatus +
        ", registrationConfirmedOn=" + registrationConfirmedOn +
        ", ndefUrlToken='" + ndefUrlToken + '\'' +
        ", approvedApplications=" + approvedApplications +
        ", isCardIssuerDataComplete=" + isCardIssuerDataComplete +
        ", isCardDataComplete=" + isCardDataComplete +
        ", card=" + card +
        ", hasSharedSlotApproval=" + hasSharedSlotApproval +
        '}';
  }

  public static final class Builder {
    private Long id;
    private User user;
    private String company;
    private String email;
    private String cardName;
    private byte[] logo;
    private NumberOfCardsEnum numberOfCards;
    private String countryIsoCode;
    private String industrySectorNaicsNr;
    private String linkedInUrl;
    private String phone;
    private String homepage;
    private String addressName;
    private String addressStreet;
    private String addressZipCity;
    private String addressCountry;
    private CardIssuerRegistrationStatusEnum registrationStatus = com.xyz.CardIssuerRegistrationStatusEnum.UNCONFIRMED;
    private boolean isCardIssuerDataComplete;
    private boolean isCardDataComplete;
    private String ndefUrlToken;
    private CardTypeEnum cardType = CardTypeEnum.EV2;
    private KeyReference ev1MasterKeyRef;
    private KeyReference damAuthKeyRef;
    private KeyReference damMacKeyRef;
    private KeyReference damEncKeyRef;
    private Integer numberOfSharedSlots;
    private Integer quotaLimitOfSharedSlot;
    private Integer availableCardSpaceInBytes;
    private boolean hasSharedSlotApproval;
    private Long registrationConfirmedOn;

    private Builder() {
      // empty
    }

    public Builder id(final long val) {
      id = val;
      return this;
    }

    public Builder user(final User val) {
      user = val;
      return this;
    }

    public Builder company(final String val) {
      company = val;
      return this;
    }

    public Builder email(final String val) {
      email = val;
      return this;
    }

    public Builder cardName(final String val) {
      cardName = val;
      return this;
    }

    public Builder logo(final byte[] val) {
      logo = val;
      return this;
    }

    public Builder numberOfCards(final NumberOfCardsEnum val) {
      numberOfCards = val;
      return this;
    }

    public Builder countryIsoCode(final String val) {
      countryIsoCode = val;
      return this;
    }

    public Builder industrySectorNaicsNr(final String val) {
      industrySectorNaicsNr = val;
      return this;
    }

    public Builder linkedInUrl(final String val) {
      linkedInUrl = val;
      return this;
    }

    public Builder phone(final String val) {
      phone = val;
      return this;
    }

    public Builder homepage(final String val) {
      homepage = val;
      return this;
    }

    public Builder addressName(final String val) {
      addressName = val;
      return this;
    }

    public Builder addressStreet(final String val) {
      addressStreet = val;
      return this;
    }

    public Builder addressZipCity(final String val) {
      addressZipCity = val;
      return this;
    }

    public Builder addressCountry(final String val) {
      addressCountry = val;
      return this;
    }

    public Builder registrationStatus(final CardIssuerRegistrationStatusEnum val) {
      registrationStatus = val;
      return this;
    }

    public Builder registrationConfirmedOn(@Nullable final Long registrationConfirmedOn) {
      this.registrationConfirmedOn = registrationConfirmedOn;
      return this;
    }

    public Builder ndefUrlToken(final String val) {
      ndefUrlToken = val;
      return this;
    }

    public Builder cardType(final CardTypeEnum val) {
      cardType = val;
      return this;
    }

    public Builder damAuthKeyRef(final KeyReference val) {
      damAuthKeyRef = val;
      return this;
    }

    public Builder damMacKeyRef(final KeyReference val) {
      damMacKeyRef = val;
      return this;
    }

    public Builder damEncKeyRef(final KeyReference val) {
      damEncKeyRef = val;
      return this;
    }

    public Builder ev1MasterKeyRef(final KeyReference val) {
      ev1MasterKeyRef = val;
      return this;
    }

    public Builder isCardIssuerDataComplete(final boolean val) {
      isCardIssuerDataComplete = val;
      return this;
    }

    public Builder isCardDataComplete(final boolean val) {
      isCardDataComplete = val;
      return this;
    }

    public Builder numberOfSharedSlots(final int val) {
      numberOfSharedSlots = val;
      return this;
    }

    public Builder quotaLimitOfSharedSlot(final int val) {
      quotaLimitOfSharedSlot = val;
      return this;
    }

    public Builder availableCardSpaceInBytes(final int val) {
      availableCardSpaceInBytes = val;
      return this;
    }

    public Builder hasSharedSlotApproval(final boolean val) {
      hasSharedSlotApproval = val;
      return this;
    }

    public CardIssuer build() {
      return new CardIssuer(this);
    }
  }
}
