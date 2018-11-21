package mage.cards.a;

import java.util.UUID;
import mage.abilities.effects.common.DamageWithPowerTargetEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.TargetController;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.permanent.ControllerPredicate;
import mage.target.common.TargetControlledCreaturePermanent;
import mage.target.common.TargetCreaturePermanent;

/**
 *
 * @author TheElk801
 */
public final class AggressiveInstinct extends CardImpl {

    private static final FilterCreaturePermanent filter = new FilterCreaturePermanent("creature you don't control");

    static {
        filter.add(new ControllerPredicate(TargetController.NOT_YOU));
    }

    public AggressiveInstinct(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.SORCERY}, "{1}{G}");

        // Target creature you control deals damage equal to its power to target creature you don't control.
        this.getSpellAbility().addEffect(new DamageWithPowerTargetEffect());
        this.getSpellAbility().addTarget(new TargetControlledCreaturePermanent());
        this.getSpellAbility().addTarget(new TargetCreaturePermanent(filter));
    }

    public AggressiveInstinct(final AggressiveInstinct card) {
        super(card);
    }

    @Override
    public AggressiveInstinct copy() {
        return new AggressiveInstinct(this);
    }
}
