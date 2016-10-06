package fr.redfroggy.sample.derivation.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * Settings
 */
@Component
@ConfigurationProperties
@Data
public class Settings {

    /**
     * Derivation standard to use
     * default: AN10922_AES128
     */
    @NotNull
    protected String standard = "AN10922_AES128";

    /**
     * Key to diversify
     * default: 00000000000000000000000000000000
     */
    @NotNull
    protected String key = "00000000000000000000000000000000";

    /**
     * Key to diversify
     * default: 00000000000000000000000000000000
     */
    @NotNull
    protected String uid = "00000000000000";

    /**
     * Key to diversify (AN10922 only)
     * default: 000000
     */
    protected String aid = "000000";

    /**
     * Key to diversify (AN10922 only)
     * default: 00
     */
    protected String systemIdentifier = "00";

    /**
     * Index of key to diversify (AN0148 only)
     * default: 0
     */
    protected int keyIndex = 0;

	/**
	 * @return the standard
	 */
	public String getStandard() {
		return standard;
	}

	/**
	 * @param standard the standard to set
	 */
	public void setStandard(String standard) {
		this.standard = standard;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the aid
	 */
	public String getAid() {
		return aid;
	}

	/**
	 * @param aid the aid to set
	 */
	public void setAid(String aid) {
		this.aid = aid;
	}

	/**
	 * @return the systemIdentifier
	 */
	public String getSystemIdentifier() {
		return systemIdentifier;
	}

	/**
	 * @param systemIdentifier the systemIdentifier to set
	 */
	public void setSystemIdentifier(String systemIdentifier) {
		this.systemIdentifier = systemIdentifier;
	}

	/**
	 * @return the keyIndex
	 */
	public int getKeyIndex() {
		return keyIndex;
	}

	/**
	 * @param keyIndex the keyIndex to set
	 */
	public void setKeyIndex(int keyIndex) {
		this.keyIndex = keyIndex;
	}
    
    
    

}
