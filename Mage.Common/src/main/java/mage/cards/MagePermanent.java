package mage.cards;

import mage.view.PermanentView;

import java.util.List;

public abstract class MagePermanent extends MageCard {
    private static final long serialVersionUID = -3469258620601702171L;
    public abstract List<MagePermanent> getLinks();
    public abstract void update(PermanentView card);
    public abstract PermanentView getOriginalPermanent();

}