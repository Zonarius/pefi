package party.zonarius.pefibackend.script.error;

import lombok.Data;

@Data
public abstract class ScriptError {
    private final ScriptErrorType type;
}
