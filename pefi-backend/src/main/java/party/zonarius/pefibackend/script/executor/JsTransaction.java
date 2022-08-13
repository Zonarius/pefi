package party.zonarius.pefibackend.script.executor;

import lombok.Builder;
import org.graalvm.polyglot.HostAccess;

@Builder
public class JsTransaction {
     @HostAccess.Export
     public String iban;
     @HostAccess.Export
     public String description;
     @HostAccess.Export
     public String paymentDate;
     @HostAccess.Export
     public String transactionDate;
     @HostAccess.Export
     public double amount;
     @HostAccess.Export
     public String currency;
}
