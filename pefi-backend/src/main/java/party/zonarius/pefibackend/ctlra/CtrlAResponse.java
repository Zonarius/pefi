package party.zonarius.pefibackend.ctlra;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jparsec.error.ParserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@RequiredArgsConstructor
public abstract sealed class CtrlAResponse {
    private final ResponseType type;

    public ResponseEntity<CtrlAResponse> toResponseEntity() {
        return new ResponseEntity<>(this, type.httpStatus);
    }

    @Getter
    @RequiredArgsConstructor
    public enum ResponseType {
        SAVED("saved", HttpStatus.OK),
        PARSER_ERROR("parser_error", HttpStatus.BAD_REQUEST),
        NOT_OVERLAPPING("not_overlapping", HttpStatus.BAD_REQUEST);

        @JsonValue
        private final String name;
        private final HttpStatus httpStatus;
    }

    @Getter
    public static final class Saved extends CtrlAResponse {
        private final int itemsSaved;
        public Saved(int itemsSaved) {
            super(ResponseType.SAVED);
            this.itemsSaved = itemsSaved;
        }
    }

    @Getter
    public static final class ParserError extends CtrlAResponse {
        private final String message;
        public ParserError(ParserException ex) {
            super(ResponseType.PARSER_ERROR);
            message = ex.getMessage();
        }
    }

    @Getter
    public static final class NotOverlapping extends CtrlAResponse {
        public NotOverlapping() {
            super(ResponseType.NOT_OVERLAPPING);
        }
    }
}
