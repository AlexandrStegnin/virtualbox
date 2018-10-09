package ru.stegnin.virtualbox.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomDateSerializer extends StdSerializer<LocalDate> {
    private SimpleDateFormat formatter
            = new SimpleDateFormat("yyyy-MM-dd");

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
