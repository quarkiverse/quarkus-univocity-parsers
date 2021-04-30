package io.quarkiverse.univocityparsers.it.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import com.univocity.parsers.annotations.*;

import io.quarkiverse.univocityparsers.it.converters.ConversionDateCSV;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * A simple example for testing
 * to parser a csv file which
 * represent several data file
 */
@RegisterForReflection
public class CSVFileExample implements Serializable {

    @RegisterForReflection
    public enum Type {
        USER('U'),
        SYSTEM('S');

        public final char typeCode;

        Type(char typeCode) {
            this.typeCode = typeCode;
        }
    }

    @Trim
    @Parsed(field = "CODE")
    private String code;

    @Parsed(field = "YEARS")
    private Integer years;

    @Parsed(field = "DATE")
    @Convert(conversionClass = ConversionDateCSV.class)
    private LocalDate date;

    @Trim
    @Parsed(field = "WEIGHT", defaultNullRead = "0")
    private Float weight;

    @Parsed(field = "BILLABLE")
    @BooleanString(trueStrings = "1", falseStrings = "0")
    private Boolean billable;

    @Parsed(field = "PROFILE")
    private Long profile;

    @Parsed(field = "USER")
    @Trim
    @UpperCase
    private String user;

    @Parsed(field = "CREATED_AT")
    @Format(formats = { "yyyy-MM-dd", "dd/MM/yyyy" }, options = "locale=en;lenient=false")
    private Date createdAt;

    @Parsed(field = "FEES")
    @Replace(expression = "\\$", replacement = "")
    @Format(formats = { "#0,00" }, options = "decimalSeparator=,")
    private BigDecimal fees;

    @Parsed(field = "ADMIN")
    @BooleanString(trueStrings = { "yes", "y" }, falseStrings = { "no", "n" })
    private boolean admin;

    @Parsed(field = "TYPE_CODE")
    @EnumOptions(customElement = "typeCode")
    private Type type;

    @Parsed(field = "STARTS")
    @NullString(nulls = { "?", "N/A" })
    private Integer stars;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getYears() {
        return years;
    }

    public void setYears(Integer years) {
        this.years = years;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Boolean getBillable() {
        return billable;
    }

    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    public Long getProfile() {
        return profile;
    }

    public void setProfile(Long profile) {
        this.profile = profile;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CSVFileExample that = (CSVFileExample) o;
        return admin == that.admin && Objects.equals(code, that.code) && Objects.equals(years, that.years) && Objects
                .equals(date, that.date) && Objects.equals(weight, that.weight) && Objects.equals(billable, that.billable)
                && Objects.equals(profile, that.profile) && Objects.equals(user, that.user) && Objects
                        .equals(createdAt, that.createdAt)
                && Objects.equals(fees, that.fees) && type == that.type && Objects
                        .equals(stars, that.stars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, years, date, weight, billable, profile, user, createdAt, fees, admin, type, stars);
    }

    public CSVFileExample(String code, Integer years, LocalDate date, Float weight, Boolean billable, Long profile, String user,
            Date createdAt, BigDecimal fees, boolean admin, Type type, Integer stars) {
        this.code = code;
        this.years = years;
        this.date = date;
        this.weight = weight;
        this.billable = billable;
        this.profile = profile;
        this.user = user;
        this.createdAt = createdAt;
        this.fees = fees;
        this.admin = admin;
        this.type = type;
        this.stars = stars;
    }

    public CSVFileExample() {

    }

    @Override
    public String toString() {
        return "CSVFileExample{" +
                "code='" + code + '\'' +
                ", years=" + years +
                ", date=" + date +
                ", weight=" + weight +
                ", billable=" + billable +
                ", profile=" + profile +
                ", user='" + user + '\'' +
                ", createdAt=" + createdAt +
                ", fees=" + fees +
                ", admin=" + admin +
                ", type=" + type +
                ", stars=" + stars +
                '}';
    }
}
