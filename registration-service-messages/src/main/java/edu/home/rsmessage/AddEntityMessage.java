package edu.home.rsmessage;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
    @JsonSubTypes.Type(AddServiceMessage.class),
    @JsonSubTypes.Type(AddEventMessage.class),
    @JsonSubTypes.Type(AddParameterMessage.class)
})
public abstract class AddEntityMessage {

    private String name;

    private String caption;

    abstract static class Builder<T extends Builder<T>> {
        private String name;

        private String caption;

        public T setName(String name) {
            this.name = name;
            return self();
        }

        public T setCaption(String caption) {
            this.caption = caption;
            return self();
        }

        abstract AddEntityMessage build();

        protected abstract T self();
    }

    AddEntityMessage(Builder<?> builder) {
        this.name = builder.name;
        this.caption = builder.caption;
    }

    public String getName() {
        return name;
    }

    public String getCaption() {
        return caption;
    }
}