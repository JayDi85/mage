
package mage.cards.f;

import mage.abilities.Ability;
import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.common.RemoveCountersSourceCost;
import mage.abilities.effects.common.DamageTargetEffect;
import mage.abilities.effects.common.counter.AddCountersSourceEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Zone;
import mage.counters.CounterType;
import mage.filter.common.FilterControlledCreaturePermanent;
import mage.game.Game;
import mage.game.events.DamagedEvent;
import mage.game.events.GameEvent;
import mage.game.events.GameEvent.EventType;
import mage.game.permanent.Permanent;
import mage.target.common.TargetAnyTarget;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author Plopman
 */
public final class FiveAlarmFire extends CardImpl {

    public FiveAlarmFire(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.ENCHANTMENT},"{1}{R}{R}");
        


        //Whenever a creature you control deals combat damage, put a blaze counter on Five-Alarm Fire.
        this.addAbility(new FiveAlarmFireTriggeredAbility());
        //Remove five blaze counters from Five-Alarm Fire: Five-Alarm Fire deals 5 damage to any target.
        Ability ability = new SimpleActivatedAbility(Zone.BATTLEFIELD, new DamageTargetEffect(5), new RemoveCountersSourceCost(CounterType.BLAZE.createInstance(5)));
        ability.addTarget(new TargetAnyTarget());
        this.addAbility(ability);
    }

    public FiveAlarmFire(final FiveAlarmFire card) {
        super(card);
    }

    @Override
    public FiveAlarmFire copy() {
        return new FiveAlarmFire(this);
    }
}


class FiveAlarmFireTriggeredAbility extends TriggeredAbilityImpl {

    // Because a creature that is blocked by multiple creatures it deals damage to, only causes to add one counter to ,
    // it's neccessary to remember which creature already triggered
    Set<UUID> triggeringCreatures = new HashSet<>();

    private static final FilterControlledCreaturePermanent filter = new FilterControlledCreaturePermanent();
    
    public FiveAlarmFireTriggeredAbility() {
        super(Zone.BATTLEFIELD, new AddCountersSourceEffect(CounterType.BLAZE.createInstance()), false);
    }

    public FiveAlarmFireTriggeredAbility(final FiveAlarmFireTriggeredAbility ability) {
            super(ability);
            triggeringCreatures.addAll(ability.triggeringCreatures);
    }

    @Override
    public FiveAlarmFireTriggeredAbility copy() {
            return new FiveAlarmFireTriggeredAbility(this);
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        return event.getType() == EventType.DAMAGED_CREATURE
                || event.getType() == EventType.DAMAGED_PLANESWALKER
                || event.getType() == EventType.DAMAGED_PLAYER
                || event.getType() == EventType.COMBAT_DAMAGE_STEP_PRE;
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        if (event.getType() == EventType.DAMAGED_CREATURE
                || event.getType() == EventType.DAMAGED_PLANESWALKER
                || event.getType() == EventType.DAMAGED_PLAYER) {
            if (((DamagedEvent) event).isCombatDamage() && !triggeringCreatures.contains(event.getSourceId())) {
                Permanent permanent = game.getPermanent(event.getSourceId());
                if (permanent != null && filter.match(permanent, sourceId, controllerId, game)) {
                    triggeringCreatures.add(event.getSourceId());
                    return true;
                }
            }
        }
        // reset the remembered creatures for every combat damage step
        if (event.getType() == EventType.COMBAT_DAMAGE_STEP_PRE) {
            triggeringCreatures.clear();
        }
        return false;
    }

    @Override
    public String getRule() {
            return "Whenever a creature you control deals combat damage, " + super.getRule();
    }

}
