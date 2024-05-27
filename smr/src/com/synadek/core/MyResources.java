/**
 * MyResources.java
 * 3 Apr 2016
 * @author Daniel McCue
 */
/**
 * MyResources.java
 * 
 * @author Daniel McCue 15 May 2011
 */

package com.synadek.core;

import java.util.ListResourceBundle;

/**
 * English (default) resources for user messages, labels, etc.
 */
public class MyResources extends ListResourceBundle {
  /**
   * Default constructor for MyResources.
   */
  public MyResources() {
    super();
  }

  /**
   * getContents returns the resource elements.
   * 
   * @return the array of tag, value pairs of resources. (non-Javadoc)
   * @see java.util.ListResourceBundle#getContents()
   * @formatter:off
   */
  @Override
  protected final Object[][] getContents() {
    return new Object[][] { { "button.About", "About" }, { "button.Ok", "Ok" },

        { "label.calm", "Calm" }, { "label.freshbreeze", "Fresh breeze" }, { "label.gale", "Gale" },
        { "label.gentlebreeze", "Gentle breeze" }, { "label.hurricane", "Hurricane" },
        { "label.lightair", "Light air" }, { "label.lightbreeze", "Light breeze" },
        { "label.moderatebreeze", "Moderate breeze" }, { "label.neargale", "Near gale" },
        { "label.severegale", "Severe gale" }, { "label.storm", "Storm" },
        { "label.strongbreeze", "Strong breeze" }, { "label.violentstorm", "Violent storm" },

        { "msg.water.calm", "Sea like a mirror" },
        { "msg.water.freshbreeze",
            "Moderate waves, taking a more pronounced long form; "
                + "many white horses are formed. Chance of some spray." },
        { "msg.water.gale",
            "Moderately high waves of greater length; "
                + "edges of crests begin to break into spindrift. "
                + "The foam is blown in well-marked streaks along the direction of the wind." },
        { "msg.water.gentlebreeze",
            "Large wavelets. Crests begin to break. Foam of glassy appearance. "
                + "Perhaps scattered white horses." },
        { "msg.water.hurricane",
            "The air is filled with foam and spray. "
                + "Sea completely white with driving spray; visibility very seriously affected." },
        { "msg.water.lightair",
            "Ripples with the appearance of scales are formed, but without foam crests" },
        { "msg.water.lightbreeze",
            "Small wavelets, still short, but more pronounced. "
                + "Crests have a glassy appearance and do not break." },
        { "msg.water.moderatebreeze",
            "Small waves, becoming larger; fairly frequent white horses." },
        { "msg.water.neargale",
            "Sea heaps up and white foam from breaking waves begins to be blown in streaks"
                + " along the direction of the wind" },
        { "msg.water.severegale",
            "High waves. Dense streaks of foam along the direction of the wind."
                + " Crests of waves begin to topple, tumble and roll over. Spray may affect visibility." },
        { "msg.water.storm",
            "Very high waves with long over-hanging crests. The resulting foam, in great patches,"
                + " is blown in dense white streaks along the direction of the wind."
                + " On the whole the surface of the sea takes on a white appearance."
                + " The 'tumbling' of the sea becomes heavy and shock-like. Visibility affected." },
        { "msg.water.strongbreeze",
            "Large waves begin to form; the white foam crests are more extensive everywhere. "
                + "Probably some spray." },
        { "msg.water.violentstorm",
            "Exceptionally high waves (small and medium-size ships might disappear behind the waves)."
                + " The sea is completely covered with long white patches of foam flying"
                + " along the direction of the wind. Everywhere the edges of the wave crests are blown into froth."
                + " Visibility affected." },

        { "value.no", "no" }, { "value.yes", "yes" }, { "value.Simulated", "simulated" },

    };
  }
}
