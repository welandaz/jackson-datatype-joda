package com.fasterxml.jackson.datatype.joda.deser;

import java.io.IOException;

import org.joda.time.Instant;

import com.fasterxml.jackson.core.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.joda.cfg.FormatConfig;
import com.fasterxml.jackson.datatype.joda.cfg.JacksonJodaDateFormat;

/**
 * Basic deserializer for {@link org.joda.time.ReadableDateTime} and its subtypes.
 * Accepts JSON String and Number values and passes those to single-argument constructor.
 * Does not (yet?) support JSON object; support can be added if desired.
 */
public class InstantDeserializer
    extends JodaDateDeserializerBase<Instant>
{
    public InstantDeserializer() {
        this(FormatConfig.DEFAULT_DATETIME_PARSER);
    }

    public InstantDeserializer(JacksonJodaDateFormat format) {
        super(Instant.class, format);
    }

    @Override
    public JodaDateDeserializerBase<?> withFormat(JacksonJodaDateFormat format) {
        return new InstantDeserializer(format);
    }

    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
    {
        JsonToken t = p.currentToken();
        if (t == JsonToken.VALUE_NUMBER_INT) {
            return new Instant(p.getLongValue());
        }
        if (t == JsonToken.VALUE_STRING) {
            String str = p.getText().trim();
            if (str.length() == 0) {
                return null;
            }
            // 11-Sep-2018, tatu: `DateTimeDeserializer` allows timezone inclusion in brackets;
            //    should that be checked here too?
            return Instant.parse(str, _format.createParser(ctxt));
        }
        return _handleNotNumberOrString(p, ctxt);
    }
}
