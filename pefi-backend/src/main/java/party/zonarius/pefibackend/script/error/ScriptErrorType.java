package party.zonarius.pefibackend.script.error;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ScriptErrorType {
    MISSING_TAG_ERROR("missingTagError");

    @JsonValue
    private final String name;
}
