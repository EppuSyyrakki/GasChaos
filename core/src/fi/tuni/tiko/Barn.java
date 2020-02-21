package fi.tuni.tiko;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class Barn extends Building {
    private int manureShoveled = 30;    // how much removed from barn by shoveling action
    private int feedInBarn;             // amount of food available to cows this turn
    private int manureInBarn;           // amount of manure produced on previous turn

    public Barn() {
        background = new Texture("barnBackground.png");
    }

    /**
     * Reduce data.feed by amount needed to feed all cows for 2 days.  If not enough in data.feed,
     * decrease to 0 and increase by same amount. Block if data.feed = 0, and if cows have enough.
     */
    public GameData actionFeedCows(GameData data) {
        ArrayList<Cow> cowList = data.getCowList();
        Cow cow = cowList.get(0);
        int addAmount = cowList.size() * cow.getFeed() * 2;

        if (feedInBarn > 1.5 * addAmount) {
            // TODO action blocked, cows have enough food UI message
        } else {
            if (data.getFeed() == 0) {
                // TODO action blocked, feed storage empty UI message
            } else if (data.getFeed() < addAmount) {
                feedInBarn += data.getFeed();
                data.setFeed(0);
                // TODO feed storage empty UI message
            } else {
                feedInBarn += addAmount;
                data.setFeed(-addAmount);
                // TODO cows fed UI message
            }
        }
        return data;
    }

    /**
     * Reduce manureInBarn and increase data.manure by same amount if less than data.manureMax
     */
    public GameData actionShovelManure(GameData data) {
        if (manureInBarn == 0) {
            // TODO action blocked, no manure in barn UI message
        } else if (manureInBarn < manureShoveled) {
            data.setManure(data.getManure() + manureInBarn);
            manureInBarn = 0;
            // TODO barn is clean UI message
        } else if (manureInBarn > manureShoveled) {
            data.setManure(data.getManure() + manureShoveled);
            manureInBarn -= manureShoveled;
            // TODO barn still a bit dirty UI message
        }

        if (data.getManure() > data.getManureMax()) {   // check if manure within limit
            data.setManure(data.getManureMax());
            // TODO manure pit full UI message
        }
        return data;
    }

    /**
     * Reduce methaneAmount to 0 in each of data.cowList if possible and increase data.methane by
     * same amount.
     */
    public GameData actionCollectMethane(GameData data) {
        ArrayList<Cow> tmpCowList = data.getCowList();
        int methaneCollected = 0;

        for (int i = 0; i < tmpCowList.size(); i++) {
            Cow cow = tmpCowList.get(i);
            methaneCollected += cow.getMethaneAmount();
            cow.setMethaneAmount(0);
            tmpCowList.set(i, cow);
        }

        data.setCowList(tmpCowList);
        data.setMethane(data.getMethane() + methaneCollected);
        // TODO cow methane tanks empty UI message

        if (data.getMethane() >= data.getMethaneMax()) {
            // TODO methane tank dangerously full UI message
        }

        return data;
    }

    /**
     * Update amount of this.manureInBarn from data.cowList
     */
    public void update(GameData data) {
        ArrayList<Cow> cowList = data.getCowList();

        for (Cow cow : cowList) {
            manureInBarn += cow.getManure();
            feedInBarn -= cow.getFeed();
        }
    }
}

