package party.zonarius.pefibackend.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import party.zonarius.pefibackend.db.entity.ApplicationEntity;
import party.zonarius.pefibackend.db.repository.ApplicationRepository;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
public class ApplicationConfiguration {
    private final ApplicationRepository applicationRepository;

    @PostConstruct
    @Transactional
    public void initializeApplicationEntity() {
        if (applicationRepository.findById(1).isEmpty()) {
            ApplicationEntity applicationEntity = ApplicationEntity.builder()
                .id(1)
                .script("""
                    /**
                     * This function is called for every transaction in the export.
                     * The return value will be assigned to the transaction.
                     *
                     * @param   {Object} tx                 The transaction
                     * @param   {string} tx.iban            The IBAN of the transaction
                     * @param   {string} tx.description     The description of the transaction, lines are split by "|"
                     * @param   {string} tx.paymentDate     The date of the payment, ISO-formatted (ex. "2022-12-31")
                     * @param   {string} tx.transactionDate The date of the transaction, ISO-formatted (ex. "2022-12-31")
                     * @param   {number} tx.amount          The amount of the transaction, positive for income, negative for costs
                     * @param   {string} tx.currency        The currency of the transaction (ex. "EUR")
                     * @returns {string|undefined}          Tag of the transaction, separated by dots (ex. "essen.geschäft.billa"). Undefined or empty string if this script does not assign a tag to the transaction.
                     */
                    function getTag(tx) {
                        
                    }
                    """.stripIndent())
                .build();
            applicationRepository.saveAndFlush(applicationEntity);
        }
    }
}
