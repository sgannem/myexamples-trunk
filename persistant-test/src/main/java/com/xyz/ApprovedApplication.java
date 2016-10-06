package com.xyz;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;


/**
 * Entity Object which resolves the M-2-N Relation between the {@link Application} and the {@link CardIssuer}. The Entity contains the AID
 * on the Card of the Card Issuer which can differ from the {@link Application#preferredAid}
 *
 * @author Gerald Madlmayr (gerald.madlmayr@rise-world.com)
 * @author Alexander Zapletal (alexander.zapletal@rise-world.com)
 */
@Entity
public class ApprovedApplication extends AbstractStrongEntity {

  private Application application;
  private CardIssuer cardIssuer;
  private int aid;
  private long approvalOn;
  private Long revocationOn;
  private ApprovalTypeEnum approvalType;

  ApprovedApplication() {
    this.application = null;
    this.cardIssuer = null;
    this.aid = 0;
    this.approvalOn = 0;
    this.revocationOn = null;
  }

  private ApprovedApplication(final BuilderForTest builder) {
    super(builder.id, 0L);
    setApplication(builder.application);
    setCardIssuer(builder.cardIssuer);
    setAid(builder.aid);
    setApprovalOn(builder.approvalOn);
    setRevocationOn(builder.revocationOn);
    setApprovalType(builder.approvalType);
  }

  private ApprovedApplication(final Builder builder) {
    super(null, 0L);
    setApplication(builder.application);
    setCardIssuer(builder.cardIssuer);
    setAid(builder.aid);
    setApprovalOn(builder.approvalOn);
    setRevocationOn(builder.revocationOn);
    setApprovalType(builder.approvalType);
  }

  @ManyToOne(optional = false, targetEntity = Application.class)
  public Application getApplication() {
    return application;
  }

  void setApplication(final Application application) {
    this.application = application;
  }

  @ManyToOne(optional = false, targetEntity = CardIssuer.class)
  public CardIssuer getCardIssuer() {
    return cardIssuer;
  }

  void setCardIssuer(final CardIssuer cardIssuer) {
    this.cardIssuer = cardIssuer;
  }

  @Column(nullable = false)
  public int getAid() {
    return aid;
  }

  @SuppressWarnings("AssignmentToCollectionOrArrayFieldFromParameter")
  public void setAid(final int aid) {
    this.aid = aid;
  }

  @Column
  public Long getApprovalOn() {
    return approvalOn;
  }

  public void setApprovalOn(final Long approvalOn) {
    this.approvalOn = approvalOn;
  }

  @Column
  public Long getRevocationOn() {
    return revocationOn;
  }

  public void setRevocationOn(final Long revocationOn) {
    this.revocationOn = revocationOn;
  }

  @Enumerated(EnumType.STRING)
  public ApprovalTypeEnum getApprovalType() {
    return approvalType;
  }

  void setApprovalType(final ApprovalTypeEnum approvalType) {
    this.approvalType = approvalType;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }

    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }

    final ApprovedApplication that = (ApprovedApplication) o;
    return Objects.equals(getApplication(), that.getApplication()) && Objects.equals(getCardIssuer(), that.getCardIssuer());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getApplication(), getCardIssuer());
  }

  public static Builder builder() {
    return new Builder();
  }

  public static BuilderForTest builderForTest() {
    return new BuilderForTest();
  }


  @Override
  public String toString() {
    return "ApprovedApplication{" +
        "application.id=" + getApplication().getId() +
        ", cardIssuer.id=" + getCardIssuer().getId() +
        ", aid=" + aid +
        ", approvalOn=" + approvalOn +
        ", revocationOn=" + revocationOn +
        ", approvalType=" + approvalType +
        '}';
  }


  public static class Builder {
    protected Application application;
    protected CardIssuer cardIssuer;
    protected int aid;
    protected Long approvalOn;
    protected Long revocationOn;
    protected ApprovalTypeEnum approvalType;

    private Builder() {
      // empty
    }

    public Builder application(final Application val) {
      application = val;
      return this;
    }

    public Builder cardIssuer(final CardIssuer val) {
      cardIssuer = val;
      return this;
    }

    public Builder aid(final int val) {
      aid = val;
      return this;
    }

    public Builder approvalOn(final Long val) {
      approvalOn = val;
      return this;
    }

    public Builder revocationOn(final Long val) {
      revocationOn = val;
      return this;
    }

    public Builder approvalType(final ApprovalTypeEnum val) {
      approvalType = val;
      return this;
    }

    public ApprovedApplication build() {
      return new ApprovedApplication(this);
    }
  }

  public static class BuilderForTest extends Builder {
    private Long id;

    public Builder id(final Long val) {
      id = val;
      return this;
    }

    @Override
    public ApprovedApplication build() {
      return new ApprovedApplication(this);
    }
  }
}
