package com.xyz;

import static com.xyz.WebUtils.writeJsonFieldIfNotNull;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Maps an {@link ApplicationProvider} to json.
 *
 * @author Martin Reiterer (martin.reiterer@rise-world.com)
 */
public class ApplicationProviderSerializer extends StdSerializer<ApplicationProvider> {

	  public ApplicationProviderSerializer() {
	    super(ApplicationProvider.class);
	  }

	  @Override
	  public void serialize(final ApplicationProvider applicationProvider, final JsonGenerator gen, final SerializerProvider provider) throws
	      IOException {
	    gen.writeStartObject();

	    gen.writeNumberField("id", applicationProvider.getId().intValue());
	    writeJsonFieldIfNotNull(gen, "name", applicationProvider.getName());
	    writeJsonFieldIfNotNull(gen, "username", applicationProvider.getUser().getUsername());
	    writeJsonFieldIfNotNull(gen, "email", applicationProvider.getEmail());
	    writeJsonFieldIfNotNull(gen, "countryIsoCode", applicationProvider.getCountryIsoCode());
	    writeJsonFieldIfNotNull(gen, "industrySectorNaicsNr", applicationProvider.getIndustrySectorNaicsNr());
	    writeJsonFieldIfNotNull(gen, "linkedInUrl", applicationProvider.getLinkedInUrl());
	    writeJsonFieldIfNotNull(gen, "phone", applicationProvider.getPhone());
	    writeJsonFieldIfNotNull(gen, "homepage", applicationProvider.getHomepage());
	    writeJsonFieldIfNotNull(gen, "addressName", applicationProvider.getAddressName());
	    writeJsonFieldIfNotNull(gen, "addressStreet", applicationProvider.getAddressStreet());
	    writeJsonFieldIfNotNull(gen, "addressZipCity", applicationProvider.getAddressZipCity());
	    writeJsonFieldIfNotNull(gen, "addressCountry", applicationProvider.getAddressCountry());

	    gen.writeStringField("registrationStatus", applicationProvider.getRegistrationStatus().name());
	    gen.writeObjectField("application", applicationProvider.getApplication());

	    gen.writeEndObject();
	  }
	}
