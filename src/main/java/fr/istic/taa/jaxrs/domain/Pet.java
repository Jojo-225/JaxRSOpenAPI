package fr.istic.taa.jaxrs.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.GeneratorType;

import io.swagger.v3.oas.models.tags.Tag;
import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "pet")
@XmlRootElement(name = "Pet")
public class Pet implements Serializable {

  @Id
 @GeneratedValue(generator = "increment")
  private Long id;

  private String name;
  private List<Tag> tags = new ArrayList<Tag>();

  @XmlElement(name = "id")
  public Long getId() {
    return id;
  }


  @XmlElement(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @XmlElementWrapper(name = "tags")
  @XmlElement(name = "tag")
  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }
}