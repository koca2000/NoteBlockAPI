package com.xxmicloxx.NoteBlockAPI.songplayer;

import com.xxmicloxx.NoteBlockAPI.model.FadeType;
import com.xxmicloxx.NoteBlockAPI.model.fade.LinearFade;
import com.xxmicloxx.NoteBlockAPI.model.fade.NoFade;
import org.jetbrains.annotations.NotNull;

@Deprecated
public class Fade {

	private FadeInstance fade;
	private double tempo;
	
	/**
	 * Create new fade effect
	 * @param type Type of fade effect
	 * @param fadeDuration - duration of fade effect in ticks
	 */
	@Deprecated
	public Fade(@NotNull FadeType type, int fadeDuration){
		// Assumes that most common tempo is close to 10 tps
		fade = new FadeInstance(type == FadeType.LINEAR ? new LinearFade(fadeDuration / 10d) : NoFade.Instance);
		tempo = 10;
	}

	Fade(@NotNull FadeInstance fadeInstance, double songTempo){
		fade = fadeInstance;
		tempo = songTempo;
	}

	@Deprecated
	protected byte calculateFade() {
		if (fade.isDone())
			return -1;
		return fade.calculateVolume(0);
	}

	@Deprecated
	protected int getFadeDone() {
		return (int) Math.round(fade.getElapsedTime() / tempo);
	}

	@Deprecated
	protected void setFadeStart(byte fadeStart) {
		fade.setInitialVolume(fadeStart);
	}

	@Deprecated
	protected void setFadeTarget(byte fadeTarget) {
		fade.setTargetVolume(fadeTarget);
	}

	/**
	 * Returns fade effect type
	 * @return {@link FadeType}
	 */
	@Deprecated
	@NotNull
	public FadeType getType() {
		return fade.getFade() instanceof LinearFade ? FadeType.LINEAR : FadeType.NONE;
	}

	/**
	 * Set fade effect type
	 * @param type FadeType
	 */
	@Deprecated
	public void setType(@NotNull FadeType type) {
		FadeInstance newFade = new FadeInstance(type == FadeType.LINEAR ? new LinearFade(fade.getFade().getDurationInSeconds()) : NoFade.Instance);
		newFade.setInitialVolume(fade.getInitialVolume());
		newFade.setElapsedTime(fade.getElapsedTime());
		newFade.setTargetVolume(fade.getTargetVolume());
		fade = newFade;
	}

	/**
	 * Returns duration of fade effect
	 * @return duration in ticks
	 */
	@Deprecated
	public int getFadeDuration() {
		return (int) Math.round(fade.getFade().getDurationInSeconds() / tempo);
	}

	/**
	 * Set fade effect duration
	 * @param fadeDuration duration in ticks
	 */
	@Deprecated
	public void setFadeDuration(int fadeDuration) {
		FadeInstance newFade = new FadeInstance(getType() == FadeType.LINEAR ? new LinearFade(fadeDuration) : NoFade.Instance);
		newFade.setInitialVolume(fade.getInitialVolume());
		newFade.setElapsedTime(fade.getElapsedTime());
		newFade.setTargetVolume(fade.getTargetVolume());
		fade = newFade;
	}

	@Deprecated
	protected byte getFadeStart() {
		return fade.getInitialVolume();
	}

	@Deprecated
	protected byte getFadeTarget() {
		return fade.getTargetVolume();
	}

	@Deprecated
	protected void setFadeDone(int fadeDone){
		this.fade.setElapsedTime(fadeDone * tempo);
	}

	void setTempo(double tempo){
		this.tempo = tempo;
	}

	FadeInstance getFadeInstance(){
		return fade;
	}

	@Deprecated
	public boolean isDone(){
	    return fade.isDone();
    }
}
