package ru.stegnin.virtualbox.support;

import lombok.Data;

@Data
public class GenericResponse {
    private String message;

    public static class Builder {
        private GenericResponse newMessage;

        public Builder() {
            newMessage = new GenericResponse();
        }

        public Builder withMessage(String message) {
            newMessage.message = message;
            return this;
        }

        public GenericResponse build() {
            return newMessage;
        }
    }
}
