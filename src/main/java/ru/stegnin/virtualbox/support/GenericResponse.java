package ru.stegnin.virtualbox.support;

import lombok.Data;

@Data
public class GenericResponse {
    private String message;
    private String error;

    public static class Builder {
        private GenericResponse newMessage;

        public Builder() {
            newMessage = new GenericResponse();
        }

        public Builder withMessage(String message) {
            newMessage.message = message;
            return this;
        }

        public Builder withError(String error) {
            newMessage.error = error;
            return this;
        }

        public GenericResponse build() {
            return newMessage;
        }
    }
}
