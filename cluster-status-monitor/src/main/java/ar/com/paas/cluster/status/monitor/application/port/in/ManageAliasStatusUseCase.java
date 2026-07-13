package ar.com.paas.cluster.status.monitor.application.port.in;

import ar.com.paas.cluster.status.monitor.domain.model.AliasStatus;

import java.util.List;

/**
 * Caso de uso ABM de estados de alias.
 */
public interface ManageAliasStatusUseCase {

    List<AliasStatus> list();

    AliasStatus create(AliasStatus s);

    AliasStatus update(String alias, AliasStatus s);

    void delete(String alias);
}
