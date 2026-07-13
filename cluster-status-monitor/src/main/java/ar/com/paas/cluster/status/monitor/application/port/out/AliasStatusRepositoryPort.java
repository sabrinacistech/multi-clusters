package ar.com.paas.cluster.status.monitor.application.port.out;

import ar.com.paas.cluster.status.monitor.domain.model.AliasStatus;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de estados de alias.
 */
public interface AliasStatusRepositoryPort {

    AliasStatus save(AliasStatus status);

    List<AliasStatus> findAll();

    Optional<AliasStatus> findByAlias(String alias);

    void deleteByAlias(String alias);
}
