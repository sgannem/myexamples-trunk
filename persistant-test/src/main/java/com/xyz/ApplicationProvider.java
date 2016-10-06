package com.xyz;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 * Entity Object which is representing the Application Provider as stored in the database.
 *
 * @author Gerald Madlmayr (gerald.madlmayr@rise-world.com)
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
@Entity
public class ApplicationProvider extends AbstractStrongEntity {

  @Embedded
  private User user;

  private String name;

  private String email;

  private String countryIsoCode;

  private String industrySectorNaicsNr;

  private String linkedInUrl;

  private String phone;

  private String homepage;

  private String addressName;

  private String addressStreet;

  private String addressZipCity;

  private String addressCountry;

  private Long registrationConfirmedOn;

  private List<Application> applications = new ArrayList<>();

  private ApplicationProviderRegistrationStatusEnum registrationStatus;

  ApplicationProvider() {
    super();
  }

  private ApplicationProvider(final Builder builder) {
    super(builder.id);
    setUser(builder.user);
    setName(builder.name);
    setEmail(builder.email);
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
  }

  @OneToMany(mappedBy = "applicationProvider", cascade = CascadeType.PERSIST)
  public List<Application> getApplications() {
    return applications;
  }

  public void setApplications(final List<Application> applications) {
    this.applications = applications;
  }

  public User getUser() {
    return user;
  }

  public void setUser(final User user) {
    this.user = user;
  }

  @Column
  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @Column
  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  @Enumerated(EnumType.STRING)
  public ApplicationProviderRegistrationStatusEnum getRegistrationStatus() {
    return registrationStatus;
  }

  private void setRegistrationStatus(final ApplicationProviderRegistrationStatusEnum registrationStatus) {
    this.registrationStatus = registrationStatus;
  }

  public Long getRegistrationConfirmedOn() {
    return registrationConfirmedOn;
  }

  void setRegistrationConfirmedOn(final Long registrationConfirmedOn) {
    this.registrationConfirmedOn = registrationConfirmedOn;
  }

  public void markAsConfirmed(final long now) {
    this.registrationStatus = ApplicationProviderRegistrationStatusEnum.CONFIRMED;
    this.registrationConfirmedOn = now;
  }

  @Transient
  public boolean isConfirmed() {
    return (this.registrationStatus == ApplicationProviderRegistrationStatusEnum.CONFIRMED);
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

  @Transient
  public Application getApplication() {
    final List<Application> theApplications = getApplications();
    assert theApplications.size() == 1;
    return theApplications.get(0);
  }

  public void setApplication(final Application application) {
    assert applications.isEmpty();
    getApplications().add(application);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }

    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }

    final ApplicationProvider that = (ApplicationProvider) o;
    return Objects.equals(getUser(), that.getUser());
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
    return "ApplicationProvider{" +
        "user=" + user +
        ", name='" + name + '\'' +
        ", email='" + email + '\'' +
        ", countryIsoCode='" + countryIsoCode + '\'' +
        ", industrySectorNaicsNr='" + industrySectorNaicsNr + '\'' +
        ", linkedInUrl='" + linkedInUrl + '\'' +
        ", phone='" + phone + '\'' +
        ", homepage='" + homepage + '\'' +
        ", addressName='" + addressName + '\'' +
        ", addressStreet='" + addressStreet + '\'' +
        ", addressZipCity='" + addressZipCity + '\'' +
        ", addressCountry='" + addressCountry + '\'' +
        ", applications=" + applications +
        ", registrationStatus=" + registrationStatus +
        ", registrationConfirmedOn=" + registrationConfirmedOn +
        '}';
  }

  public static final class Builder {
    private Long id;
    private User user;
    private String name;
    private String email;
    private String countryIsoCode;
    private String industrySectorNaicsNr;
    private String linkedInUrl;
    private String phone;
    private String homepage;
    private String addressName;
    private String addressStreet;
    private String addressZipCity;
    private String addressCountry;
    private Long registrationConfirmedOn;
    private ApplicationProviderRegistrationStatusEnum registrationStatus;

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

    public Builder name(final String val) {
      name = val;
      return this;
    }

    public Builder email(final String val) {
      email = val;
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

    public Builder registrationStatus(final ApplicationProviderRegistrationStatusEnum val) {
      registrationStatus = val;
      return this;
    }

    public Builder registrationConfirmedOn(@Nullable final Long registrationConfirmedOn) {
      this.registrationConfirmedOn = registrationConfirmedOn;
      return this;
    }

    public ApplicationProvider build() {
      return new ApplicationProvider(this);
    }
  }
}
