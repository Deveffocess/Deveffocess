package com.livo.nuo.lib.stepperview;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;

interface IStepperView {

	/**
	 * Get stepper adapter
	 *
	 * @return Stepper Adapter
	 */
	IStepperAdapter getStepperAdapter();

	/**
	 * Return the index of current step
	 *
	 * @return Index
	 */
	int getCurrentStep();

	/**
	 * Get normal point color
	 *
	 * @return Normal Point Color
	 */
	@ColorInt
	int getNormalColor();

	/**
	 * Get activated point color
	 *
	 * @return Activated Point Color
	 */
	@ColorInt
	int getActivatedColor();

	/**
	 * Get line color
	 *
	 * @return Line Color
	 */
	@ColorInt
	int getLineColor();

	/**
	 * Get error highlight color
	 *
	 * @return Error Highlight Color
	 */
	@ColorInt
	int getErrorColor();

	/**
	 * Get animation duration
	 *
	 * @return Animation Duration
	 */
	int getAnimationDuration();

	/**
	 * Get done icon drawable
	 *
	 * @return Done icon drawable
	 */
	Drawable getDoneIcon();

    /**
     * Should show summary always
     *
     * @return If should show summary always
     */
    boolean isAlwaysShowSummary();

}
