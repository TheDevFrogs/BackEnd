package ca.usherbrooke.remisetravaux.validator;

import br.com.fluentvalidator.AbstractValidator;
import ca.usherbrooke.remisetravaux.business.Assignment;

import java.util.Objects;

import static br.com.fluentvalidator.predicate.ComparablePredicate.greaterThanOrEqual;
import static br.com.fluentvalidator.predicate.ObjectPredicate.nullValue;
import static java.util.function.Predicate.not;
public class AssignmentValidator extends AbstractValidator<Assignment> {
    @Override
    public void rules() {
        ruleFor(Assignment::getId_group)
                .must(not(nullValue()));

        ruleFor(Assignment::getName)
                .must(not(nullValue()));

        ruleFor(Assignment::getDue_date)
                .must(not(nullValue()));

        ruleFor(Assignment::getTeam_size)
                .must(greaterThanOrEqual(1));
    }
}
