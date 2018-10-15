package ru.stegnin.virtualbox.settings.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class CustomDateSerializer extends StdSerializer<LocalDate> {

    public CustomDateSerializer() {
        this(null);
    }

    public CustomDateSerializer(Class t) {
        super(t);
    }

    @Override
    public void serialize (LocalDate value, JsonGenerator gen, SerializerProvider arg2)
            throws IOException {
        gen.writeString(value.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())));
    }
}
