package com.xyz;


import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 * Entity Object which is representing the Application as stored in the database.
 *
 * @author Gerald Madlmayr (gerald.madlmayr@rise-world.com)
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */

@Entity
public class Application extends AbstractStrongEntity {

  private String appProviderName;
  private String appName;
  private String description;
  private String appProviderPersoEndpointUrl;
  private Integer preferredAid;
  private int numberOfKeys;
  private KeyReference applicationMasterKey;
  private Byte applicationMasterKeyVersion;

  private int quotaLimit;
  private String dfName;
  private String isoFileId;
  private String appProviderDeleteEndpointUrl;
  private byte[] logo;
  private String androidAppPackageName;

  private boolean isApplicationDataComplete;
  private Long unpublishedOn;

  private ApplicationProvider applicationProvider;
  private List<ApprovedApplication> approvedApplications;

  private boolean areShortFileIdsSupported;

  Application() {
    // empty
  }

  private Application(final Builder builder) {
    super(builder.id);
    setAppProviderName(builder.appProviderName);
    setAppName(builder.appName);
    setDescription(builder.description);
    setAppProviderPersoEndpointUrl(builder.appProviderPersoEndpointUrl);
    setPreferredAid(builder.preferredAid);
    setNumberOfKeys(builder.numberOfKeys);
    setApplicationMasterKey(builder.applicationMasterKey);
    setApplicationMasterKeyVersion(builder.applicationMasterKeyVersion);
    setQuotaLimit(builder.quotaLimit);
    setDfName(builder.dfName);
    setIsoFileId(builder.isoFileId);
    setAppProviderDeleteEndpointUrl(builder.appProviderDeleteEndpointUrl);
    setLogo(builder.logo);
    setAndroidAppPackageName(builder.androidAppPackageName);
    setApplicationProvider(builder.applicationProvider);
    setUnpublishedOn(builder.unpublishedOn);
    setAreShortFileIdsSupported(builder.areShortFileIdsSupported);
  }

  @Column(length = 128, nullable = false)
  public String getAppProviderName() {
    return appProviderName;
  }

  public void setAppProviderName(final String company) {
    this.appProviderName = company;
  }

  @Column(length = 128, nullable = false)
  public String getAppName() {
    return appName;
  }

  public void setAppName(final String applicationName) {
    this.appName = applicationName;
  }

  @Column
  public Integer getPreferredAid() {
    return preferredAid;
  }

  @SuppressWarnings("AssignmentToCollectionOrArrayFieldFromParameter")
  public void setPreferredAid(final Integer preferredAid) {
    this.preferredAid = preferredAid;
  }

  @Column(nullable = false)
  public int getNumberOfKeys() {
    return numberOfKeys;
  }

  public void setNumberOfKeys(final int numberOfKeys) {
    this.numberOfKeys = numberOfKeys;
  }

  @Column
  public Byte getApplicationMasterKeyVersion() {
    return applicationMasterKeyVersion;
  }

  public void setApplicationMasterKeyVersion(final Byte applicationMasterKeyVersion) {
    this.applicationMasterKeyVersion = applicationMasterKeyVersion;
  }

  @ManyToOne(optional = true, targetEntity = KeyReference.class, cascade = CascadeType.PERSIST)
  public KeyReference getApplicationMasterKey() {
    return applicationMasterKey;
  }

  public void setApplicationMasterKey(final KeyReference applicationMasterKey) {
    this.applicationMasterKey = applicationMasterKey;
  }

  @Column(length = 1024, nullable = false)
  public String getDescription() {
    return description;
  }

  public void setDescription(final String shortDescription) {
    this.description = shortDescription;
  }

  @Column(length = 1024, nullable = false)
  public String getAppProviderPersoEndpointUrl() {
    return appProviderPersoEndpointUrl;
  }

  public void setAppProviderPersoEndpointUrl(final String appProviderPersoEndpointUrl) {
    this.appProviderPersoEndpointUrl = appProviderPersoEndpointUrl;
  }

  @OneToMany(mappedBy = "application")
  public List<ApprovedApplication> getApprovedApplications() {
    return approvedApplications;
  }

  public void setApprovedApplications(final List<ApprovedApplication> approvedApplications) {
    this.approvedApplications = approvedApplications;
  }

  @Column(nullable = false)
  public int getQuotaLimit() {
    return quotaLimit;
  }

  public void setQuotaLimit(final int quotaLimit) {
    this.quotaLimit = quotaLimit;
  }

  @Column(length = 32, nullable = true)
  public String getDfName() {
    return dfName;
  }

  public void setDfName(final String dfName) {
    this.dfName = dfName;
  }

  @Column(length = 4, nullable = true)
  public String getIsoFileId() {
    return isoFileId;
  }

  public void setIsoFileId(final String isoFileId) {
    this.isoFileId = isoFileId;
  }

  @Column(length = 1024, nullable = false)
  public String getAppProviderDeleteEndpointUrl() {
    return appProviderDeleteEndpointUrl;
  }

  public void setAppProviderDeleteEndpointUrl(final String appProviderDeleteEndpointUrl) {
    this.appProviderDeleteEndpointUrl = appProviderDeleteEndpointUrl;
  }

  @Column(length = 1000000, nullable = true)
  public byte[] getLogo() {
    return logo;
  }

  public void setLogo(final byte[] logo) {
    this.logo = logo;
  }

  @Column
  public String getAndroidAppPackageName() {
    return androidAppPackageName;
  }

  public void setAndroidAppPackageName(final String androidAppPackageName) {
    this.androidAppPackageName = androidAppPackageName;
  }

  @ManyToOne(optional = false, targetEntity = ApplicationProvider.class)
  public ApplicationProvider getApplicationProvider() {
    return applicationProvider;
  }

  public void setApplicationProvider(final ApplicationProvider applicationProvider) {
    this.applicationProvider = applicationProvider;
  }

  @Column
  public Long getUnpublishedOn() {
    return unpublishedOn;
  }

  public void setUnpublishedOn(final Long unpublishedOn) {
    this.unpublishedOn = unpublishedOn;
  }

  @Transient
  public boolean isApplicationDataComplete() {
    return isApplicationDataComplete;
  }

  public void isApplicationDataComplete(final boolean isApplicationDataComplete) {
    this.isApplicationDataComplete = isApplicationDataComplete;
  }

  @Transient
  public ApplicationStatusEnum getStatus() {
    if (!isApplicationDataComplete()) {
      return ApplicationStatusEnum.MISSING_DATA;
    } else if (getUnpublishedOn() != null) {
      return ApplicationStatusEnum.UNPUBLISHED;
    } else {
      return ApplicationStatusEnum.PUBLISHED;
    }
  }

  @Column(nullable = false)
  public boolean getAreShortFileIdsSupported() {
    return areShortFileIdsSupported;
  }

  public void setAreShortFileIdsSupported(final boolean areShortFileIdsSupported) {
    this.areShortFileIdsSupported = areShortFileIdsSupported;
  }

  public boolean hasApplicationMasterKey() {
    return (applicationMasterKey != null);
  }

  @Override
  public boolean equals(@Nullable final Object o) {
    if (this == o) {
      return true;
    }

    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }

    final Application that = (Application) o;
    return Objects.equals(getApplicationProvider(), that.getApplicationProvider());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getApplicationProvider());
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public String toString() {
    return "Application{" +
        "id='" + getId() + '\'' +
        ", appProviderName='" + appProviderName + '\'' +
        ", appName='" + appName + '\'' +
        ", description='" + description + '\'' +
        ", appProviderPersoEndpointUrl='" + appProviderPersoEndpointUrl + '\'' +
        ", preferredAid=" + preferredAid +
        ", numberOfKeys=" + numberOfKeys +
        ", applicationMasterKey='" + applicationMasterKey + '\'' +
        ", quotaLimit=" + quotaLimit +
        ", dfName='" + dfName + '\'' +
        ", isoFileId='" + isoFileId + '\'' +
        ", appProviderDeleteEndpointUrl='" + appProviderDeleteEndpointUrl + '\'' +
        ", androidAppPackageName='" + androidAppPackageName + '\'' +
        ", isApplicationDataComplete=" + isApplicationDataComplete +
        ", unpublishedOn=" + unpublishedOn +
        ", approvedApplications=" + approvedApplications +
        ", applicationMasterKey=" + applicationMasterKey +
        ", applicationMasterKeyVersion=" + applicationMasterKeyVersion +
        ", areShortFileIdsSupported=" + areShortFileIdsSupported +
        '}';
  }

  public static final class Builder {
    private Long id;
    private String appProviderName;
    private String appName;
    private String description;
    private String appProviderPersoEndpointUrl;
    private Integer preferredAid;
    private int numberOfKeys;
    private int quotaLimit;
    private String dfName;
    private String isoFileId;
    private String appProviderDeleteEndpointUrl;
    private byte[] logo;
    private ApplicationProvider applicationProvider;
    private String androidAppPackageName;
    private Long unpublishedOn;
    private KeyReference applicationMasterKey;
    private byte applicationMasterKeyVersion;
    private boolean areShortFileIdsSupported;

    private Builder() {
      // empty
    }

    public Builder id(final long val) {
      id = val;
      return this;
    }

    public Builder appProviderName(final String val) {
      appProviderName = val;
      return this;
    }

    public Builder appName(final String val) {
      appName = val;
      return this;
    }

    public Builder description(final String val) {
      description = val;
      return this;
    }

    public Builder appProviderPersoEndpointUrl(final String val) {
      appProviderPersoEndpointUrl = val;
      return this;
    }

    public Builder preferredAid(final Integer val) {
      preferredAid = val;
      return this;
    }

    public Builder numberOfKeys(final int val) {
      numberOfKeys = val;
      return this;
    }

    public Builder applicationMasterKey(final KeyReference val) {
      applicationMasterKey = val;
      return this;
    }

    public Builder applicationMasterKeyVersion(final byte val) {
      applicationMasterKeyVersion = val;
      return this;
    }

    public Builder quotaLimit(final int val) {
      quotaLimit = val;
      return this;
    }

    public Builder dfName(final String val) {
      dfName = val;
      return this;
    }

    public Builder isoFileId(final String val) {
      isoFileId = val;
      return this;
    }

    public Builder appProviderDeleteEndpointUrl(final String val) {
      appProviderDeleteEndpointUrl = val;
      return this;
    }

    public Builder logo(final byte[] val) {
      logo = val;
      return this;
    }

    public Builder androidAppPackageName(final String val) {
      androidAppPackageName = val;
      return this;
    }

    public Builder applicationProvider(final ApplicationProvider val) {
      applicationProvider = val;
      return this;
    }

    public Builder unpublishedOn(final Long val) {
      unpublishedOn = val;
      return this;
    }

    public Builder areShortFileIdsSupported(final boolean val) {
      areShortFileIdsSupported = val;
      return this;
    }

    public Application build() {
      return new Application(this);
    }
  }
}
