package com.jzs.common.keyguard;

public interface IJzsViewMediatorCallback {
	
	public void userActivity();

    /**
     * Reports user activity and requests that the screen stay on for at least
     * the specified amount of time.
     * @param millis The amount of time in millis.  This value is currently ignored.
     */
	public void userActivity(long millis);

    /**
     * Report that the keyguard is done.
     * @param authenticated Whether the user securely got past the keyguard.
     *   the only reason for this to be false is if the keyguard was instructed
     *   to appear temporarily to verify the user is supposed to get past the
     *   keyguard, and the user fails to do so.
     */
	public void keyguardDone(boolean authenticated);

    /**
     * Report that the keyguard is done drawing.
     */
	public void keyguardDoneDrawing();

    /**
     * Tell ViewMediator that the current view needs IME input
     * @param needsInput
     */
	public void setNeedsInput(boolean needsInput);

    /**
     * Tell view mediator that the keyguard view's desired user activity timeout
     * has changed and needs to be reapplied to the window.
     */
	public void onUserActivityTimeoutChanged();

    /**
     * Report that the keyguard is dismissable, pending the next keyguardDone call.
     */
	public void keyguardDonePending();

    /**
     * Report when keyguard is actually gone
     */
	public void keyguardGone();
}
