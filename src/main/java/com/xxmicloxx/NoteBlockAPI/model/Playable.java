package com.xxmicloxx.NoteBlockAPI.model;

import java.io.File;

public interface Playable extends Cloneable {
    /**
     * Gets the length in ticks of this Song
     * @return length of this Song
     * @deprecated short is too small to fit midi length
     */
    @Deprecated
    default short getLength() {
        return (short) getLengthInTicks();
    }

    /**
     * Gets the length in ticks of this Song
     * @return length of this song in ticks
     */
    long getLengthInTicks();

    /**
     * Gets the title / name of this Song
     * @return title of the Song
     */
    String getTitle();

    /**
     * Gets the author of the Song
     *
     * @return author
     */
    String getAuthor();

    /**
     * Gets the original author of the Song
     * @return author
     */
    default String getOriginalAuthor() {
        return getAuthor();
    }

    /**
     * Returns the File from which this Song is sourced
     * @return file of this Song
     */
    File getPath();

    /**
     * Gets the description of this Song
     * @return description
     */
    String getDescription();

    /**
     * Gets the speed (ticks per second) of this Song
     * @return speed
     */
    double getSpeed();
}
