package fr.redfroggy.sample.derivation.security;

import lombok.Getter;

/**
 * Algorithms authorized
 */
public enum Algorithm {
    DES("DESede/CBC/NoPadding", "DESede", 8),
    TDES("DESede/CBC/NoPadding", "DESede", 8),
    TKTDES("DESede/CBC/NoPadding", "DESede", 8),
    AES("AES/CBC/NoPadding", "AES", 16),
    AES192("AES/CBC/NoPadding", "AES", 16),
    RSA("RSA", "RSA", 0);

    @Getter
    private String cipherAlgorithm;

    @Getter
    private String keyAlgorithm;

    @Getter
    private int blocSize;

    Algorithm(String cipherAlgorithm, String keyAlgorithm, int blocSize) {
        this.cipherAlgorithm = cipherAlgorithm;
        this.keyAlgorithm = keyAlgorithm;
        this.blocSize = blocSize;
    }

	/**
	 * @return the cipherAlgorithm
	 */
	public String getCipherAlgorithm() {
		return cipherAlgorithm;
	}

	/**
	 * @param cipherAlgorithm the cipherAlgorithm to set
	 */
	public void setCipherAlgorithm(String cipherAlgorithm) {
		this.cipherAlgorithm = cipherAlgorithm;
	}

	/**
	 * @return the keyAlgorithm
	 */
	public String getKeyAlgorithm() {
		return keyAlgorithm;
	}

	/**
	 * @param keyAlgorithm the keyAlgorithm to set
	 */
	public void setKeyAlgorithm(String keyAlgorithm) {
		this.keyAlgorithm = keyAlgorithm;
	}

	/**
	 * @return the blocSize
	 */
	public int getBlocSize() {
		return blocSize;
	}

	/**
	 * @param blocSize the blocSize to set
	 */
	public void setBlocSize(int blocSize) {
		this.blocSize = blocSize;
	}
    
    
}