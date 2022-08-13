package party.zonarius.pefibackend.script;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import party.zonarius.pefibackend.db.entity.ApplicationEntity;
import party.zonarius.pefibackend.db.repository.ApplicationRepository;
import party.zonarius.pefibackend.db.repository.TransactionRepository;
import party.zonarius.pefibackend.script.error.MissingTagError;
import party.zonarius.pefibackend.script.error.ScriptErrors;
import party.zonarius.pefibackend.script.executor.ScriptExecutor;
import party.zonarius.pefibackend.script.upload.ScriptUploadRequest;

import java.util.List;

@RestController
@RequestMapping("api/script")
@RequiredArgsConstructor
public class ScriptController {

    private final TransactionRepository transactionRepository;
    private final ApplicationRepository applicationRepository;
    private final ScriptExecutor scriptExecutor;

    @GetMapping("/errors")
    public ScriptErrors scriptErrors() {
        List<MissingTagError> missingTagErrors = transactionRepository.findAllWithMissingTag().stream()
            .map(MissingTagError::new)
            .toList();

        return new ScriptErrors(missingTagErrors);
    }

    @GetMapping
    public LoadScriptResponse loadScript() {
        String script = applicationRepository.getApplication().getScript();
        return new LoadScriptResponse(script);
    }

    @PostMapping
    public void uploadScript(@RequestBody ScriptUploadRequest request) {
        saveScript(request.getScript());
        scriptExecutor.applyScript();
    }

    private void saveScript(String request) {
        ApplicationEntity application = applicationRepository.getApplication();
        application.setScript(request);
        applicationRepository.saveAndFlush(application);
    }
}
