package com.xxmicloxx.NoteBlockAPI.model.fade;

public interface IFade {

    /**
     * Returns duration in seconds of this fade.
     * @return duration in seconds
     */
    double getDurationInSeconds();

    /**
     * Calculates volume for the specified time when fading from initial to target volume.
     * @param initialVolume Initial volume at time 0. Value from 0 to 100.
     * @param targetVolume Volume after the fading is finished. Value from 0 to 100.
     * @param time Time in seconds
     * @return Volume for the specified time from 0 to 100.
     */
    byte calculateFadeVolume(byte initialVolume, byte targetVolume, double time);
}
