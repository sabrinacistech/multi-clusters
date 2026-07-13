package ar.com.paas.cluster.status.monitor.infrastructure.adapter.out.mongo;

import ar.com.paas.cluster.status.monitor.application.port.out.AliasStatusRepositoryPort;
import ar.com.paas.cluster.status.monitor.domain.exception.PersistenceException;
import ar.com.paas.cluster.status.monitor.domain.model.AliasStatus;
import ar.com.paas.cluster.status.monitor.infrastructure.adapter.out.mongo.repository.AliasStatusMongoRepository;
import ar.com.paas.cluster.status.monitor.infrastructure.mapper.AliasStatusDocumentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia para estados de alias.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliasStatusPersistenceAdapter implements AliasStatusRepositoryPort {

    private final AliasStatusMongoRepository repository;
    private final AliasStatusDocumentMapper mapper;

    @Override
    public AliasStatus save(AliasStatus status) {
        try {
            return mapper.toDomain(repository.save(mapper.toDocument(status)));
        } catch (DataAccessException ex) {
            throw new PersistenceException("Error guardando alias", ex);
        }
    }

    @Override
    public List<AliasStatus> findAll() {
        try {
            return repository.findAll().stream().map(mapper::toDomain).toList();
        } catch (DataAccessException ex) {
            throw new PersistenceException("Error listando alias", ex);
        }
    }

    @Override
    public Optional<AliasStatus> findByAlias(String alias) {
        try {
            return repository.findByAlias(alias).map(mapper::toDomain);
        } catch (DataAccessException ex) {
            throw new PersistenceException("Error buscando alias: " + alias, ex);
        }
    }

    @Override
    public void deleteByAlias(String alias) {
        try {
            repository.deleteByAlias(alias);
        } catch (DataAccessException ex) {
            throw new PersistenceException("Error eliminando alias: " + alias, ex);
        }
    }
}
