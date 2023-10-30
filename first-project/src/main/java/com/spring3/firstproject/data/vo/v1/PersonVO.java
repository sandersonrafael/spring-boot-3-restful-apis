package com.spring3.firstproject.data.vo.v1;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.lang.Nullable;

import com.github.dozermapper.core.Mapping;

// import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

// @JsonPropertyOrder({"variavel1", "variavel2", ..., "variaveln"}) -> utilizado para definir a ordem de exibição dos atributos no JSON de resposta da API
@JsonPropertyOrder({"id"}) // pode apresentar erro se for trabalhar com YAML
public class PersonVO extends RepresentationModel<PersonVO> implements Serializable {
    private static final long serialVersionUID = 1L;

    // @JsonProperty(value = "nome_do_atributo_no_json") -> determina como o atributo será exibido após na response

    @Mapping("id")
    @JsonProperty("id")
    private Long key;

    // @JsonProperty(value = "first_name") // pode apresentar erro se for trabalhar com YAML
    private String firstName;

    // @JsonProperty(value = "last_name") // pode apresentar erro se for trabalhar com YAML
    private String lastName;

    private String address;

    // @JsonIgnore -> faz com que a propriedade do objeto não seja exibida na response
    // @JsonIgnore // pode apresentar erro se for trabalhar com YAML
    private String gender;

    public PersonVO() {
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        return result;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (obj == null || getClass() != obj.getClass())
            return false;
        PersonVO other = (PersonVO) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        if (gender == null) {
            if (other.gender != null)
                return false;
        } else if (!gender.equals(other.gender))
            return false;
        return true;
    }
}
