package com.sparnord.heatmaps.contextualized;

public class CompositeIdNode {

  String id_assessedObject;
  String id_contextObject;
  String id_session;

  public String getId_assessedObject() {
    return this.id_assessedObject;
  }

  public void setId_assessedObject(final String id_assessedObject) {
    this.id_assessedObject = id_assessedObject;
  }

  public String getId_contextObject() {
    return this.id_contextObject;
  }

  public void setId_contextObject(final String id_contextObject) {
    this.id_contextObject = id_contextObject;
  }

  public String getId_session() {
    return this.id_session;
  }

  public void setId_session(final String id_session) {
    this.id_session = id_session;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((this.id_assessedObject == null) ? 0 : this.id_assessedObject.hashCode());
    result = (prime * result) + ((this.id_contextObject == null) ? 0 : this.id_contextObject.hashCode());
    result = (prime * result) + ((this.id_session == null) ? 0 : this.id_session.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    CompositeIdNode other = (CompositeIdNode) obj;
    if (this.id_assessedObject == null) {
      if (other.id_assessedObject != null) {
        return false;
      }
    } else if (!this.id_assessedObject.equals(other.id_assessedObject)) {
      return false;
    }
    if (this.id_contextObject == null) {
      if (other.id_contextObject != null) {
        return false;
      }
    } else if (!this.id_contextObject.equals(other.id_contextObject)) {
      return false;
    }
    if (this.id_session == null) {
      if (other.id_session != null) {
        return false;
      }
    } else if (!this.id_session.equals(other.id_session)) {
      return false;
    }
    return true;
  }

}

