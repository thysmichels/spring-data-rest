package org.springframework.data.rest.repository;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Abstract class used as a helper for those classes that need access to the exported repositories.
 *
 * @author Jon Brisbin <jbrisbin@vmware.com>
 */
public abstract class RepositoryExporterSupport<S extends RepositoryExporterSupport<? super S>> {

  @Autowired
  protected List<RepositoryExporter> repositoryExporters = Collections.emptyList();

  /**
   * Get a List of {@link RepositoryExporter}s.
   *
   * @return
   */
  public List<RepositoryExporter> getRepositoryExporters() {
    return repositoryExporters;
  }

  /**
   * Set the List of {@link RepositoryExporter}s.
   *
   * @param repositoryExporters
   */
  public void setRepositoryExporters(List<RepositoryExporter> repositoryExporters) {
    this.repositoryExporters = repositoryExporters;
  }

  /**
   * Get a List of {@link RepositoryExporter}s.
   *
   * @return
   */
  public List<RepositoryExporter> repositoryExporters() {
    return repositoryExporters;
  }

  /**
   * Set the List of {@link RepositoryExporter}s.
   *
   * @param repositoryExporters
   */
  @SuppressWarnings({"unchecked"})
  public S repositoryExporters(List<RepositoryExporter> repositoryExporters) {
    this.repositoryExporters = repositoryExporters;
    return (S) this;
  }

  @SuppressWarnings({"unchecked"})
  protected RepositoryMetadata repositoryMetadataFor(String name) {
    for (RepositoryExporter exporter : repositoryExporters) {
      RepositoryMetadata repoMeta = exporter.repositoryMetadataFor(name);
      if (null != repoMeta) {
        return repoMeta;
      }
    }
    throw new RepositoryNotFoundException("No repository found for name " + name);
  }

  @SuppressWarnings({"unchecked"})
  protected RepositoryMetadata repositoryMetadataFor(Class<?> domainType) {
    for (RepositoryExporter exporter : repositoryExporters) {
      RepositoryMetadata repoMeta = exporter.repositoryMetadataFor(domainType);
      if (null != repoMeta) {
        return repoMeta;
      }
    }
    throw new RepositoryNotFoundException("No repository found for type " + domainType.getName());
  }

  @SuppressWarnings({"unchecked"})
  protected RepositoryMetadata repositoryMetadataFor(AttributeMetadata attrMeta) {
    if (attrMeta.isCollectionLike() || attrMeta.isMapLike()) {
      return repositoryMetadataFor(attrMeta.elementType());
    } else {
      return repositoryMetadataFor(attrMeta.type());
    }
  }

}