package edu.home.subscriptionservice.data.subscription.parameter;

import edu.home.subscriptionservice.data.parameter.Parameter;
import edu.home.subscriptionservice.data.subscription.Id;
import edu.home.subscriptionservice.data.user.User;
import edu.home.subscriptionservice.dto.AddMultiStringParamSubscriptionDTO;
import edu.home.subscriptionservice.dto.AddParameterSubscriptionDTO;
import edu.home.subscriptionservice.dto.ParameterSubscriptionDTO;
import jakarta.persistence.*;

@Entity
@Inheritance
@DiscriminatorValue("PS")
public class ParameterSubscription {

    private static final String USER_ID_COLUMN = "user_id";

    private static final String PARAMETER_ID_COLUMN = "parameter_id";

    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = Id.FIRST_ENTITY_ID, column = @Column(name = USER_ID_COLUMN)),
        @AttributeOverride(name = Id.SECOND_ENTITY_ID, column = @Column(name = PARAMETER_ID_COLUMN))
    })
    private Id id = new Id();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = USER_ID_COLUMN, insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = PARAMETER_ID_COLUMN, insertable = false, updatable = false)
    private Parameter parameter;

    private boolean checked;

    protected ParameterSubscription() {}

    public ParameterSubscription(User user, Parameter parameter) {
        this.id = new Id(user.getId(), parameter.getId());
        this.user = user;
        this.parameter = parameter;
    }

    public static ParameterSubscription getParameterSubscription(
        User user, Parameter parameter,
        AddParameterSubscriptionDTO<?> addParameterSubscriptionDTO
    ) {
        if (addParameterSubscriptionDTO
                instanceof AddMultiStringParamSubscriptionDTO multiStringValueDTO) {
            return new MultiStringParameterSubscription(
                    user, parameter, multiStringValueDTO.getValue()
            );
        }

        throw new IllegalArgumentException();
    }

    public ParameterSubscriptionDTO toDTO() {
        return null;
    }

    public String getParameterName() {
        return parameter.getName();
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}