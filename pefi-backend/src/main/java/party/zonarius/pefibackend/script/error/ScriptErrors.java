package party.zonarius.pefibackend.script.error;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ScriptErrors {
    private List<? extends ScriptError> errors;
}
